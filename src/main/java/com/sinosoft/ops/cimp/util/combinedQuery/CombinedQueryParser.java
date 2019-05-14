package com.sinosoft.ops.cimp.util.combinedQuery;

import com.sinosoft.ops.cimp.util.combinedQuery.beans.CombinedQueryParseException;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.ExprStream;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.Node;
import com.sinosoft.ops.cimp.util.combinedQuery.processors.nodes.NodeProcessor;
import org.springframework.stereotype.Component;

import java.util.Deque;
import java.util.LinkedList;

@Component
public class CombinedQueryParser {

    private final NodeProcessor[] nodeProcessors;

    public CombinedQueryParser(NodeProcessor[] nodeProcessors) {
        this.nodeProcessors = nodeProcessors;
    }


    private String sql = "基本情况.姓名 等于 '周建琨' and " +
            " 基本信息.性别 等于 [男] and 基本信息.出生日期 等于 '2000-01-01' " +
            " 基本信息.出生日期 等于 系统日期() " +
            " (求商(月份差(基本情况.出生日期,系统日期()),12) 小于等于 '30' ) " +
            " and ((学历.学历代码 等于 [本科] or 学历.学历代码 等于 [硕士]) and 学历.教育类别 等于 [全日制教育]) " +
            "  ";

    /**
     * 将表达式解析为sql
     */
    public String parse(String expression) throws CombinedQueryParseException {
        return parseGramTree(expression).getSql();
    }

    /**
     * 将表达式解析为语法树
     */
    private Node parseGramTree(String expression) throws CombinedQueryParseException {
        Deque<Node> stack = new LinkedList<>();
        ExprStream stream = new ExprStream(expression);

        String expr;
        Node node;
        while (stream.hasNext()) {
            expr = getExpr(stream);
            if (expr == null) {
                break;
            }
            System.out.println(expr);

            //获取节点处理器
            NodeProcessor nodeProcessor = getNodeProcessor(expr);
            //将表达式解析为节点
            node = nodeProcessor.parse(expr);
            //处理子节点
            if (node.getSubExprStr() != null) {
                String[] subExpr = node.getSubExprStr();
                Node[] subNodes = node.getSubNodes();
                for (int i = 0; i < node.getSubExprStr().length; ++i) {
                    subNodes[i] = parseGramTree(subExpr[i]);
                }
            }

            nodeProcessor.pushNode(stack, node);
        }

        return stack.pop();
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
        throw new CombinedQueryParseException("未定义的表达式：" + expr);
    }

    /**
     * 获取节点对应的表达式(函数节点只取函数名,剩余部分后续处理)
     */
    private String getExpr(ExprStream stream) {
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
            case '[':
                while (stream.hasNext()) {
                    char c = stream.next();
                    sb.append(c);
                    if (c == ']') {
                        break;
                    }
                }
                break;
            case '\'':
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
                    if (c == ' ' || c == '(') {
                        stream.back();
                        break;
                    }
                    sb.append(c);
                }
        }

        return sb.toString();
    }


}
