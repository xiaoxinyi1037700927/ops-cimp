package com.sinosoft.ops.cimp.util.combinedQuery.processors.operators;


import com.sinosoft.ops.cimp.util.combinedQuery.beans.CombinedQueryParseException;
import com.sinosoft.ops.cimp.util.combinedQuery.enums.Operator;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.Node;

import java.util.List;

public abstract class OperatorProcessor {
    private Operator operator;

    public OperatorProcessor(Operator operator) {
        this.operator = operator;
    }

    /**
     * 表达式是否满足运算符
     * 复杂的表达式(如：在...之中)需由子类继承实现
     *
     * @param expr
     * @return
     */
    public boolean support(String expr) {
        return operator.getName().equalsIgnoreCase(expr);
    }


    /**
     * 用于运算符和值在一起的表达式的解析(如：介于'1'和'9'之间),由子类继承实现
     *
     * @param node
     * @param expr
     * @throws CombinedQueryParseException
     */
    public void parse(Node node, String expr) throws CombinedQueryParseException {
    }


    /**
     * 获取表达式对应的sql文本
     *
     * @param subNodes
     * @return
     */
    public String getSql(List<Node> subNodes) {
        Object[] subSql = new Object[operator.getSubNodeNum()];
        for (int i = 0; i < subNodes.size(); ++i) {
            subSql[i] = subNodes.get(i).getSql();
        }
        return String.format(operator.getSql(), subSql);
    }

    public Operator getOperator() {
        return operator;
    }
}
