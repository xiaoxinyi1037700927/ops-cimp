package com.sinosoft.ops.cimp.util.combinedQuery.enums;

public enum Brackets {
    LEFT("("),
    RIGHT(")");

    private String name;

    Brackets(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
