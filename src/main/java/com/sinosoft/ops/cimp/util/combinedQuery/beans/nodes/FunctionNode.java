package com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes;

/**
 * 函数节点
 */
public class FunctionNode extends Node {
    public static final int CODE = 1 << 3;
    /**
     * 子节点支持的节点类型
     */
    public static final int supportNodes = FieldNode.CODE & FunctionNode.CODE & ValueNode.CODE;

    public FunctionNode(String expr) {
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
