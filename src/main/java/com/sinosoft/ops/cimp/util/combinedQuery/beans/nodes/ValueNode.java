package com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes;


/**
 * 值节点
 */
public class ValueNode extends Node {
    public static final int CODE = 1 << 5;
    /**
     * 子节点支持的节点类型
     */
    public static final int supportNodes = Node.NONE;

    public ValueNode(String expr) {
        super(expr);
    }

    /**
     * 获取节点对应的sql
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
