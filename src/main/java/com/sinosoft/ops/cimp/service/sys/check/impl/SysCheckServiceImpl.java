package com.sinosoft.ops.cimp.service.sys.check.impl;

import com.sinosoft.ops.cimp.entity.sys.check.QSysCheckItem;
import com.sinosoft.ops.cimp.entity.sys.check.SysCheckItem;
import com.sinosoft.ops.cimp.repository.sys.check.SysCheckConditionRepository;
import com.sinosoft.ops.cimp.repository.sys.check.SysCheckItemRepository;
import com.sinosoft.ops.cimp.schedule.beans.SysCheckResultTables;
import com.sinosoft.ops.cimp.schedule.tasks.DataStatisticsTask;
import com.sinosoft.ops.cimp.service.sys.check.SysCheckService;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.vo.from.sys.check.SysCheckQueryDataModel;
import com.sinosoft.ops.cimp.vo.to.sys.check.SysCheckConditionModel;
import com.sinosoft.ops.cimp.vo.to.sys.check.SysCheckStatisticsData;
import com.sinosoft.ops.cimp.vo.to.sys.check.SysCheckStatisticsDataItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysCheckServiceImpl implements SysCheckService {

    private final SysCheckConditionRepository sysCheckConditionRepository;

    private final SysCheckItemRepository sysCheckItemRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SysCheckServiceImpl(SysCheckConditionRepository sysCheckConditionRepository, SysCheckItemRepository sysCheckItemRepository, JdbcTemplate jdbcTemplate) {
        this.sysCheckConditionRepository = sysCheckConditionRepository;
        this.sysCheckItemRepository = sysCheckItemRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 系统查错条件列表
     */
    @Override
    public List<SysCheckConditionModel> listSysCheckCondition() {
        return sysCheckConditionRepository.findAll(new Sort(Sort.Direction.ASC, "sort"))
                .stream().map(condition -> {
                    SysCheckConditionModel model = new SysCheckConditionModel();
                    model.setId(condition.getId());
                    model.setName(condition.getName());

                    return model;
                }).collect(Collectors.toList());
    }

    /**
     * 获取指定查错条件的分组统计数据
     */
    @Override
    public List<SysCheckStatisticsData> queryData(SysCheckQueryDataModel queryModel) {
        QSysCheckItem qCheckItem = QSysCheckItem.sysCheckItem;
        List<SysCheckStatisticsData> resultList = new ArrayList<>();

        List<SysCheckItem> sysCheckItems = sysCheckItemRepository.findBySysCheckConditionId(queryModel.getSysCheckConditionId());

        //如果请求参数中单位id为空，根据当前用户的数据权限查询，否则，查询其下级单位
        String depCodeSqlWhere = " select B001001_A from dep_b001 where ";
        if (StringUtils.isNotEmpty(queryModel.getOrgId())) {
            depCodeSqlWhere += "pptr in (select B001001_A from dep_b001 where dep_id ='" + queryModel.getOrgId() + "') ";
        } else {
            //当前用户的数据权限
            String dataOrgId = SecurityUtils.getSubject().getCurrentUser().getDataOrganizationId();
//            String dataOrgId = "DC5C7986DAEF50C1E02AB09B442EE34F";
            depCodeSqlWhere += "DEP_ID ='" + dataOrgId + "'";
        }

        String type = null;
        for (SysCheckItem sysCheckItem : sysCheckItems) {
            type = sysCheckItem.getType();

            SysCheckStatisticsData data = new SysCheckStatisticsData();
            data.setTitle("下级各单位数据采集情况");
            data.setType(type);

            //只处理类型为A B 的
            if (!"A".equalsIgnoreCase(type) && !"B".equalsIgnoreCase(type)) {
                continue;
            }

            data.setItems(jdbcTemplate.queryForList(getQuerySqlAB(depCodeSqlWhere, sysCheckItem.getId(), type))
                    .stream().map(map -> {
                        SysCheckStatisticsDataItem item = new SysCheckStatisticsDataItem();
                        item.setName(map.get("name").toString());
                        item.setNum(map.get("num").toString());
                        item.setOrgId(map.get("id").toString());
                        item.setTotal(map.get("total").toString());
                        item.setHasChildren(Integer.parseInt(map.get("childNum").toString()) > 0);

                        return item;
                    }).collect(Collectors.toList()));
            resultList.add(data);
        }

        return resultList;
    }

    private String getQuerySqlAB(String depCodeSqlWhere, String checkConditionId, String type) {
        //获取结果集的表名
        String id = jdbcTemplate.queryForMap("SELECT RESULT_TABLES FROM SYS_CHECK_RESULT_TABLES WHERE ROWNUM = 1").get("RESULT_TABLES").toString();
        SysCheckResultTables resultTables = SysCheckResultTables.getAnotherTables(id);
        if (resultTables == null) {
            return "";
        }

        StringBuilder sql = new StringBuilder();
        sql.append("select r.total total, nvl(rt.num, 0) num, db.dep_id id, db.B01001 name, ");
        sql.append(" nvl((select count(1) countNum from dep_b001 where pptr = r.tree_level_code),0) childNum ");
        sql.append(" from ");
        sql.append(resultTables.getResultsName());
        sql.append(" r ");
        sql.append(" inner join dep_b001 db on db.B001001_A = r.tree_level_code ");
        sql.append(" left join ");
        sql.append(resultTables.getResultsTempName());
        sql.append(" rt ");
        sql.append("  on r.tree_level_code = rt.tree_level_code and rt.type='");
        sql.append(type);
        sql.append("' and rt.CHECK_CONDITION_ID = '");
        sql.append(checkConditionId);
        sql.append("' where r.type='");
        sql.append(type);
        sql.append("' and db.B01001 is not null and db.status = 0 ");
        sql.append(" and db.B001001_A in ");
        sql.append(" (");
        if ("A".equalsIgnoreCase(type)) {
            sql.append(depCodeSqlWhere);
        } else if ("B".equalsIgnoreCase(type)) {
            sql.append(" select B001001_A from dep_b001 where pptr in (");
            sql.append(depCodeSqlWhere);
            sql.append(") union all ");
            sql.append(" select B001001_A from dep_b001 where B001001_A in (");
            sql.append(depCodeSqlWhere);
            sql.append(") ");
        }
        sql.append(" ) ");
        sql.append(" order by r.tree_level_code asc");

        return sql.toString();
    }
}
