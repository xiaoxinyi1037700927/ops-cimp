package com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes;

import com.sinosoft.ops.cimp.util.combinedQuery.processors.functions.FunctionProcessor;

/**
 * 函数节点
 */
public class FunctionNode extends Node {
    public static final int CODE = 1 << 3;
    public static final int SUPPORT_NODES = FieldNode.CODE | FunctionNode.CODE | ValueNode.CODE;

    private FunctionProcessor processor;

    public FunctionNode(String expr, FunctionProcessor processor) {
        super(expr, processor.getFunction().getParamsNum() == 0, processor.getFunction().getParamsNum(), SUPPORT_NODES, CODE);
        this.processor = processor;
    }

    /**
     * 获取节点对应的sql
     */
    @Override
    public String getSql() {
        return processor.getSql(subNodes);
    }

}
