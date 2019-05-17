package com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes;

import com.sinosoft.ops.cimp.util.combinedQuery.beans.CombinedQueryParseException;

import java.util.ArrayList;
import java.util.List;

/**
 * 语法树节点
 */
public abstract class Node {
    public static final int NONE = 0;

    public Node(String expr, boolean isComplete, int subNodesNum, int supportSubNodes, int code) {
        this.expr = expr;
        this.isComplete = isComplete;
        this.subNodesNum = subNodesNum;
        this.supportSubNodes = supportSubNodes;
        this.code = code;
    }

    /**
     * 当前节点是否完整
     */
    private boolean isComplete;
    /**
     * 当前节点对应的表达式
     */
    private String expr;
    /**
     * 当前节点的code
     */
    private int code;
    /**
     * 当前节点需要的子节点数量
     */
    private int subNodesNum;
    /**
     * 当前节点支持的子节点类型
     */
    private int supportSubNodes;
    /**
     * 子节点对应的表达式
     */
    private List<String> subNodeExpr = new ArrayList<>();
    /**
     * 父节点
     */
    protected Node parent;
    /**
     * 子节点
     */
    protected List<Node> subNodes = new ArrayList<>();

    public boolean isComplete() {
        return isComplete;
    }

    public String getExpr() {
        return expr;
    }

    public int getSubNodesNum() {
        return subNodesNum;
    }

    public int getSupportSubNodes() {
        return supportSubNodes;
    }

    public int getCode() {
        return code;
    }

    public List<String> getSubNodeExpr() {
        return subNodeExpr;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Node> getSubNodes() {
        return subNodes;
    }

    /**
     * 添加子节点表达式
     *
     * @param expr
     */
    public void addSubNodeExpr(String expr) {
        subNodeExpr.add(expr);
    }

    /**
     * 添加子节点表达式
     *
     * @param exprs
     */
    public void addSubNodeExpr(List<String> exprs) {
        subNodeExpr.addAll(exprs);
    }

    /**
     * 添加子节点
     *
     * @param node
     * @throws CombinedQueryParseException
     */
    public void addSubNode(Node node) throws CombinedQueryParseException {
        if (isComplete) {
            throw new CombinedQueryParseException("添加子节点失败！");
        }
        subNodes.add(node);

        if (subNodes.size() == subNodesNum) {
            isComplete = true;
        }
    }


    /**
     * 获取节点对应的sql
     */
    public abstract String getSql();


}
