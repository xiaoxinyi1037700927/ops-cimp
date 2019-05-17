package com.sinosoft.ops.cimp.util.combinedQuery.enums;

public enum LogicalOperator {
    AND("并且", "^and|并且$", " %s AND %s ", " %s 并且 %s "),
    OR("或者", "^or|或者$", " %s OR %s ", " %s 或者 %s ");

    private String name;
    private String regex;
    private String sql;
    private String exprFormat;


    public String getName() {
        return name;
    }

    public String getRegex() {
        return regex;
    }

    public String getSql() {
        return sql;
    }

    public String getExprFormat() {
        return exprFormat;
    }

    LogicalOperator(String name, String regex, String sql, String exprFormat) {
        this.name = name;
        this.regex = regex;
        this.sql = sql;
        this.exprFormat = exprFormat;
    }

}
