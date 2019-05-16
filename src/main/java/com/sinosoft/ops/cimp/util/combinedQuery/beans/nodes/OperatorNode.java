
package com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes;


import com.sinosoft.ops.cimp.util.combinedQuery.processors.operators.OperatorProcessor;

/**
 * 运算符节点
 */
public class OperatorNode extends Node {
    public static final int CODE = 1 << 2;
    public static final int SUPPORT_NODES = FieldNode.CODE | FunctionNode.CODE | ValueNode.CODE;
    private OperatorProcessor processor;

    public OperatorNode(String expr, OperatorProcessor processor) {
        super(expr, false, processor.getOperator().getSubNodeNum(), SUPPORT_NODES, CODE);
        this.processor = processor;
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

}
