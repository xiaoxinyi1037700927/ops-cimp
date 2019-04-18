package com.sinosoft.ops.cimp.constant;


public enum TableFieldLogicalDeleteFlagEnum {
    有效("0","有效"),
    删除("1","删除");

    public String code;
    public String value;

    TableFieldLogicalDeleteFlagEnum(String code, String value) {
        this.code  = code;
        this.value = value;
    }

    public String getCode() { return code; }

    public String getValue() {
        return value;
    }
}

