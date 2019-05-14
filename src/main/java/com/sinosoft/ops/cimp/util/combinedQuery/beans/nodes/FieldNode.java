package com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes;


/**
 * 字段节点
 */
public class FieldNode extends Node {
    public static final int CODE = 1 << 4;
    /**
     * 子节点支持的节点类型
     */
    public static final int supportNodes = Node.NONE;

    public FieldNode(String expr) {
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

