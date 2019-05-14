package com.sinosoft.ops.cimp.util.combinedQuery.beans;

import com.sinosoft.ops.cimp.util.combinedQuery.beans.expressions.EqExpr;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.expressions.NqExpr;

public enum FieldType {
    STRING("string",
            EqExpr.CODE & NqExpr.CODE),
    NUMBER("number",
            EqExpr.CODE & NqExpr.CODE),
    DATE("date",
            EqExpr.CODE & NqExpr.CODE),
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
