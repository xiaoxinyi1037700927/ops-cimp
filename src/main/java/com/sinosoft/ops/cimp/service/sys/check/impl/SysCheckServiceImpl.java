package com.sinosoft.ops.cimp.service.sys.check.impl;

import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.sys.check.QSysCheckCondition;
import com.sinosoft.ops.cimp.entity.sys.check.SysCheckCondition;
import com.sinosoft.ops.cimp.mapper.sys.check.SysCheckConditionMapper;
import com.sinosoft.ops.cimp.repository.oraganization.OrganizationRepository;
import com.sinosoft.ops.cimp.repository.sys.check.SysCheckConditionRepository;
import com.sinosoft.ops.cimp.repository.sys.check.SysCheckTypeRepository;
import com.sinosoft.ops.cimp.schedule.syscheck.SysCheckResultTables;
import com.sinosoft.ops.cimp.schedule.syscheck.SysCheckTypeAdapter;
import com.sinosoft.ops.cimp.service.sys.check.SysCheckService;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.vo.from.sys.check.*;
import com.sinosoft.ops.cimp.vo.to.sys.check.SysCheckConditionModel;
import com.sinosoft.ops.cimp.vo.to.sys.check.SysCheckEmpModel;
import com.sinosoft.ops.cimp.vo.to.sys.check.SysCheckTreeNode;
import com.sinosoft.ops.cimp.vo.to.sys.check.SysCheckTypeModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class SysCheckServiceImpl implements SysCheckService {

    private final SysCheckConditionRepository sysCheckConditionRepository;
    private final SysCheckTypeRepository sysCheckTypeRepository;
    private final JdbcTemplate jdbcTemplate;
    private final JPAQueryFactory jpaQueryFactory;
    private final SysCheckTypeAdapter[] typeAdapters;
    private final OrganizationRepository organizationRepository;

    @Autowired
    public SysCheckServiceImpl(SysCheckConditionRepository sysCheckConditionRepository, SysCheckTypeRepository sysCheckTypeRepository, JdbcTemplate jdbcTemplate, JPAQueryFactory jpaQueryFactory, SysCheckTypeAdapter[] typeAdapters, OrganizationRepository organizationRepository) {
        this.sysCheckConditionRepository = sysCheckConditionRepository;
        this.sysCheckTypeRepository = sysCheckTypeRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.jpaQueryFactory = jpaQueryFactory;
        this.typeAdapters = typeAdapters;
        this.organizationRepository = organizationRepository;
    }

    /**
     * 系统查错类型
     */
    @Override
    public List<SysCheckTypeModel> listSysCheckType() {
        return sysCheckTypeRepository.findAll().stream().map(type -> {
            SysCheckTypeModel model = new SysCheckTypeModel();
            model.setId(type.getId());
            model.setName(type.getName());
            return model;
        }).collect(Collectors.toList());
    }

    /**
     * 系统查错条件列表
     */
    @Override
    public PaginationViewModel<SysCheckConditionModel> listSysCheckCondition(SysCheckConditionSearchModel searchModel) {
        int pageSize = searchModel.getPageSize();
        int pageIndex = searchModel.getPageIndex();
        //是否分页
        boolean isPaging = pageSize > 0 && pageIndex > 0;

        QSysCheckCondition qCondition = QSysCheckCondition.sysCheckCondition;
        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.isNotEmpty(searchModel.getTypeId())) {
            builder.and(qCondition.typeId.eq(searchModel.getTypeId()));
        }
        if (StringUtils.isNotEmpty(searchModel.getName())) {
            builder.and(qCondition.name.contains(searchModel.getName().trim()));
        }
        if (StringUtils.isNotEmpty(searchModel.getWherePart())) {
            builder.and(qCondition.wherePart.contains(searchModel.getWherePart().trim()));
        }
        if (StringUtils.isNotEmpty(searchModel.getSysTableNameEn())) {
            builder.and(qCondition.sysTableNameEn.contains(searchModel.getSysTableNameEn()));
        }

        List<SysCheckConditionModel> models = null;
        long total = 0;
        if (isPaging) {
            PageRequest pageRequest = PageRequest.of(pageIndex - 1, pageSize);
            Page<SysCheckCondition> page = sysCheckConditionRepository.findAll(builder, pageRequest);
            models = page.getContent().stream().map(SysCheckConditionMapper.INSTANCE::sysCheckConditionToModel).collect(Collectors.toList());
            total = page.getTotalElements();
        } else {
            Iterable<SysCheckCondition> iterable = sysCheckConditionRepository.findAll(builder);
            models = StreamSupport.stream(iterable.spliterator(), false).map(SysCheckConditionMapper.INSTANCE::sysCheckConditionToModel).collect(Collectors.toList());
            total = models.size();
        }

        return new PaginationViewModel
                .Builder<SysCheckConditionModel>()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .totalCount(total)
                .data(models)
                .build();
    }

    /**
     * 添加查错条件
     */
    @Override
    public void addSysCheckCondition(SysCheckConditionAddModel addModel) {
        SysCheckCondition condition = SysCheckConditionMapper.INSTANCE.addModelToSysCheckCondition(addModel);

        QSysCheckCondition qCondition = QSysCheckCondition.sysCheckCondition;
        Integer sort = jpaQueryFactory.select(qCondition.sort.max()).from(qCondition).fetchOne();
        condition.setSort(sort != null ? ++sort : 0);

        sysCheckConditionRepository.save(condition);
    }

    /**
     * 修改查错条件
     */
    @Override
    public boolean modifySysCheckCondition(SysCheckConditionModifyModel modifyModel) {
        Optional<SysCheckCondition> conditionOptional = sysCheckConditionRepository.findById(modifyModel.getId());

        if (!conditionOptional.isPresent()) {
            return false;
        }

        SysCheckCondition condition = conditionOptional.get();
        SysCheckConditionMapper.INSTANCE.modifyModelToSysCheckCondition(modifyModel, condition);
        sysCheckConditionRepository.save(condition);

        return true;
    }

    /**
     * 删除查错条件
     */
    @Override
    public void deleteSysCheckCondition(List<String> ids) {
        if (ids == null || ids.size() == 0) {
            return;
        }

        for (String id : ids) {
            sysCheckConditionRepository.deleteById(id);
        }
    }

    /**
     * 查错结果
     */
    @Override
    public SysCheckResultModel listSysCheckResult(SysCheckSearchModel searchModel) {
        //当前用户数据权限
        String dataOrgId = SecurityUtils.getSubject().getCurrentUser().getDataOrganizationId();
        String code = organizationRepository.findById(dataOrgId).get().getCode();

        //获取结果集的表名
        String id = jdbcTemplate.queryForMap("SELECT RESULT_TABLES FROM SYS_CHECK_RESULT_TABLES WHERE ROWNUM = 1").get("RESULT_TABLES").toString();
        SysCheckResultTables resultTables = SysCheckResultTables.getTables(id);

        //查错项
        QSysCheckCondition qCondition = QSysCheckCondition.sysCheckCondition;
        Iterable<SysCheckCondition> iterable = sysCheckConditionRepository.findAll(qCondition.sort.asc());
        List<SysCheckCondition> conditions = Lists.newArrayList(iterable);

        //获取每个查错项的总数和错误数
        int totalWrongNum = 0;
        List<SysCheckResultItemModel> items = new ArrayList<>();
        try {
            for (SysCheckCondition condition : conditions) {
                SysCheckResultItemModel item = new SysCheckResultItemModel();
                item.setConditionId(condition.getId());
                item.setConditionName(condition.getName());

                SysCheckTypeAdapter typeAdapter = getTypeAdapter(condition.getTypeId());
                Integer total = typeAdapter.getTotalNum(jdbcTemplate, resultTables.getResultsName(), code);
                Integer wrongNum = typeAdapter.getWrongNum(jdbcTemplate, resultTables.getResultsTempName(), code, condition.getId());

                totalWrongNum += wrongNum;
                item.setWrongNum(wrongNum);
                item.setCompleteSchedule(new BigDecimal((total - wrongNum) / (double) total * 100).intValue());

                if (searchModel.isWrongOnly() && wrongNum == 0) {
                    continue;
                }

                items.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        SysCheckResultModel result = new SysCheckResultModel();
        result.setConditionNum(conditions.size());
        result.setWrongNum(totalWrongNum);
        result.setItems(items);

        return result;
    }

    /**
     * 获取查错机构树
     */
    @Override
    public List<SysCheckTreeNode> getOrgTree(SysCheckOrgTreeSearchModel searchModel) {
        SysCheckCondition condition = sysCheckConditionRepository.findById(searchModel.getConditionId()).get();

        //获取结果集的表名
        String id = jdbcTemplate.queryForMap("SELECT RESULT_TABLES FROM SYS_CHECK_RESULT_TABLES WHERE ROWNUM = 1").get("RESULT_TABLES").toString();
        SysCheckResultTables resultTables = SysCheckResultTables.getTables(id);

        //如果请求参数中单位id为空，根据当前用户的数据权限查询，否则，查询其下级单位
        String depCodeSqlWhere = " select B001001_A from dep_b001 where ";
        if (StringUtils.isNotEmpty(searchModel.getOrgId())) {
            depCodeSqlWhere += "pptr in (select B001001_A from dep_b001 where dep_id ='" + searchModel.getOrgId() + "') ";
        } else {
            //当前用户的数据权限
            String dataOrgId = SecurityUtils.getSubject().getCurrentUser().getDataOrganizationId();
            depCodeSqlWhere += "DEP_ID ='" + dataOrgId + "'";
        }

        try {
            SysCheckTypeAdapter typeAdapter = getTypeAdapter(condition.getTypeId());


            return typeAdapter.getTreeNodes(jdbcTemplate, resultTables.getResultsTempName(), searchModel.getConditionId(), depCodeSqlWhere);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public PaginationViewModel<SysCheckEmpModel> getEmpList(SysCheckEmpSearchModel searchModel) {
        SysCheckCondition condition = sysCheckConditionRepository.findById(searchModel.getConditionId()).get();

        int pageIndex = searchModel.getPageIndex() > 0 ? searchModel.getPageIndex() : 1;
        int pageSize = searchModel.getPageSize() > 0 ? searchModel.getPageSize() : 10;
        int startIndex = (pageIndex - 1) * pageSize;
        int endIndex = pageIndex * pageSize;

        String wherePart = condition.getWherePart();
        if (StringUtils.isNotBlank(searchModel.getKeywords())) {
            wherePart += " and t1.A01001 || b001.B01001 like '%" + searchModel.getKeywords().trim() + "%'";
        }

        //获取单位的code
        String code = jdbcTemplate.queryForMap("SELECT TREE_LEVEL_CODE AS code FROM DEP_B001 WHERE DEP_ID = '" + searchModel.getOrgId() + "'").get("code").toString();

        String sql = "SELECT EMP_ID AS \"empId\",\n" +
                "  A01001 AS \"name\",\n" +
                "  (SELECT NAME FROM SYS_CODE_ITEM WHERE CODE_SET_ID = 18 AND CODE = A01004) AS \"gender\",\n" +
                "  TO_CHAR(A01007, 'YYYY-MM-DD') AS \"birthday\",\n" +
                "  (SELECT B01001 FROM DEP_B001 WHERE DEP_ID = A001004_A) AS \"organization\",\n" +
                "  (SELECT LISTAGG(A02016_A, '，') WITHIN GROUP ( ORDER BY EMP_ID) FROM EMP_A02 WHERE EMP_ID = T.EMP_ID AND A02001_B = T.A001004_A GROUP BY T.EMP_ID) AS \"position\",\n" +
                "  (SELECT LISTAGG(A05002_A, '，') WITHIN GROUP ( ORDER BY EMP_ID) FROM EMP_A05 WHERE EMP_ID = T.EMP_ID GROUP BY T.EMP_ID) AS \"positionLevel\"\n" +
                "FROM (SELECT ROWNUM AS RN,T.*\n" +
                "      FROM (SELECT T1.*\n" +
                "            FROM EMP_A001 T1\n" +
                "            INNER JOIN DEP_B001 B001 ON B001.DEP_ID = T1.A001004_A AND B001.TREE_LEVEL_CODE LIKE '" + code + "%'\n" +
                "            WHERE T1.STATUS = '0' AND B001.STATUS = '0' AND " + wherePart +
//                "            ORDER BY T1.ORDINAL ASC\n" +
                "            ) T\n" +
                "      WHERE ROWNUM <= '" + endIndex + "') T\n" +
                "WHERE RN > '" + startIndex + "'";

        String countSql = "SELECT count(*) as total\n" +
                " FROM EMP_A001 T1\n" +
                " INNER JOIN DEP_B001 B001 ON B001.DEP_ID = T1.A001004_A AND B001.TREE_LEVEL_CODE LIKE '" + code + "%'\n" +
                " WHERE T1.STATUS = '0' AND " + wherePart;


//        String sql = "select EMP_ID as \"empId\"," +
//                "       A01001 as \"name\"," +
//                "       (select name from SYS_CODE_ITEM where CODE_SET_ID = 18 and code = A01004) as \"gender\"," +
//                "       to_char(A01007, 'yyyy-mm-dd') as \"birthday\"," +
//                "       (select B01001 from DEP_B001 where DEP_ID = A001004_A) as \"organization\"," +
//                "       (select LISTAGG(A02016_A, '，') within group ( order by ID) from EMP_A02 where EMP_ID = t.EMP_ID and A02001_B = t.A001004_A group by t.EMP_ID) as \"position\"," +
//                "       (select LISTAGG(A05002_A, '，') within group ( order by ID) from EMP_A05 where EMP_ID = t.EMP_ID group by t.EMP_ID) as \"positionLevel\"" +
//                " from (select ROWNUM as rn, t.*" +
//                "      from (select t1.*, tmp.codeLen, tmp.code, tmp.sortNum" +
//                "            from EMP_A001 t1" +
//                "                     inner join (select a02.EMP_ID," +
//                "                                        min(length(nvl(org.code, '99999')))         as codeLen," +
//                "                                        nvl(min(nvl(org.code, '99999')), '99999')   as code," +
//                "                                        nvl(min(nvl(a02.A02023, '99999')), '99999') as sortNum" +
//                "                                 from EMP_A02 a02" +
//                "                                          inner join ORGANIZATION org on a02.A02001_B = org.ID" +
//                "                                 where a02.STATUS = '0'" +
//                "                                   and a02.A02055 = '2'" +
//                "                                   and org.CODE like (select code || '%' from ORGANIZATION where ID = '" + searchModel.getOrgId() + "')" +
//                "                                 group by a02.EMP_ID) tmp on t1.EMP_ID = tmp.EMP_ID" +
//                "            left join DEP_B001 b001 on b001.DEP_ID = t1.A001004_A  " +
//                "              where t1.STATUS = '0' and " +
//                wherePart +
//                "            order by tmp.codeLen, tmp.code, tmp.sortNum, t1.ORDINAL) t" +
//                "      where ROWNUM <= '" + endIndex + "') t" +
//                " where rn > '" + startIndex + "'";

//        String countSql = "select count(*) as total " +
//                "            from EMP_A001 t1" +
//                "                     inner join (select a02.EMP_ID" +
//                "                                 from EMP_A02 a02" +
//                "                                          inner join ORGANIZATION org on a02.A02001_B = org.ID" +
//                "                                 where a02.STATUS = '0'" +
//                "                                   and a02.A02055 = '2'" +
//                "                                   and org.CODE like (select code || '%' from ORGANIZATION where ID = '" + searchModel.getOrgId() + "')" +
//                "                                 group by a02.EMP_ID) tmp on t1.EMP_ID = tmp.EMP_ID" +
//                "            left join DEP_B001 b001 on b001.DEP_ID = t1.A001004_A  " +
//                "              where t1.STATUS = '0' and " +
//                wherePart;

        List<SysCheckEmpModel> models = jdbcTemplate.queryForList(sql).stream().map(map -> {
            SysCheckEmpModel model = new SysCheckEmpModel();
            model.setEmpId(map.get("empId").toString());
            model.setName(map.get("name") != null ? map.get("name").toString() : "");
            model.setBirthday(map.get("birthday") != null ? map.get("birthday").toString() : "");
            model.setGender(map.get("gender") != null ? map.get("gender").toString() : "");
            model.setPosition(map.get("position") != null ? map.get("position").toString() : "");
            model.setOrganization(map.get("organization") != null ? map.get("organization").toString() : "");
            model.setPositionLevel(map.get("positionLevel") != null ? map.get("positionLevel").toString() : "");
            model.setSysTableNameEn(condition.getSysTableNameEn());
            return model;
        }).collect(Collectors.toList());

        Long total = ((BigDecimal) jdbcTemplate.queryForMap(countSql).get("total")).longValue();

        return new PaginationViewModel.Builder<SysCheckEmpModel>()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .totalCount(total)
                .data(models)
                .build();
    }


    /**
     * 根据查错类型获取适配器
     */
    private SysCheckTypeAdapter getTypeAdapter(String typeId) throws Exception {
        for (SysCheckTypeAdapter typeAdapter : typeAdapters) {
            if (typeAdapter.support(typeId)) {
                return typeAdapter;
            }
        }
        throw new Exception("no adapter for type " + typeId);
    }

}
