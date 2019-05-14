package com.sinosoft.ops.cimp.util.combinedQuery.processors.nodes;

import com.sinosoft.ops.cimp.util.combinedQuery.beans.CombinedQueryParseException;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.Node;

import java.util.Deque;

/**
 * 语法树节点处理器
 */
public interface NodeProcessor {
    /**
     * 将表达式解析为节点
     *
     * @param expr
     * @return
     * @throws CombinedQueryParseException
     */
    Node parse(String expr) throws CombinedQueryParseException;

    /**
     * 表达式是否满足节点
     *
     * @param expr
     * @return
     */
    boolean support(String expr);

    /**
     * 将节点push进堆栈中
     *
     * @param stack
     * @param node
     */
    default void pushNode(Deque<Node> stack, Node node) {
        stack.push(node);
    }


}
