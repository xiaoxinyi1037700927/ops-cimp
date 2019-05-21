package com.sinosoft.ops.cimp.util.combinedQuery.enums;

public enum LogicalOperator {
    AND("并且", "^and|并且$", "%s AND %s", "%s 并且 %s"),
    OR("或者", "^or|或者$", "%s OR %s", "%s 或者 %s");

    /**
     * 逻辑操作符名称
     */
    private String name;
    /**
     * 解析的正则表达式
     */
    private String regex;
    /**
     * sql的格式
     */
    private String sqlFormat;
    /**
     * 表达式的格式
     */
    private String exprFormat;


    LogicalOperator(String name, String regex, String sqlFormat, String exprFormat) {
        this.name = name;
        this.regex = regex;
        this.sqlFormat = sqlFormat;
        this.exprFormat = exprFormat;
    }

    public String getName() {
        return name;
    }

    public String getRegex() {
        return regex;
    }

    public String getSqlFormat() {
        return sqlFormat;
    }

    public String getExprFormat() {
        return exprFormat;
    }


}
