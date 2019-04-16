package com.sinosoft.ops.cimp.dao.domain;

import java.io.Serializable;
import java.util.List;

public class ResultParam implements Serializable {

    private static final long serialVersionUID = -4636100627666881734L;
    /**
     * 一条结果记录
     * 一条记录时候，或返回结果属性只有一个。values 有值
     */
    private Object[] values;
    /**
     * 多条结果记录
     * 多条记录时候有值
     */
    private Object[][] valueLists;
    /**
     * 属性结果集
     */
    private List<String> attrs;
    /**
     * 结果类型，一条记录或多条记录
     */
    private ValueType valueType;

    /**
     * 结果类型，一条记录、多条记录
     */
    public enum ValueType {
        //一条记录
        //多条记录
        ONE,
        MORE
    }

    private String entityName;

    public ResultParam() {
    }

    public String getEntityName() {
        return entityName;
    }

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }

    public Object[][] getValueLists() {
        return valueLists;
    }

    public void setValueLists(Object[][] valueLists) {
        this.valueLists = valueLists;
    }

    public List<String> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<String> attrs) {
        this.attrs = attrs;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public void setValueType(ValueType valueType) {
        this.valueType = valueType;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }
}
