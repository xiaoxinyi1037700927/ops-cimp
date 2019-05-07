package com.sinosoft.ops.cimp.schedule.syscheck;

import com.sinosoft.ops.cimp.entity.sys.check.SysCheckCondition;
import com.sinosoft.ops.cimp.vo.to.sys.check.SysCheckTreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 查错类型：干部
 */
@Component
public class SysCheckEmpAdapter implements SysCheckTypeAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SysCheckEmpAdapter.class);

    private static final String TYPE_ID = "2";

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
                " SELECT a.root, nvl(sum(b.pn), 0), '" + TYPE_ID + "' FROM a LEFT JOIN b ON a.dep_id = b.dep_id " +
                " GROUP BY root";

        jdbcTemplate.update(sql);
    }

    /**
     * 统计错误数
     */
    @Override
    public void statisticsWrongNum(JdbcTemplate jdbcTemplate, SysCheckCondition condition) {
        String sql = "insert into RESULT_WRONG_NUMBER(org_id, tree_level_code, pptr, num, Type, check_condition_id) " +
                " select b001.dep_id,max(b001.B001001_A),max(b001.pptr), count(1), '" + TYPE_ID + "', '" + condition.getId() +
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
                " select dl.root, nvl(sum(rwn.num), 0), '" + TYPE_ID + "', '" + condition.getId() +
                "' from RESULT_WRONG_NUMBER rwn " +
                " inner join DEP_LEVEL dl on dl.DEP_ID = rwn.org_id " +
                " where rwn.Type = '" + TYPE_ID + "' and rwn.check_condition_id = '" + condition.getId() + "'" +
                " group by dl.root ";

        jdbcTemplate.update(sql);
    }

    /**
     * 获取总数
     */
    @Override
    public Integer getTotalNum(JdbcTemplate jdbcTemplate, String tableName, String treeLevelCode) {
        String sql = "select total from " + tableName +
                " where type = '" + TYPE_ID +
                "' and tree_level_code = '" + treeLevelCode + "'";

        List<Map<String, Object>> map = jdbcTemplate.queryForList(sql);
        return map.size() > 0 ? ((BigDecimal) map.get(0).get("total")).intValue() : 0;
    }

    /**
     * 获取错误数
     */
    @Override
    public Integer getWrongNum(JdbcTemplate jdbcTemplate, String tableName, String treeLevelCode, String conditionId) {
        String sql = "select nvl(num,0) as num from " + tableName +
                " where type = '" + TYPE_ID
                + "' and tree_level_code = '" + treeLevelCode +
                "' and check_condition_id = '" + conditionId + "'";

        List<Map<String, Object>> map = jdbcTemplate.queryForList(sql);
        return map.size() > 0 ? ((BigDecimal) map.get(0).get("num")).intValue() : 0;
    }

    /**
     * 获取查错单位树
     */
    @Override
    public List<SysCheckTreeNode> getTreeNodes(JdbcTemplate jdbcTemplate, String tableName, String conditionId, String sqlWhere) {
        String sql = "select nvl(rt.num, 0) num, b001.dep_id id, b001.B01001 name, " +
                " nvl((select count(1) countNum from dep_b001 where pptr = b001.tree_level_code),0) childNum " +
                " from dep_b001 b001 " +
                " left join " +
                tableName +
                " rt " +
                "  on b001.tree_level_code = rt.tree_level_code and rt.type='" +
                TYPE_ID +
                "' and rt.CHECK_CONDITION_ID = '" +
                conditionId +
                "' where b001.B01001 is not null and b001.status = 0 and b001.B001001_A in( " +
                sqlWhere +
                " ) " +
                " order by b001.tree_level_code asc";

        return jdbcTemplate.queryForList(sql)
                .stream().map(map -> {
                    SysCheckTreeNode node = new SysCheckTreeNode();
                    node.setName(map.get("name").toString());
                    node.setWrongNum(map.get("num").toString());
                    node.setOrgId(map.get("id").toString());
                    node.setHasChildren(Integer.parseInt(map.get("childNum").toString()) > 0);
                    return node;
                }).collect(Collectors.toList());
    }

    /**
     * 适配器是否支持该查错类型
     */
    @Override
    public boolean support(String typeId) {
        return TYPE_ID.equals(typeId);
    }
}
