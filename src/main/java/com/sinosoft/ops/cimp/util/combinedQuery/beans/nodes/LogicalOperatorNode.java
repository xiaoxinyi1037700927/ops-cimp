package com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes;

import com.sinosoft.ops.cimp.util.combinedQuery.enums.LogicalOperator;

/**
 * 逻辑操作符节点(and/or)
 */
public class LogicalOperatorNode extends Node {
    public static final int CODE = 1 << 1;
    public static final int SUPPORT_NODES = LogicalOperatorNode.CODE | OperatorNode.CODE | BracketsNode.CODE;

    private LogicalOperator logicalOperator;

    public LogicalOperatorNode(String expr, LogicalOperator logicalOperator) {
        super(expr, false, 2, SUPPORT_NODES, CODE);
        this.logicalOperator = logicalOperator;
    }

    /**
     * 获取节点对应的sql
     *
     * @return
     */
    @Override
    public String getSql() {
        return String.format(logicalOperator.getSql(), subNodes.get(0).getSql(), subNodes.get(1).getSql());
    }

}
