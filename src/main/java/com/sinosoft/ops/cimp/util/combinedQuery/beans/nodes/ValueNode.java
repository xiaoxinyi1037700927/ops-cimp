package com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes;


/**
 * 值节点
 */
public class ValueNode extends Node {
    public static final int CODE = 1 << 5;
    public static final int SUPPORT_NODES = Node.NONE;
    private static final String SQL = " %s ";

    private String value;
    private boolean isCode;

    public ValueNode(String expr, String value, boolean isCode) {
        super(expr, !isCode, 0, SUPPORT_NODES, CODE);
        this.value = value;
        this.isCode = isCode;
    }

    /**
     * 获取节点对应的sql
     */
    @Override
    public String getSql() {
        return String.format(SQL, value);
    }

}
