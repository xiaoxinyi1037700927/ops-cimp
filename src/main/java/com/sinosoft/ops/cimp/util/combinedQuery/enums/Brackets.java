package com.sinosoft.ops.cimp.util.combinedQuery.enums;

public enum Brackets {
    LEFT("(", "("),
    RIGHT(")", ")");

    private String name;
    private String sql;

    Brackets(String name, String sql) {
        this.name = name;
        this.sql = sql;
    }

    public String getName() {
        return name;
    }

    public String getSql() {
        return sql;
    }
}
