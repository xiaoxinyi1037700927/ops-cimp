package com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes;

import com.sinosoft.ops.cimp.util.combinedQuery.processors.functions.FunctionProcessor;

/**
 * 函数节点
 */
public class FunctionNode extends Node {
    private FunctionProcessor processor;

    public FunctionNode(String expr, FunctionProcessor processor) {
        super(expr, processor.getFunction().getParamsNum() == 0, processor.getFunction().getParamsType());
        this.processor = processor;
    }

    /**
     * 获取节点的返回类型
     *
     * @return
     */
    @Override
    public int getReturnType() {
        return processor.getFunction().getReturnType();
    }

    /**
     * 获取节点对应的sql
     */
    @Override
    public String getSql() {
        return processor.getSql(subNodes);
    }

    /**
     * 获取节点对应的表达式
     */
    @Override
    public String getExpr() {
        return processor.getExpr(subNodes);
    }

    public FunctionProcessor getProcessor() {
        return processor;
    }
}
