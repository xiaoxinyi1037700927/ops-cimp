package com.sinosoft.ops.cimp.util.combinedQuery.enums;

public enum LogicalOperator {
    AND("^and|并且$", " %s AND %s "),
    OR("^or|或者$", " %s OR %s ");

    private String regex;
    private String sql;

    public String getRegex() {
        return regex;
    }

    public String getSql() {
        return sql;
    }

    LogicalOperator(String regex, String sql) {
        this.regex = regex;
        this.sql = sql;
    }

}
