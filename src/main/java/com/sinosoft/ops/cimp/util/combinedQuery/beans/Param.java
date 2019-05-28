package com.sinosoft.ops.cimp.util.combinedQuery.beans;

import java.util.List;

public class Param {

    public enum Type {
        FUNCTION("function"),
        FIELD("field"),
        CODE("code"),
        VALUE("value");

        private String name;

        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public Param() {

    }

    public Param(String id, String text) {
        this.id = id;
        this.text = text;
    }

    private String id;

    private String text;

    private int returnType;

    private int isFunction;

    private String functionName;

    private List<Param> params;

    private String tableId;

    private String fieldId;

    private String type = Type.VALUE.getName();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getReturnType() {
        return returnType;
    }

    public void setReturnType(int returnType) {
        this.returnType = returnType;
    }

    public int getIsFunction() {
        return isFunction;
    }

    public void setIsFunction(int isFunction) {
        this.isFunction = isFunction;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
