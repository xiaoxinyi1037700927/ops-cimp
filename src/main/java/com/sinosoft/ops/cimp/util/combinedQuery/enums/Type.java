package com.sinosoft.ops.cimp.util.combinedQuery.enums;

public enum Type {
    BRACKETS(1),
    lOGICAL_OPERATOR(1 << 1),
    OPERATOR(1 << 2),
    ARRAY(1 << 3),
    STRING(1 << 4),
    NUMBER(1 << 5),
    DATE(1 << 6),
    LOB(1 << 7),
    NONE(0),
    ALL_FIELD_TYPE(STRING.code | NUMBER.code | DATE.code | LOB.code),
    ALL((1 << 30) - 1);

    private int code;

    Type(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
