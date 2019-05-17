package com.sinosoft.ops.cimp.constant;

public enum SysOpLogEnum {

    CREATE(10, "创建"),
    UPDATE(11, "修改"),
    DELETE(12, "删除");

    private Integer opType;

    private String opName;

    SysOpLogEnum(Integer opType, String opName) {
        this.opType = opType;
        this.opName = opName;
    }

    public Integer getOpType() {
        return opType;
    }

    public String getOpName() {
        return opName;
    }
}
