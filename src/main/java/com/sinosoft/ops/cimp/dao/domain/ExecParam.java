package com.sinosoft.ops.cimp.dao.domain;

import java.io.Serializable;

public class ExecParam implements Serializable {
    private static final long serialVersionUID = -3059615197770042598L;
    /**
     * 新增，修改时候。name 为业务属性名，value为具体值
     * 查询时候，name为查询结果显示属性 value为空
     */
    private String fieldName;
    private Object fieldValue;

    public ExecParam(String fieldName, Object fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public ExecParam() {

    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(Object fieldValue) {
        this.fieldValue = fieldValue;
    }
}
