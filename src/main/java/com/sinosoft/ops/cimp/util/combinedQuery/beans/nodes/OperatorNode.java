
package com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes;


import com.sinosoft.ops.cimp.util.combinedQuery.enums.Type;
import com.sinosoft.ops.cimp.util.combinedQuery.processors.operators.OperatorProcessor;

/**
 * 运算符节点
 */
public class OperatorNode extends Node {
    private OperatorProcessor processor;

    public OperatorNode(String expr, OperatorProcessor processor) {
        super(expr, false, processor.getOperator().getParamsType());
        this.processor = processor;
    }

    /**
     * 获取节点的返回类型
     *
     * @return
     */
    @Override
    public int getReturnType() {
        return Type.OPERATOR.getCode();
    }

    /**
     * 获取节点对应的sql
     *
     * @return
     */
    @Override
    public String getSql() {
        return processor.getSql(subNodes);
    }

    /**
     * 获取节点对应的表达式
     *
     * @return
     */
    @Override
    public String getExpr() {
        return processor.getExpr(subNodes);
    }

    public OperatorProcessor getProcessor() {
        return processor;
    }
}
