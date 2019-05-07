package com.sinosoft.ops.cimp.schedule.syscheck;

import com.sinosoft.ops.cimp.entity.sys.check.SysCheckCondition;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 查错类型：单位
 */
@Deprecated
//@Component
public class SysCheckDepAdapter implements SysCheckTypeAdapter {

    private static final String TYPE_ID = "1";

    /**
     * 统计总数
     */
    @Override
    public void statisticsTotalNum(JdbcTemplate jdbcTemplate, String tableName) {
        String sql = "INSERT INTO " + tableName + "(Tree_Level_Code,Total,type) " +
                "WITH a AS(" +
                " SELECT dep_id, description, tree_level_code,pptr,LEVEL_NUM, root FROM DEP_LEVEL " +
                ")," +
                "b AS(SELECT dep_id, count(1) pn FROM dep_b001 WHERE status = 0 GROUP BY dep_id)" +
                " SELECT a.root, sum(nvl(b.pn, 0)), 'B' FROM a LEFT JOIN b ON a.dep_id = b.dep_id " +
                " GROUP BY root";

        jdbcTemplate.update(sql);
    }

    /**
     * 统计错误数
     */
    @Override
    public void statisticsWrongNum(JdbcTemplate jdbcTemplate, SysCheckCondition condition) {
        String sql = "insert into RESULT_WRONG_NUMBER(org_id, tree_level_code, pptr, num, Type, check_condition_id) " +
                " select b001.dep_id,max(b001.B001001_A),max(b001.pptr), count(1), '" + condition.getTypeId() + "', '" + condition.getId() +
                "' from dep_b001 b001 " +
                " where b001.status=0 and " + condition.getWherePart() +
                " group by b001.dep_id  ";

        jdbcTemplate.update(sql);
    }

    /**
     * 统计错误总数
     */
    @Override
    public void statisticsTotalWrongNum(JdbcTemplate jdbcTemplate, SysCheckCondition condition, String tableName) {
        String sql = " insert into " + tableName + "(Tree_Level_Code,num,Type,check_condition_id) " +
                " select dl.root, nvl(sum(rwn.num), 0), '" + condition.getTypeId() + "', '" + condition.getId() +
                "' from RESULT_WRONG_NUMBER rwn " +
                " inner join DEP_LEVEL dl on dl.DEP_ID = rwn.org_id " +
                " where rwn.Type = '" + condition.getTypeId() + "' and rwn.check_condition_id = '" + condition.getId() + "'" +
                " group by dl.root ";

        jdbcTemplate.update(sql);
    }

    /**
     * 获取总数
     */
    @Override
    public Integer getTotalNum(JdbcTemplate jdbcTemplate, String tableName, String treeLevelCode) {
        return 0;
    }

    /**
     * 获取错误数
     */
    @Override
    public Integer getWrongNum(JdbcTemplate jdbcTemplate, String tableName, String treeLevelCode, String conditionId) {
        return 0;
    }

    /**
     * 适配器是否支持该查错类型
     */
    @Override
    public boolean support(String typeId) {
        return TYPE_ID.equals(typeId);
    }
}
