package com.sinosoft.ops.cimp.util.combinedQuery;

import com.sinosoft.ops.cimp.util.combinedQuery.beans.CombinedQueryParseException;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.ExprStream;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.Expression;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.Param;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.*;
import com.sinosoft.ops.cimp.util.combinedQuery.processors.nodes.NodeProcessor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

@Component
public class CombinedQueryParser {

    private final NodeProcessor[] nodeProcessors;

    @Autowired
    public CombinedQueryParser(NodeProcessor[] nodeProcessors) {
        this.nodeProcessors = nodeProcessors;
    }


    public List<Expression> parseExpressions(String expression) throws CombinedQueryParseException {
        Node root = parseGramTree(expression, null);

        List<Expression> expressions = new ArrayList<>();

        if (root != null) {
            nodeToExpressions(root, expressions, null);
        }

        return expressions;
    }

    private void nodeToExpressions(Node root, List<Expression> expressions, String logicalOperator) {
        if (root instanceof LogicalOperatorNode) {
            //逻辑表达式节点，递归处理子节点
            List<Node> subNodes = root.getSubNodes();
            nodeToExpressions(subNodes.get(0), expressions, logicalOperator);
            nodeToExpressions(subNodes.get(1), expressions, ((LogicalOperatorNode) root).getLogicalOperator().getName());
        } else if (root instanceof BracketsNode) {
            //添加括号表达式
            Expression expression = new Expression();
            List<Expression> subExprs = new ArrayList<>();
            expression.setBracketsNode(true);
            expression.setSubExprs(subExprs);
            expression.setLogicalOperator(logicalOperator);
            expressions.add(expression);

            //递归处理子节点
            nodeToExpressions(root.getSubNodes().get(0), subExprs, null);
        } else if (root instanceof OperatorNode) {
            Expression expression = new Expression();
            expression.setText(root.getExpr());

            List<Node> subNodes = root.getSubNodes();
            expression.setParam1(nodeToParam(subNodes.get(0)));
            expression.setParam2(subNodes.size() > 1 ? nodeToParam(subNodes.get(1)) : null);
            expression.setLogicalOperator(logicalOperator);
            expression.setOperator(((OperatorNode) root).getProcessor().getOperator().getName());

            expressions.add(expression);
        }
    }

    private Param nodeToParam(Node node) {
        Param param = new Param();
        param.setText(node.getExpr());
        param.setReturnType(node.getReturnType());

        if (node instanceof FunctionNode) {
            param.setIsFunction(1);
            param.setFunctionName(((FunctionNode) node).getProcessor().getFunction().getName());

            List<Param> params = new ArrayList<>();
            for (Node subNode : node.getSubNodes()) {
                params.add(nodeToParam(subNode));
            }
            param.setParams(params);
        }

        return param;
    }

    /**
     * 将表达式解析为sql
     */
    public String parse(String expression) throws CombinedQueryParseException {
        Node root = parseGramTree(expression, null);

        if (root == null) {
            return null;
        }


        //调用后置处理器
        invokeNodePostProcessors(root);

        return root.getSql();
    }


    /**
     * 调用后置处理器
     *
     * @param root
     */
    private void invokeNodePostProcessors(Node root) {


        for (Node subNode : root.getSubNodes()) {
            invokeNodePostProcessors(subNode);
        }
    }


    /**
     * @param expression
     * @param parent     父节点为null是根节点
     * @return
     * @throws CombinedQueryParseException
     */
    private Node parseGramTree(String expression, Node parent) throws CombinedQueryParseException {
        if (StringUtils.isEmpty(expression)) {
            return null;
        }

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

        if (stack.size() != 1) {
            //如果处理结束后堆栈中的节点数大于1，说明解析出错
            throw new CombinedQueryParseException("非法表达式!");
        }


        node = stack.pop();
        node.setParent(parent);
        if (parent == null) {
            if (!(node instanceof BracketsNode || node instanceof LogicalOperatorNode || node instanceof OperatorNode)) {
                //根节点必须是括号节点、逻辑运算符节点、运算符节点之一
                throw new CombinedQueryParseException("非法表达式!");
            }

            //语法树节点校验
            checkNode(node);
        }

        return node;
    }

    /**
     * 节点检验
     *
     * @param node
     * @throws CombinedQueryParseException
     */
    private void checkNode(Node node) throws CombinedQueryParseException {
        NodeProcessor processor = getNodeProcessor(node);
        processor.checkNode(node);

        //校验子节点
        for (Node subNode : node.getSubNodes()) {
            checkNode(subNode);
        }
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
            case '[':
                //数组格式的value
                sb.append(stream.getUtilRightBrackets('[', ']'));
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
