package com.sinosoft.ops.cimp.util.combinedQuery.processors.nodes;

import com.sinosoft.ops.cimp.util.combinedQuery.beans.CombinedQueryParseException;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.FieldNode;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.Node;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.ValueNode;
import com.sinosoft.ops.cimp.util.combinedQuery.enums.Type;
import com.sinosoft.ops.cimp.util.combinedQuery.utils.CodeUtil;
import com.sinosoft.ops.cimp.util.combinedQuery.utils.CombinedQueryUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 值节点处理器
 */
@Component
public class ValueNodeProcessor extends NodeProcessor {
    private static final Pattern PATTERN = Pattern.compile("^['|\\[](.*?)['|\\]]$");
    public static final Pattern PATTERN_CODE = Pattern.compile("^\\[(.+?)](.+?)$");

    private final CodeUtil codeUtil;

    public ValueNodeProcessor(CodeUtil codeUtil) {
        this.codeUtil = codeUtil;
    }

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

        boolean isArray = false;
        List<String> list = new ArrayList<>();
        if (expr.startsWith("'")) {
            //单个值
            list.add(value);
        } else {
            //数组
            list.addAll(Arrays.asList(value.substring(value.indexOf("'") + 1, value.lastIndexOf("'")).split("'\\s*,\\s*'")));
            isArray = true;
        }

        int returnType = 0;
        List<String> values = new ArrayList<>();
        List<String> codeNames = new ArrayList<>();
        for (String s : list) {
            //解析码值
            matcher = PATTERN_CODE.matcher(s);
            if (matcher.matches()) {
                values.add(matcher.group(1));
                codeNames.add(matcher.group(2));
                returnType |= Type.CODE.getCode();
            } else {
                values.add(s);

                returnType |= CombinedQueryUtil.getValueType(s);
            }
        }

        if (codeNames.size() > 0 && values.size() != codeNames.size()) {
            //数组中同时存在码值和非码值
            throw new CombinedQueryParseException("码值和非码值不能同时存在：" + expr);
        }

        if (CombinedQueryUtil.getTypeNum(returnType) != 1) {
            //如果存在多个类型的值，统一作为字符串处理
            returnType = Type.STRING.getCode();
        }

        return new ValueNode(values, codeNames, codeNames.size() > 0, isArray, returnType);
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
        if (stack.size() > 0 && (stack.peek().getNextSubType() & node.getReturnType()) != 0) {
            //如果栈首支持value类型的子节点，合并
            Node first = stack.pop();
            first.addSubNode(node);

            return first;
        } else {
            stack.push(node);
        }

        return null;
    }


    @Override
    public void checkNode(Node node) throws CombinedQueryParseException {
        super.checkNode(node);

        //判断码值是否正确
        if (node.getReturnType() == Type.CODE.getCode()) {
            ValueNode vNode = (ValueNode) node;
            FieldNode fieldNode = getFieldNode(node.getParent());
            if (fieldNode == null || fieldNode.getCodeSetId() == null) {
                throw new CombinedQueryParseException("没有找到码值对应的代码集：" + vNode.getExpr());
            }

            for (int i = 0; i < vNode.getValues().size(); i++) {
                String code = vNode.getValues().get(i);
                String name = vNode.getCodeNames().get(i);
                if (!codeUtil.judgeCode(fieldNode.getCodeSetId(), code, name)) {
                    throw new CombinedQueryParseException("错误的码值：[" + code + "]" + name);
                }
            }

            //设置码值对应的代码集名称
            vNode.setCodeSetName(fieldNode.getCodeSetName());
        }
    }

    /**
     * 获取码值对应的字段节点
     *
     * @return
     */
    private FieldNode getFieldNode(Node node) {
        for (Node subNode : node.getSubNodes()) {
            if (subNode instanceof FieldNode) {
                return ((FieldNode) subNode);
            }
        }

        return null;
    }


}
