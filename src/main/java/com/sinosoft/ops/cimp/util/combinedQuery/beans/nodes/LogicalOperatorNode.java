package com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes;

/**
 * 逻辑操作符节点(and/or)
 */
public class LogicalOperatorNode extends Node {
    public static final int CODE = 1 << 1;
    /**
     * 子节点支持的节点类型
     */
    public static final int supportNodes = LogicalOperatorNode.CODE & ExpressionNode.CODE & BracketsNode.CODE;


    public LogicalOperatorNode(String expr) {
        super(expr);
    }

    /**
     * 获取节点对应的sql
     *
     * @return
     */
    @Override
    public String getSql() {
        return null;
    }


    @Override
    public int getCode() {
        return CODE;
    }
}
