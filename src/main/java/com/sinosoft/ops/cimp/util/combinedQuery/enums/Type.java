package com.sinosoft.ops.cimp.util.combinedQuery.enums;

public enum Type {
    NONE(0),
    BRACKETS(1),
    LOGICAL_OPERATOR(1 << 1),
    OPERATOR(1 << 2),
    STRING(1 << 3),
    NUMBER(1 << 4),
    DATE(1 << 5),
    LOB(1 << 6),
    CODE(1 << 7),
    ALL_FIELD_TYPE(STRING.code | NUMBER.code | DATE.code | LOB.code | CODE.code),
    ALL((1 << 30) - 1);

    private int code;

    Type(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
