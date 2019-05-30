package com.sinosoft.ops.cimp.service.sys.tag.impl;

import com.google.common.collect.Lists;
import com.sinosoft.ops.cimp.entity.sys.tag.CadreTag;
import com.sinosoft.ops.cimp.entity.sys.tag.SysTag;
import com.sinosoft.ops.cimp.repository.sys.tag.CadreTagRepository;
import com.sinosoft.ops.cimp.service.sys.tag.CadreTagService;
import com.sinosoft.ops.cimp.util.IdUtil;
import com.vip.vjtools.vjkit.number.MathUtil;
import com.vip.vjtools.vjkit.number.NumberUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class CadreTagServiceImpl implements CadreTagService {

    //每批次抓取数量
    private static final int FETCH_BATCH_COUNT = 3000;
    private final CadreTagRepository cadreTagRepository;
    private final JdbcTemplate jdbcTemplate;
    // 创建线程池（默认为当前核心数2倍）
    private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    @Autowired
    public CadreTagServiceImpl(CadreTagRepository cadreTagRepository, JdbcTemplate jdbcTemplate) {
        this.cadreTagRepository = cadreTagRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int markingTag(SysTag sysTag) {
        String tagId = sysTag.getId();
        String tagCategoryId = sysTag.getTagCategoryId();
        String tagSql = sysTag.getTagSql();
        String tagCountSql = sysTag.getTagCountSql();
        String tagTargetName = sysTag.getTagTargetName();

        //执行该tag之前先把原来的删掉
        String sql = "DELETE FROM CADRE_TAG WHERE TAG_ID = '%s' AND TAG_TYPE = '0'";
        String execSql = String.format(sql, tagId);
        jdbcTemplate.update(execSql);
        int tagCountNumber = 0;
        if (StringUtils.isNotEmpty(tagCountSql)) {
            Map<String, Object> countMap = jdbcTemplate.queryForMap(tagCountSql);
            Object tagCount = countMap.get("tagCount");
            if (tagCount != null) {
                tagCountNumber = NumberUtil.toInt(String.valueOf(tagCount));
            }
        }

        //如果待打标签的数据量特别大，则必须使用分页
        int batchNumber = MathUtil.divide(tagCountNumber, FETCH_BATCH_COUNT, RoundingMode.DOWN);

        for (int i = 0; i < batchNumber; i++) {
            int fromIndex = i * FETCH_BATCH_COUNT;
            int toIndex = (i + 1) * FETCH_BATCH_COUNT;
            String pagerSql = this.addPager(tagSql, fromIndex, toIndex);
            if (StringUtils.isNotEmpty(pagerSql)) {
                List<Map<String, Object>> mapList = jdbcTemplate.queryForList(pagerSql);
                List<CadreTag> cadreTagList = Lists.newArrayListWithCapacity(FETCH_BATCH_COUNT);
                for (Map<String, Object> map : mapList) {
                    CadreTag cadreTag = new CadreTag();

                    Object o = map.get(tagTargetName);
                    cadreTag.setSubId(IdUtil.uuidWithoutMinus());
                    cadreTag.setTagCategoryId(tagCategoryId);
                    cadreTag.setTagId(tagId);
                    cadreTag.setEmpId(String.valueOf(o));
                    cadreTag.setTagType("0");
                    cadreTag.setCreateTime(new Date());
                    
                    cadreTagList.add(cadreTag);
                }
                cadreTagRepository.saveAll(cadreTagList);
                cadreTagRepository.flush();
            }
        }
        if (batchNumber * FETCH_BATCH_COUNT < tagCountNumber) {
            int fromIndex = batchNumber * FETCH_BATCH_COUNT;
            int toIndex = tagCountNumber;
            String pagerSql = this.addPager(tagSql, fromIndex, toIndex);
            assert pagerSql != null;
            List<Map<String, Object>> mapList = jdbcTemplate.queryForList(pagerSql);

            List<CadreTag> cadreTagList = Lists.newArrayListWithCapacity(mapList.size());
            for (Map<String, Object> map : mapList) {
                Object o = map.get(tagTargetName);
                CadreTag cadreTag = new CadreTag();

                cadreTag.setSubId(IdUtil.uuidWithoutMinus());
                cadreTag.setTagCategoryId(tagCategoryId);
                cadreTag.setTagId(tagId);
                cadreTag.setEmpId(String.valueOf(o));

                cadreTagList.add(cadreTag);
            }
            cadreTagRepository.saveAll(cadreTagList);
            cadreTagRepository.flush();
        }
        return tagCountNumber;
    }

    @Override
    public void parallelMarkingTag(List<SysTag> sysTagList) {
        if (sysTagList != null && sysTagList.size() > 0) {
            CompletionService completionService = new ExecutorCompletionService(executorService);
            int jobCount = sysTagList.size();
            for (SysTag sysTag : sysTagList) {
                completionService.submit(() -> markingTag(sysTag), 0L);
            }
//            int finishJobCount = 0;
//            while (finishJobCount < jobCount) { // 判断提交线程数和完成线程数是否相等
//                if (completionService.poll() != null) {
//                    finishJobCount++;
//                }
//            }
//            executorService.shutdown();
        }
    }

    @Override
    public void deleteTag(String tagId) {
        if (StringUtils.isNotEmpty(tagId)) {
            String sql = "DELETE FROM CADRE_TAG WHERE TAG_ID = '%s' WHERE CADRE_TYPE = '0'";
            String execSql = String.format(sql, tagId);
            jdbcTemplate.update(execSql);
        }
    }

    private String addPager(String sql, int startRow, int endRow) {
        if (StringUtils.isNotEmpty(sql)) {
            String pageSqlPrefix = "SELECT * FROM ( SELECT PAGE_DATA.*, ROWNUM RN FROM ( ";
            String pageSqlSuffixTemplate = ") PAGE_DATA WHERE ROWNUM <= %s ) WHERE RN > %s";
            String pageSqlSuffix = String.format(pageSqlSuffixTemplate, endRow, startRow);
            return pageSqlPrefix + sql + pageSqlSuffix;
        }
        return null;
    }


}
