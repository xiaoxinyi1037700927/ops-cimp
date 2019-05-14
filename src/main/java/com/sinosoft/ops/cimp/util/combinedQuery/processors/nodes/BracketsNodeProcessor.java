package com.sinosoft.ops.cimp.util.combinedQuery.processors.nodes;

import com.sinosoft.ops.cimp.util.combinedQuery.beans.CombinedQueryParseException;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.BracketsNode;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.Node;
import com.sinosoft.ops.cimp.util.combinedQuery.enums.Brackets;
import org.springframework.stereotype.Component;

import java.util.Deque;

/**
 * 括号节点处理器
 */
@Component
public class BracketsNodeProcessor implements NodeProcessor {

    /**
     * 将表达式解析为节点
     *
     * @param expr
     * @return
     * @throws CombinedQueryParseException
     */
    @Override
    public Node parse(String expr) throws CombinedQueryParseException {
        return new BracketsNode(expr);
    }

    /**
     * 表达式是否满足节点
     *
     * @param expr
     * @return
     */
    @Override
    public boolean support(String expr) {
        for (Brackets brackets : Brackets.values()) {
            if (brackets.getName().equals(expr)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将节点push进堆栈中
     *
     * @param stack
     * @param node
     */
    @Override
    public void pushNode(Deque<Node> stack, Node node) {
        stack.push(node);
    }
}
