package com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes;

/**
 * 语法树节点
 */
public abstract class Node {
    public static final int NONE = 0;

    public Node(String exprStr) {
        this.exprStr = exprStr;
    }

    /**
     * 当前节点对应的表达式
     */
    protected String exprStr;
    /**
     * 子节点对应的表达式
     */
    protected String[] subExprStr;
    /**
     * 父节点
     */
    protected Node parent;
    /**
     * 子节点
     */
    protected Node[] subNodes;

    public String getExprStr() {
        return exprStr;
    }

    public void setExprStr(String exprStr) {
        this.exprStr = exprStr;
    }

    public String[] getSubExprStr() {
        return subExprStr;
    }

    public void setSubExprStr(String[] subExprStr) {
        this.subExprStr = subExprStr;
    }

    public Node[] getSubNodes() {
        return subNodes;
    }

    public void setSubNodes(Node[] subNodes) {
        this.subNodes = subNodes;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getCode() {
        return NONE;
    }

    /**
     * 获取节点对应的sql
     */
    public abstract String getSql();

}
