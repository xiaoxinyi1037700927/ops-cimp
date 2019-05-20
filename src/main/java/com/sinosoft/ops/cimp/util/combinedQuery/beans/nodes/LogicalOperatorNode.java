package com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes;

import com.sinosoft.ops.cimp.util.combinedQuery.enums.LogicalOperator;
import com.sinosoft.ops.cimp.util.combinedQuery.enums.Type;

/**
 * 逻辑操作符节点(and/or)
 */
public class LogicalOperatorNode extends Node {
    private static final int[] SUPPORT_SUB_TYPES = new int[]{Type.LOGICAL_OPERATOR.getCode() | Type.OPERATOR.getCode() | Type.BRACKETS.getCode(), Type.LOGICAL_OPERATOR.getCode() | Type.OPERATOR.getCode() | Type.BRACKETS.getCode()};

    private LogicalOperator logicalOperator;

    public LogicalOperatorNode(LogicalOperator logicalOperator) {
        super(false, SUPPORT_SUB_TYPES);
        this.logicalOperator = logicalOperator;
    }

    /**
     * 获取节点的返回类型
     *
     * @return
     */
    @Override
    public int getReturnType() {
        return Type.LOGICAL_OPERATOR.getCode();
    }

    /**
     * 获取节点对应的sql
     *
     * @return
     */
    @Override
    public String getSql() {
        return String.format(logicalOperator.getSqlFormat(), subNodes.get(0).getSql(), subNodes.get(1).getSql());
    }

    /**
     * 获取节点对应的表达式
     *
     * @return
     */
    @Override
    public String getExpr() {
        return String.format(logicalOperator.getExprFormat(), subNodes.get(0).getExpr(), subNodes.get(1).getExpr());
    }


    public LogicalOperator getLogicalOperator() {
        return logicalOperator;
    }
}
