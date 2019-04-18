package com.sinosoft.ops.cimp.constant;

public enum DeleteTypeEnum {
    逻辑删除("0","逻辑删除"),
    物理删除("1","物理删除");

    public String code;
    public String value;

    DeleteTypeEnum(String code, String value) {
        this.code  = code;
        this.value = value;
    }

    public String getCode() { return code; }

    public String getValue() {
        return value;
    }
}
