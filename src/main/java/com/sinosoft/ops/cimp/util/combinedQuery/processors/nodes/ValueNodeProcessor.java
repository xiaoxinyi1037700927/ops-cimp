package com.sinosoft.ops.cimp.util.combinedQuery.processors.nodes;

import com.sinosoft.ops.cimp.util.combinedQuery.beans.CombinedQueryParseException;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.Node;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.ValueNode;
import com.sinosoft.ops.cimp.util.combinedQuery.processors.operators.InProcessor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 值节点处理器
 */
@Component
public class ValueNodeProcessor implements NodeProcessor {

    private static final Pattern PATTERN = Pattern.compile("^'(.*?)'$");
    private static final Pattern PATTERN_CODE = Pattern.compile("^\\[(\\d+)\\].+?$");

    /**
     * 表达式是否满足节点
     *
     * @param expr
     * @return
     */
    @Override
    public boolean support(String expr) {
        Matcher matcher = PATTERN.matcher(expr);
        return matcher.matches();
    }

    /**
     * 节点是否匹配
     *
     * @param node
     * @return
     */
    @Override
    public boolean support(Node node) {
        return node instanceof ValueNode;
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
        Matcher matcher = PATTERN.matcher(expr);
        if (!matcher.matches()) {
            throw new CombinedQueryParseException("无法解析的值节点表达式：" + expr);
        }

        String value = matcher.group(1);

        if (expr.contains(InProcessor.IDENTIFIER)) {
            //处理in操作符的值
            return parseInProcessorValue(value.replace(InProcessor.IDENTIFIER, ""));
        }

        //处理码值
        String code = parseCode(value);
        if (code != null) {
            return new ValueNode(expr, code, true);
        }

        return new ValueNode(expr, expr, false);
    }

    /**
     * 处理码值,提取[]中的code值
     *
     * @param expr
     * @return
     */
    private String parseCode(String expr) {
        //判断是否是码值
        Matcher matcher = PATTERN_CODE.matcher(expr);
        if (matcher.matches()) {
            return matcher.group(1);
        }

        return null;
    }

    /**
     * 处理in操作符的值,格式 'value1','value2'...
     *
     * @param expr
     * @return
     */
    private Node parseInProcessorValue(String expr) throws CombinedQueryParseException {
        boolean isCode = false;

        try {
            String[] arr = expr.substring(expr.indexOf("\"") + 1, expr.lastIndexOf("\"")).split("\"\\s*,\\s*\"");

            List<String> values = new ArrayList<>(arr.length);
            String code;
            for (String value : arr) {
                code = parseCode(value);
                if (code != null) {
                    values.add(code);
                    isCode = true;
                } else if (isCode) {
                    //同时存在码值和非码值
                    throw new CombinedQueryParseException("非法的IN操作符值：" + expr);
                } else {
                    values.add(value);
                }
            }

            return new ValueNode(expr, values.stream().collect(Collectors.joining("','", "'", "'")), isCode);
        } catch (CombinedQueryParseException e) {
            throw e;
        } catch (Exception e) {
            throw new CombinedQueryParseException("非法的IN操作符值：" + expr);
        }
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
        if (stack.size() > 0 && (stack.peek().getSupportSubNodes() & ValueNode.CODE) != 0) {
            //如果栈首支持value的子节点，合并
            Node first = stack.pop();
            first.addSubNode(node);

            return first;
        } else {
            stack.push(node);
        }

        return null;
    }
}
