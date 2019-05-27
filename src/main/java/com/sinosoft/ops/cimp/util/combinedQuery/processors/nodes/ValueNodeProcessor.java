package com.sinosoft.ops.cimp.util.combinedQuery.processors.nodes;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sinosoft.ops.cimp.entity.sys.syscode.QSysCodeItem;
import com.sinosoft.ops.cimp.entity.sys.syscode.SysCodeItem;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.CombinedQueryParseException;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.FieldNode;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.Node;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.ValueNode;
import com.sinosoft.ops.cimp.util.combinedQuery.enums.Type;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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
    private static final Pattern PATTERN_CODE = Pattern.compile("^\\[(.+?)](.+?)$");


    private final JPAQueryFactory jpaQueryFactory;

    public ValueNodeProcessor(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
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

                //判断值是不是number类型
                try {
                    new BigDecimal(s);
                    returnType |= Type.NUMBER.getCode();
                } catch (Exception e) {
                    returnType |= Type.STRING.getCode();
                }
            }
        }

        if (codeNames.size() > 0 && values.size() != codeNames.size()) {
            //数组中同时存在码值和非码值
            throw new CombinedQueryParseException("码值和非码值不能同时存在：" + expr);
        }

        if (returnType != Type.NUMBER.getCode() && returnType != Type.STRING.getCode() && returnType != Type.CODE.getCode()) {
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
            Integer codeSetId = getCodeSetId(node.getParent());
            if (codeSetId == null) {
                throw new CombinedQueryParseException("没有找到码值对应的代码集：" + vNode.getExpr());
            }

            for (int i = 0; i < vNode.getValues().size(); i++) {
                String code = vNode.getValues().get(i);
                String name = vNode.getCodeNames().get(i);
                if (!judgeCode(codeSetId, code, name)) {
                    throw new CombinedQueryParseException("错误的码值：[" + code + "]" + name);
                }
            }
        }
    }

    /**
     * 获取代码集
     *
     * @return
     */
    private Integer getCodeSetId(Node node) {
        for (Node subNode : node.getSubNodes()) {
            if (subNode instanceof FieldNode) {
                return ((FieldNode) subNode).getCodeSetId();
            }
        }
        return null;
    }

    /**
     * 判断码值的正确性
     *
     * @return
     */
    private boolean judgeCode(Integer codeSetId, String code, String name) {
        QSysCodeItem qSysCodeItem = QSysCodeItem.sysCodeItem;

        SysCodeItem sysCodeItem = jpaQueryFactory.select(qSysCodeItem)
                .from(qSysCodeItem)
                .where(qSysCodeItem.codeSetId.eq(codeSetId).and(qSysCodeItem.code.eq(code)).and(qSysCodeItem.name.eq(name)))
                .fetchOne();

        return sysCodeItem != null;
    }

    /**
     * 判断表达式是否是码值
     *
     * @param expr
     * @return
     */
    public boolean isCode(String expr) {
        List<String> list = Arrays.asList(expr.substring(expr.indexOf("'") + 1, expr.lastIndexOf("'")).split("'\\s*,\\s*'"));

        Matcher matcher;
        for (String s : list) {
            matcher = PATTERN_CODE.matcher(s);
            if (!matcher.matches()) {
                return false;
            }
        }
        return true;
    }
}
