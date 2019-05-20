package com.sinosoft.ops.cimp.util.combinedQuery.processors.nodes;

import com.sinosoft.ops.cimp.util.combinedQuery.beans.CombinedQueryParseException;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.FunctionNode;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.Node;
import com.sinosoft.ops.cimp.util.combinedQuery.enums.Type;
import com.sinosoft.ops.cimp.util.combinedQuery.processors.functions.FunctionProcessor;
import org.springframework.stereotype.Component;

import java.util.Deque;

/**
 * 函数节点处理器
 */
@Component
public class FunctionNodeProcessor extends NodeProcessor {

    private final FunctionProcessor[] functionProcessors;

    public FunctionNodeProcessor(FunctionProcessor[] functionProcessors) {
        this.functionProcessors = functionProcessors;
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
        return node instanceof FunctionNode;
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
        FunctionProcessor processor = getProcessor(expr);
        Node node = new FunctionNode(processor);
        processor.parse(node, expr);

        return node;
    }

    private FunctionProcessor getProcessor(String expr) {
        for (FunctionProcessor processor : functionProcessors) {
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
        if (stack.size() > 0 && stack.peek().getReturnType() == Type.OPERATOR.getCode()) {
            Node first = stack.pop();
            first.addSubNode(node);
            return first;
        } else {
            stack.push(node);
        }

        return null;
    }


}
