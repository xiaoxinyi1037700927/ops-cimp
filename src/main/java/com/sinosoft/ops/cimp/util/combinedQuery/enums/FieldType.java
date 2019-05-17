package com.sinosoft.ops.cimp.util.combinedQuery.enums;

public enum FieldType {
    STRING("string",
            Operator.EQ.getCode() & Operator.NQ.getCode()),
    NUMBER("number",
            Operator.EQ.getCode() & Operator.NQ.getCode()),
    DATE("date",
            Operator.EQ.getCode() & Operator.NQ.getCode()),
    BLOB("blob", 0);

    private String code;
    /**
     * 定义字段类型支持的操作符
     */
    private int ops;

    FieldType(String code, int ops) {
        this.code = code;
        this.ops = ops;
    }

    public String getCode() {
        return code;
    }

    public int getOps() {
        return ops;
    }

    public static FieldType getByCode(String code) throws Exception {
        for (FieldType type : FieldType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new Exception("can not found field type!");
    }
}
