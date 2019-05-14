package com.sinosoft.ops.cimp.util.combinedQuery.processors.nodes;

import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.Node;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.ValueNode;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 值节点处理器
 */
@Component
public class ValueNodeProcessor implements NodeProcessor {

    private static final Pattern pattern = Pattern.compile("^['|\\[](.*?)['|\\]]$");
    /**
     * 将表达式解析为节点
     *
     * @param expr
     * @return
     */
    @Override
    public Node parse(String expr) {
        return new ValueNode(expr);
    }

    /**
     * 表达式是否满足节点
     *
     * @param expr
     * @return
     */
    @Override
    public boolean support(String expr) {
        Matcher matcher = pattern.matcher(expr);
        return matcher.matches();
    }
}
