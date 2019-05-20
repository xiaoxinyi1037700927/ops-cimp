package com.sinosoft.ops.cimp.util.combinedQuery.processors.nodes;

import com.sinosoft.ops.cimp.util.combinedQuery.beans.CombinedQueryParseException;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.BracketsNode;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.Node;
import com.sinosoft.ops.cimp.util.combinedQuery.enums.Brackets;
import com.sinosoft.ops.cimp.util.combinedQuery.enums.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Deque;
import java.util.NoSuchElementException;

/**
 * 括号节点处理器
 */
@Component
public class BracketsNodeProcessor extends NodeProcessor {

    private static final Logger logger = LoggerFactory.getLogger(BracketsNodeProcessor.class);

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
        return new BracketsNode(getBrackets(expr));
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

        if (bracketsNode.isComplete() && stack.size() > 0 && stack.peek().getReturnType() == Type.LOGICAL_OPERATOR.getCode()) {
            //完整的括号节点，且栈首是一个逻辑操作符节点
            Node first = stack.pop();
            first.addSubNode(bracketsNode);
            return first;
        } else if (Brackets.RIGHT.equals((bracketsNode).getBrackets())) {
            //如果是右括号，合并节点。
            //此时，括号之间应该有且只有一个解析后的节点
            try {
                Node first = stack.pop();

                if (first.getReturnType() == Type.BRACKETS.getCode() && !first.isComplete()) {
                    //如果节点类型是括号且不完整，则说明这对括号之间没有表达式
                    throw new CombinedQueryParseException("括号之间缺失表达式！");
                }

                Node second = stack.pop();
                if (second.getReturnType() != Type.BRACKETS.getCode()) {
                    //如果堆栈中第二个节点不是括号节点，解析失败
                    logger.error("未合并的节点！");
                    throw new CombinedQueryParseException("表达式解析失败!");
                }

                second.addSubNode(first);
                return second;
            } catch (NoSuchElementException e) {
                throw new CombinedQueryParseException("缺失的 ( ");
            }
        } else {
            stack.push(node);
        }

        return null;
    }


}
