
package com.sinosoft.ops.cimp.util.combinedQuery.processors.nodes;

import com.sinosoft.ops.cimp.util.combinedQuery.beans.expressions.Expression;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.ExpressionNode;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.Node;
import org.springframework.stereotype.Component;

/**
 * 表达式节点处理器
 */
@Component
public class ExpressionNodeProcessor implements NodeProcessor {

    private final Expression[] expressions;

    public ExpressionNodeProcessor(Expression[] expressions) {
        this.expressions = expressions;
    }

    /**
     * 将表达式解析为节点
     *
     * @param expr
     * @return
     */
    @Override
    public Node parse(String expr) {
        Expression expression = getExpression(expr);
        return new ExpressionNode(expr,expression);
    }

    /**
     * 表达式是否满足节点
     *
     * @param expr
     * @return
     */
    @Override
    public boolean support(String expr) {
        return getExpression(expr) != null;
    }

    private Expression getExpression(String expr) {
        for (Expression expression : expressions) {
            if (expression.supports(expr)) {
                return expression;
            }
        }
        return null;
    }
}
