package com.sinosoft.ops.cimp.util.combinedQuery.processors.operators;


import com.sinosoft.ops.cimp.util.combinedQuery.beans.CombinedQueryParseException;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.Node;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.OperatorNode;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.ValueNode;
import com.sinosoft.ops.cimp.util.combinedQuery.enums.Operator;
import com.sinosoft.ops.cimp.util.combinedQuery.enums.Type;

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
        Object[] subSql = new Object[operator.getParamsNum()];
        for (int i = 0; i < subNodes.size(); ++i) {
            subSql[i] = subNodes.get(i).getSql();
        }
        return String.format(operator.getSqlFormat(), subSql);
    }


    /**
     * 获取表达式对应的表达式
     *
     * @param subNodes
     * @return
     */
    public String getExpr(List<Node> subNodes) {
        Object[] subExpr = new Object[operator.getParamsNum()];
        for (int i = 0; i < subNodes.size(); ++i) {
            subExpr[i] = subNodes.get(i).getExpr();
        }
        return String.format(operator.getExprFormat(), subExpr);
    }

    /**
     * 校验表达式参数类型
     *
     * @param node
     * @throws CombinedQueryParseException
     */
    public void checkType(OperatorNode node) throws CombinedQueryParseException {
        List<Node> subNodes = node.getSubNodes();
        if (subNodes.size() == 1) {
            //无需校验只有一个参数的运算符
            return;
        }

        Node first = subNodes.get(0);

        //判断参数的类型是否匹配
        for (int i = 1; i < subNodes.size(); i++) {
            Node next = subNodes.get(i);
            if (first.getReturnType() == Type.STRING.getCode() && next instanceof ValueNode && next.getReturnType() == Type.NUMBER.getCode()) {
                //处理string类型和number类型的值节点的情况
                ((ValueNode) next).setReturnType(Type.STRING.getCode());
            }

            if (first.getReturnType() != next.getReturnType()) {
                throw new CombinedQueryParseException("运算符[" + node.getProcessor().getOperator().getName() + "]参数类型不匹配");
            }
        }

    }

    public Operator getOperator() {
        return operator;
    }
}
