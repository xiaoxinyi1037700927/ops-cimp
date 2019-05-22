package com.sinosoft.ops.cimp.util.combinedQuery.beans;

import java.util.List;

public class Expression {

    private String logicalOperator;

    private String text;

    private Param param1;

    private String operator;

    private Param param2;

    private Param param3;

    private boolean isBracketsNode;

    private List<Expression> subExprs;

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

    public Param getParam1() {
        return param1;
    }

    public void setParam1(Param param1) {
        this.param1 = param1;
    }

    public Param getParam2() {
        return param2;
    }

    public void setParam2(Param param2) {
        this.param2 = param2;
    }

    public Param getParam3() {
        return param3;
    }

    public void setParam3(Param param3) {
        this.param3 = param3;
    }

    public boolean isBracketsNode() {
        return isBracketsNode;
    }

    public void setBracketsNode(boolean bracketsNode) {
        isBracketsNode = bracketsNode;
    }

    public List<Expression> getSubExprs() {
        return subExprs;
    }

    public void setSubExprs(List<Expression> subExprs) {
        this.subExprs = subExprs;
    }
}
