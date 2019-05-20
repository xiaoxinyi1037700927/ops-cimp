package com.sinosoft.ops.cimp.util.combinedQuery.processors.nodes;

import com.sinosoft.ops.cimp.util.combinedQuery.beans.CombinedQueryParseException;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.Node;

import java.util.Deque;

/**
 * 语法树节点处理器
 */
public abstract class NodeProcessor {

    /**
     * 表达式是否匹配
     *
     * @param expr
     * @return
     */
    public abstract boolean support(String expr);

    /**
     * 节点是否匹配
     *
     * @param node
     * @return
     */
    public abstract boolean support(Node node);

    /**
     * 将表达式解析为节点
     *
     * @param expr
     * @return
     * @throws CombinedQueryParseException
     */
    public abstract Node parse(String expr) throws CombinedQueryParseException;

    /**
     * 将节点push进堆栈中
     *
     * @param stack
     * @param node
     * @return
     * @throws CombinedQueryParseException
     */
    public abstract Node pushNode(Deque<Node> stack, Node node) throws CombinedQueryParseException;

    /**
     * 校验节点
     * 子类重写时，必须先调用父类方法
     *
     * @param node
     * @throws CombinedQueryParseException
     */
    public void checkNode(Node node) throws CombinedQueryParseException {
        //校验节点完整性
        if (!node.isComplete()) {
            throw new CombinedQueryParseException("缺失表达式！");
        }

    }
}
