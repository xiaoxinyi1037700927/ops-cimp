package com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes;


import com.sinosoft.ops.cimp.util.combinedQuery.enums.Type;

/**
 * 值节点
 */
public class ValueNode extends Node {
    public static final int[] SUPPORT_SUB_TYPES = new int[]{};
    private static final String SQL_FORMAT = " %s ";

    private String value;
    private boolean isCode;

    public ValueNode(String expr, String value, boolean isCode) {
        super(expr, !isCode, SUPPORT_SUB_TYPES);
        this.value = value;
        this.isCode = isCode;
    }

    /**
     * 获取节点的返回类型
     *
     * @return
     */
    @Override
    public int getReturnType() {
        return Type.STRING.getCode() | Type.NUMBER.getCode();
    }

    /**
     * 获取节点对应的sql
     */
    @Override
    public String getSql() {
        return String.format(SQL_FORMAT, value);
    }

    /**
     * 获取节点对应的表达式
     */
    @Override
    public String getExpr() {
        return expr.replaceAll("\"", "'");
    }

}
