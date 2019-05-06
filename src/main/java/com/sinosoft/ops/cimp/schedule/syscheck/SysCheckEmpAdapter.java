package com.sinosoft.ops.cimp.schedule.syscheck;

import com.sinosoft.ops.cimp.entity.sys.check.SysCheckCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * 查错类型：干部
 */
@Component
public class SysCheckEmpAdapter implements SysCheckTypeAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SysCheckEmpAdapter.class);

    /**
     * 统计总数
     */
    @Override
    public void statisticsTotalNum(JdbcTemplate jdbcTemplate, String tableName) {
        String sql = "INSERT INTO " + tableName + "(Tree_Level_Code,Total,type) " +
                "WITH a AS(" +
                " SELECT dep_id, description, tree_level_code,pptr,LEVEL_NUM, root FROM DEP_LEVEL " +
                ")," +
                "b AS(SELECT A001004_A dep_id, count(*) pn FROM emp_a001 WHERE status = 0 GROUP BY A001004_A) " +
                " SELECT a.root, nvl(sum(b.pn), 0), 'A' FROM a LEFT JOIN b ON a.dep_id = b.dep_id " +
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
                "' from dep_b001 b001 left join emp_a001 t1 on t1.A001004_A = b001.dep_id " +
                " where b001.status=0 and t1.status = 0 and " + condition.getWherePart() +
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
     * 适配器是否支持该查错类型
     */
    @Override
    public boolean support(String typeId) {
        return "2".equals(typeId);
    }
}
