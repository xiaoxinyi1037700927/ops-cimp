package com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes;

import com.sinosoft.ops.cimp.util.combinedQuery.beans.CombinedQueryParseException;
import com.sinosoft.ops.cimp.util.combinedQuery.enums.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * 语法树节点
 */
public abstract class Node {
    public Node(String expr, boolean isComplete, int[] supportSubTypes) {
        this.expr = expr;
        this.isComplete = isComplete;
        this.supportSubTypes = supportSubTypes;
    }

    /**
     * 当前节点是否完整
     */
    private boolean isComplete;
    /**
     * 当前节点对应的表达式
     */
    protected String expr;
    /**
     * 当前节点支持的子节点类型
     */
    private int[] supportSubTypes;
    /**
     * 子节点对应的表达式
     */
    private List<String> subNodeExpr = new ArrayList<>();
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

    public int[] getSupportSubTypes() {
        return supportSubTypes;
    }

    public int getNextSubType() {
        if (supportSubTypes.length > subNodes.size()) {
            return supportSubTypes[subNodes.size()];
        } else {
            return Type.NONE.getCode();
        }
    }

    public List<String> getSubNodeExpr() {
        return subNodeExpr;
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
            throw new CombinedQueryParseException("非法的表达式：" + node.expr);
        }

        //判断下一个子节点是否支持添加的节点的返回类型
        int subType = supportSubTypes[subNodes.size()];
        if ((subType & node.getReturnType()) == 0) {
            throw new CombinedQueryParseException("错误的子节点类型 : " + node.expr);
        }

        subNodes.add(node);

        //判断节点是否完整
        if (subNodes.size() == supportSubTypes.length) {
            isComplete = true;
        }
    }

    /**
     * 获取节点的返回类型
     *
     * @return
     */
    public abstract int getReturnType();


    /**
     * 获取节点对应的sql
     */
    public abstract String getSql();


}
