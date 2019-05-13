package com.sinosoft.ops.cimp.service.sys.tag.impl;

import com.google.common.collect.Lists;
import com.sinosoft.ops.cimp.entity.sys.tag.QSysTag;
import com.sinosoft.ops.cimp.entity.sys.tag.SysTag;
import com.sinosoft.ops.cimp.repository.sys.tag.CadreTagRepository;
import com.sinosoft.ops.cimp.repository.sys.tag.SysTagRepository;
import com.sinosoft.ops.cimp.service.sys.tag.SysTagService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SysTagServiceImpl implements SysTagService {
    private final SysTagRepository sysTagRepository;
    private final CadreTagRepository cadreTagRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SysTagServiceImpl(SysTagRepository sysTagRepository, CadreTagRepository cadreTagRepository, JdbcTemplate jdbcTemplate) {
        this.sysTagRepository = sysTagRepository;
        this.cadreTagRepository = cadreTagRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<SysTag> findAll() {
        return sysTagRepository.findAll();
    }

    @Override
    public List<SysTag> findAll(List<String> tagCategoryIds) {
        return Lists.newArrayList(sysTagRepository.findAll(QSysTag.sysTag.tagCategoryId.in(tagCategoryIds), QSysTag.sysTag.tagSort.asc()));
    }

    public Optional<SysTag> findSysTag(String sysTagId) {
        return sysTagRepository.findOne(QSysTag.sysTag.id.eq(sysTagId));
    }

    @Override
    public SysTag save(SysTag sysTag) {
        if (sysTag != null) {
            Map<String, Object> map = jdbcTemplate.queryForMap("SELECT MAX(TAG_SORT) AS \"tagSort\" FROM SYS_TAG ");
            Object tagSort = map.get("tagSort");
            if (tagSort != null) {
                Integer nextSort = Integer.parseInt(String.valueOf(tagSort)) + 1;
                sysTag.setTagSort(nextSort);
            }
            return sysTagRepository.save(sysTag);
        }
        return null;
    }

    @Override
    public SysTag update(SysTag sysTag) {
        return sysTagRepository.save(sysTag);
    }

    @Override
    public void delete(String sysTagId) {
        if (StringUtils.isNotEmpty(sysTagId)) {
            sysTagRepository.deleteById(sysTagId);
            String sql = "DELETE FROM CADRE_TAG WHERE TAG_ID = '%s'";
            String execSql = String.format(sql, sysTagId);
            jdbcTemplate.update(execSql);
        }
    }
}
