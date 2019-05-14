
package com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes;


import com.sinosoft.ops.cimp.util.combinedQuery.beans.expressions.Expression;

/**
 * 表达式节点
 */
public class ExpressionNode extends Node {
    public static final int CODE = 1 << 2;
    /**
     * 子节点支持的节点类型
     */
    public static final int supportNodes = FieldNode.CODE & FunctionNode.CODE & ValueNode.CODE;

    private Expression expression;

    public ExpressionNode(String exprStr, Expression expression) {
        super(exprStr);
        this.expression = expression;
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
