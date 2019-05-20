package com.sinosoft.ops.cimp.util.combinedQuery.processors.nodes;

import com.sinosoft.ops.cimp.util.combinedQuery.beans.CombinedQueryParseException;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.LogicalOperatorNode;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.Node;
import com.sinosoft.ops.cimp.util.combinedQuery.enums.LogicalOperator;
import org.springframework.stereotype.Component;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 逻辑操作符节点(and/or)处理器
 */
@Component
public class LogicalOperatorNodeProcessor extends NodeProcessor {
    private static Map<LogicalOperator, Pattern> logicalOperators = new HashMap<>();

    public LogicalOperatorNodeProcessor() {
        //预编译逻辑操作符的正则表达式
        for (LogicalOperator logicalOperator : LogicalOperator.values()) {
            logicalOperators.put(logicalOperator, Pattern.compile(logicalOperator.getRegex()));
        }
    }

    /**
     * 表达式是否满足节点
     *
     * @param expr
     * @return
     */
    @Override
    public boolean support(String expr) {
        return getLogicalOperator(expr) != null;
    }

    /**
     * 节点是否匹配
     *
     * @param node
     * @return
     */
    @Override
    public boolean support(Node node) {
        return node instanceof LogicalOperatorNode;
    }

    /**
     * 将表达式解析为节点
     *
     * @param expr
     * @return
     */
    @Override
    public Node parse(String expr) {
        return new LogicalOperatorNode(getLogicalOperator(expr));
    }

    private LogicalOperator getLogicalOperator(String expr) {
        for (Map.Entry<LogicalOperator, Pattern> logicalOperator : logicalOperators.entrySet()) {
            if (logicalOperator.getValue().matcher(expr).matches()) {
                return logicalOperator.getKey();
            }
        }
        return null;
    }

    /**
     * 将节点push进堆栈中
     *
     * @param stack
     * @param node
     * @return
     * @throws CombinedQueryParseException
     */
    @Override
    public Node pushNode(Deque<Node> stack, Node node) throws CombinedQueryParseException {
        if (node.isComplete()) {
            stack.push(node);
        } else if (stack.size() > 0 && (stack.peek().getReturnType() & node.getNextSubType()) != 0) {
            Node first = stack.pop();
            node.addSubNode(first);
            stack.push(node);
        } else {
            throw new CombinedQueryParseException("非法表达式!");
        }

        return null;
    }

}
