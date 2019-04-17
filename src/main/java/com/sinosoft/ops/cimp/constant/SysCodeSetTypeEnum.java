package com.sinosoft.ops.cimp.constant;

public enum SysCodeSetTypeEnum {
    GeneralType(0,"一般类型"),
    TreeType(1,"树类型");

    private Integer type;

    private String typeName;

    SysCodeSetTypeEnum(Integer type, String typeName) {
        this.type = type;
        this.typeName = typeName;
    }

    public Integer getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }
}
