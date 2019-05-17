package com.sinosoft.ops.cimp.util.combinedQuery.beans;

import java.util.List;

public class Param {

    private String text;

    private int returnType;

    private int isFunction;

    private String functionName;

    private List<Param> params;

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
}
