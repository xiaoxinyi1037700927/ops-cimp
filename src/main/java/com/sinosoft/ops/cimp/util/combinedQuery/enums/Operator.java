package com.sinosoft.ops.cimp.util.combinedQuery.enums;

public enum Operator {
    EQ("等于", 2, 1, " %s = %s "),
    NQ("不等于", 2, 1 << 1, " %s != %s "),
    GT("大于", 2, 1 << 2, " %s > %s "),
    LT("小于", 2, 1 << 3, " %s < %s "),
    GE("大于等于", 2, 1 << 4, " %s >= %s "),
    LE("小于等于", 2, 1 << 5, " %s <= %s "),
    IN("^在\\[(.+?)\\]之中$", 2, 1 << 6, " %s IN (%s) "),
    IS_NULL("为空", 1, 1 << 7, " %s IS NULL "),
    NOT_NULL("不为空", 1, 1 << 8, " %s IS NOT NULL "),
    CONTAINS("包含", 2, 1 << 9, " %s LIKE '%%%s%%' "),
    BETWEEN_AND("^介于(.+?)和(.+?)之间$", 3, 1 << 10, " %s BETWEEN %s AND %s ");

    private String name;
    private int subNodeNum;
    private int code;
    private String sql;

    Operator(String name, int subNodeNum, int code, String sql) {
        this.name = name;
        this.subNodeNum = subNodeNum;
        this.code = code;
        this.sql = sql;
    }

    public String getName() {
        return name;
    }

    public int getSubNodeNum() {
        return subNodeNum;
    }

    public int getCode() {
        return code;
    }

    public String getSql() {
        return sql;
    }
}
