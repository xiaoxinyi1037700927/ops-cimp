
package com.sinosoft.ops.cimp.util.combinedQuery.processors.nodes;

import com.sinosoft.ops.cimp.util.combinedQuery.beans.CombinedQueryParseException;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.Node;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.OperatorNode;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.ValueNode;
import com.sinosoft.ops.cimp.util.combinedQuery.enums.Operator;
import com.sinosoft.ops.cimp.util.combinedQuery.enums.Type;
import com.sinosoft.ops.cimp.util.combinedQuery.processors.operators.OperatorProcessor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Deque;

/**
 * 运算符节点处理器
 */
@Component
public class OperatorNodeProcessor extends NodeProcessor {

    private final OperatorProcessor[] OperatorProcessors;


    public OperatorNodeProcessor(OperatorProcessor[] operatorProcessors) {
        OperatorProcessors = operatorProcessors;
    }

    /**
     * 表达式是否满足节点
     *
     * @param expr
     * @return
     */
    @Override
    public boolean support(String expr) {
        return getProcessor(expr) != null;
    }

    /**
     * 节点是否匹配
     *
     * @param node
     * @return
     */
    @Override
    public boolean support(Node node) {
        return node instanceof OperatorNode;
    }

    /**
     * 将表达式解析为节点
     *
     * @param expr
     * @return
     * @throws CombinedQueryParseException
     */
    @Override
    public Node parse(String expr) throws CombinedQueryParseException {
        OperatorProcessor processor = getProcessor(expr);
        Node node = new OperatorNode(processor);
        processor.parse(node, expr);

        return node;
    }

    /**
     * 获取表达式对应的运算符处理器
     *
     * @param expr
     * @return
     */
    private OperatorProcessor getProcessor(String expr) {
        for (OperatorProcessor processor : OperatorProcessors) {
            if (processor.support(expr)) {
                return processor;
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
            if (stack.size() > 0 && stack.peek().getReturnType() == Type.LOGICAL_OPERATOR.getCode()) {
                Node first = stack.pop();
                first.addSubNode(node);

                return first;
            } else {
                stack.push(node);
            }
        } else if (stack.size() > 0 && (stack.peek().getReturnType() & node.getNextSubType()) != 0) {
            Node first = stack.pop();
            node.addSubNode(first);

            if (node.isComplete()) {
                return node;
            } else {
                stack.push(node);
            }
        } else {
            throw new CombinedQueryParseException("非法表达式!");
        }

        return null;
    }


    /**
     * 校验节点
     *
     * @param node
     * @throws CombinedQueryParseException
     */
    @Override
    public void checkNode(Node node) throws CombinedQueryParseException {
        super.checkNode(node);

        //校验参数类型
        ((OperatorNode) node).getProcessor().checkType((OperatorNode) node);

    }

    /**
     * 表达式开始处或括号开始处默认的1=1
     *
     * @return
     * @throws CombinedQueryParseException
     */
    public Node getDefaultNode() throws CombinedQueryParseException {
        OperatorNode node = new OperatorNode(getProcessor(Operator.EQ.getName()));

        node.addSubNode(new ValueNode(Collections.singletonList("1"), Collections.emptyList(), false, false, Type.NUMBER.getCode()));
        node.addSubNode(new ValueNode(Collections.singletonList("1"), Collections.emptyList(), false, false, Type.NUMBER.getCode()));

        return node;
    }

}
