package com.sinosoft.ops.cimp.util.combinedQuery;

import com.sinosoft.ops.cimp.util.combinedQuery.beans.CombinedQueryParseException;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.ExprStream;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.Node;
import com.sinosoft.ops.cimp.util.combinedQuery.processors.nodes.NodeProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Deque;
import java.util.LinkedList;

@Component
public class CombinedQueryParser {

    private final NodeProcessor[] nodeProcessors;

    @Autowired
    public CombinedQueryParser(NodeProcessor[] nodeProcessors) {
        this.nodeProcessors = nodeProcessors;
    }


    /**
     * 将表达式解析为sql
     */
    public String parse(String expression) throws CombinedQueryParseException {
        Node root = parseGramTree(expression, null);
        return root != null ? root.getSql() : null;
    }

    /**
     * @param expression
     * @param parent     parent为null是根节点
     * @return
     * @throws CombinedQueryParseException
     */
    private Node parseGramTree(String expression, Node parent) throws CombinedQueryParseException {
        Deque<Node> stack = new LinkedList<>();
        ExprStream stream = new ExprStream(expression);

        String expr;
        Node node;
        NodeProcessor processor;
        while (stream.hasNext()) {
            expr = getExpr(stream);
            if (expr == null) {
                break;
            }

            //获取节点处理器
            processor = getNodeProcessor(expr);
            //将表达式解析为节点
            node = processor.parse(expr);

            //节点入栈
            Node next = node;
            do {
                next = processor.pushNode(stack, next);
                processor = getNodeProcessor(next);
            } while (next != null);

            //处理子节点
            for (String exprStr : node.getSubNodeExpr()) {
                node.addSubNode(parseGramTree(exprStr, node));
            }
        }

        if (stack.size() > 1) {
            //如果处理结束后堆栈中的节点数大于1，说明解析出错
            throw new CombinedQueryParseException("解析失败：" + expression);
        } else if (stack.size() == 0) {
            return null;
        }
        //设置父节点，根节点的parent为null
        Node result = stack.pop();
        result.setParent(parent);

        return result;
    }

    /**
     * 获取节点处理器
     */
    private NodeProcessor getNodeProcessor(String expr) throws CombinedQueryParseException {
        for (NodeProcessor nodeProcessor : nodeProcessors) {
            if (nodeProcessor.support(expr)) {
                return nodeProcessor;
            }
        }
        throw new CombinedQueryParseException("no nodeProcessor for expression: " + expr);
    }

    /**
     * 获取节点处理器
     */
    private NodeProcessor getNodeProcessor(Node node) throws CombinedQueryParseException {
        if (node == null) {
            return null;
        }
        for (NodeProcessor nodeProcessor : nodeProcessors) {
            if (nodeProcessor.support(node)) {
                return nodeProcessor;
            }
        }
        throw new CombinedQueryParseException("no nodeProcessor for node: " + node.getClass());
    }

    /**
     * 获取节点对应的表达式
     */
    private String getExpr(ExprStream stream) throws CombinedQueryParseException {
        //获取第一个非空格字符
        char first = stream.next();
        while (first == ' ') {
            if (stream.hasNext()) {
                first = stream.next();
            } else {
                return null;
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append(first);

        switch (first) {
            case '(':
            case ')':
                break;
            case '\'':
                //value
                while (stream.hasNext()) {
                    char c = stream.next();
                    sb.append(c);
                    if (c == '\'') {
                        break;
                    }
                }
                break;
            default:
                while (stream.hasNext()) {
                    char c = stream.next();
                    if (c == ' ') {
                        //默认取到空格为止
                        break;
                    }
                    sb.append(c);

                    if (c == '[') {
                        //处理运算符在[...]之中，取到与之对应的右括号为止
                        sb.append(stream.getUtilRightBrackets('[', ']'));
                    }

                    if (c == '(') {
                        //函数格式，取到与之对应的右括号为止
                        sb.append(stream.getUtilRightBrackets('(', ')'));
                        break;
                    }
                }
        }

        return sb.toString();
    }


}
