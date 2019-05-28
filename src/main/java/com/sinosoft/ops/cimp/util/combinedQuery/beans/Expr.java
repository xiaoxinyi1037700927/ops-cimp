package com.sinosoft.ops.cimp.util.combinedQuery.beans;

import java.util.List;

public class Expr {

    private String id;

    private String logicalOperator;

    private String text;

    private List<Param> params;

    private String operator;

    private boolean isBracketsNode;

    private List<Expr> subExprs;

    private boolean compilePass = true;

    private String wrongMessage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogicalOperator() {
        return logicalOperator;
    }

    public void setLogicalOperator(String logicalOperator) {
        this.logicalOperator = logicalOperator;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }

    public boolean isBracketsNode() {
        return isBracketsNode;
    }

    public void setBracketsNode(boolean bracketsNode) {
        isBracketsNode = bracketsNode;
    }

    public List<Expr> getSubExprs() {
        return subExprs;
    }

    public void setSubExprs(List<Expr> subExprs) {
        this.subExprs = subExprs;
    }

    public boolean isCompilePass() {
        return compilePass;
    }

    public void setCompilePass(boolean compilePass) {
        this.compilePass = compilePass;
    }

    public String getWrongMessage() {
        return wrongMessage;
    }

    public void setWrongMessage(String wrongMessage) {
        this.wrongMessage = wrongMessage;
    }
}
