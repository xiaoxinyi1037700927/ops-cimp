package com.sinosoft.ops.cimp.util.combinedQuery.enums;

public enum LogicalOperator {
    AND("and", " %s AND %s "),
    OR("or", " %s OR %s ");

    private String name;
    private String sql;

    public String getName() {
        return name;
    }

    public String getSql() {
        return sql;
    }

    LogicalOperator(String name, String sql) {
        this.name = name;
        this.sql = sql;
    }

}
