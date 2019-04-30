package com.sinosoft.ops.cimp.vo.to.organization;

import java.io.Serializable;

public class DepFieldVO implements Serializable {
    private static final long serialVersionUID = 3645742331089893088L;

    //字段英文名
    private Object fieldNameEn;
    //字段中文名
    private Object fieldNameCn;
    //字段值
    private Object fieldValue;

    public Object getFieldNameEn() {
        return fieldNameEn;
    }

    public void setFieldNameEn(Object fieldNameEn) {
        this.fieldNameEn = fieldNameEn;
    }

    public Object getFieldNameCn() {
        return fieldNameCn;
    }

    public void setFieldNameCn(Object fieldNameCn) {
        this.fieldNameCn = fieldNameCn;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(Object fieldValue) {
        this.fieldValue = fieldValue;
    }
}
