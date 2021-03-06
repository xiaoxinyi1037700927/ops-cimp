package com.sinosoft.ops.cimp.schedule.syscheck;

import com.sinosoft.ops.cimp.entity.sys.check.SysCheckCondition;
import com.sinosoft.ops.cimp.vo.to.sys.check.SysCheckTreeNode;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public interface SysCheckTypeAdapter {

    /**
     * 统计总数
     */
    void statisticsTotalNum(JdbcTemplate jdbcTemplate, String tableName);

    /**
     * 统计错误数
     */
    void statisticsWrongNum(JdbcTemplate jdbcTemplate, SysCheckCondition condition);

    /**
     * 统计错误总数
     */
    void statisticsTotalWrongNum(JdbcTemplate jdbcTemplate, SysCheckCondition condition, String tableName);

    /**
     * 获取总数
     */
    Integer getTotalNum(JdbcTemplate jdbcTemplate, String tableName, String treeLevelCode);

    /**
     * 获取错误数
     */
    Integer getWrongNum(JdbcTemplate jdbcTemplate, String tableName, String treeLevelCode, String conditionId);

    /**
     * 获取查错单位树
     */
    List<SysCheckTreeNode> getTreeNodes(JdbcTemplate jdbcTemplate, String tableName, String conditionId, String sqlWhere);

    /**
     * 适配器是否支持该查错类型
     */
    boolean support(String typeId);
}
