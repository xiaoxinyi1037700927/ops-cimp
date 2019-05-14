package com.sinosoft.ops.cimp.util.combinedQuery.processors.nodes;

import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.LogicalOperatorNode;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.Node;
import com.sinosoft.ops.cimp.util.combinedQuery.enums.LogicalOperator;
import org.springframework.stereotype.Component;

/**
 * 逻辑操作符节点(and/or)处理器
 */
@Component
public class LogicalOperatorNodeProcessor implements NodeProcessor {

    /**
     * 将表达式解析为节点
     *
     * @param expr
     * @return
     */
    @Override
    public Node parse(String expr) {
        return new LogicalOperatorNode(expr);
    }

    /**
     * 表达式是否满足节点
     *
     * @param expr
     * @return
     */
    @Override
    public boolean support(String expr) {
        for (LogicalOperator logicalOperator : LogicalOperator.values()) {
            if (logicalOperator.getName().equalsIgnoreCase(expr)) {
                return true;
            }
        }
        return false;
    }
}
