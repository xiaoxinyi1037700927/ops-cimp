package com.sinosoft.ops.cimp.util.combinedQuery.processors.nodes;

import com.sinosoft.ops.cimp.util.combinedQuery.enums.Brackets;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.CombinedQueryParseException;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.BracketsNode;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.LogicalOperatorNode;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.Node;
import org.springframework.stereotype.Component;

import java.util.Deque;

/**
 * 括号节点处理器
 */
@Component
public class BracketsNodeProcessor implements NodeProcessor {

    /**
     * 表达式是否满足节点
     *
     * @param expr
     * @return
     */
    @Override
    public boolean support(String expr) {
        return getBrackets(expr) != null;
    }

    /**
     * 节点是否匹配
     *
     * @param node
     * @return
     */
    @Override
    public boolean support(Node node) {
        return node instanceof BracketsNode;
    }

    /**
     * 将表达式解析为节点
     *
     * @param expr
     * @return
     */
    @Override
    public Node parse(String expr) {
        return new BracketsNode(expr, getBrackets(expr));
    }

    private Brackets getBrackets(String expr) {
        for (Brackets brackets : Brackets.values()) {
            if (brackets.getName().equals(expr)) {
                return brackets;
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
        BracketsNode bracketsNode = (BracketsNode) node;

        if (bracketsNode.isComplete()) {
            //完整的括号节点
            if (stack.size() > 0 && stack.peek().getCode() == LogicalOperatorNode.CODE) {
                //前面是一个逻辑操作符节点
                stack.peek().addSubNode(bracketsNode);
            } else {
                stack.push(bracketsNode);
            }
        } else if (Brackets.LEFT.equals((bracketsNode).getBrackets())) {
            //如果是左括号，直接入栈
            stack.push(bracketsNode);
        } else {
            //如果是右括号，合并节点。
            //此时，括号之间应该有且只有一个解析后的节点
            Node first = stack.pop();

            if (first.getCode() == BracketsNode.CODE && !first.isComplete()) {
                //如果节点类型是括号且不完整，则说明这对括号之间没有表达式，直接忽略
                return null;
            }

            Node second = stack.pop();
            if (second.getCode() != BracketsNode.CODE) {
                //如果堆栈中第二个节点不是括号节点，解析失败
                throw new CombinedQueryParseException("解析失败!");
            }

            first.setParent(second);
            second.addSubNode(first);

            pushNode(stack, second);
        }

        return null;
    }


}
