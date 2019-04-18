package com.sinosoft.ops.cimp.constant;

public enum SysCodeSetTypeEnum {
    NULL_TYPE(5,"空类型"),
    GENERAL_TYPE(0,"一般类型"),
    TREE_TYPE(1,"树类型");

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
