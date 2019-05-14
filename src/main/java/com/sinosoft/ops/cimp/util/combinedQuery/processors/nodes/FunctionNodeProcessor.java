package com.sinosoft.ops.cimp.util.combinedQuery.processors.nodes;

import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.FunctionNode;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.Node;
import org.springframework.stereotype.Component;

/**
 * 函数节点处理器
 */
@Component
public class FunctionNodeProcessor implements NodeProcessor {

    /**
     * 将表达式解析为节点
     *
     * @param expr
     * @return
     */
    @Override
    public Node parse(String expr) {
        return new FunctionNode(expr);
    }

    /**
     * 表达式是否满足节点
     *
     * @param expr
     * @return
     */
    @Override
    public boolean support(String expr) {
        return false;
    }
}
