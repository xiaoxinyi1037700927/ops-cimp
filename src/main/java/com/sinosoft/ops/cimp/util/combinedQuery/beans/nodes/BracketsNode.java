package com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes;


import com.sinosoft.ops.cimp.util.combinedQuery.enums.Brackets;
import com.sinosoft.ops.cimp.util.combinedQuery.enums.Type;

/**
 * 括号节点
 */
public class BracketsNode extends Node {
    private static final int[] SUPPORT_SUB_TYPES = new int[]{Type.LOGICAL_OPERATOR.getCode() | Type.OPERATOR.getCode() | Type.BRACKETS.getCode()};
    private static final String FORMAT = "(%s)";

    private Brackets brackets;

    public BracketsNode(Brackets brackets) {
        super(false, SUPPORT_SUB_TYPES);
        this.brackets = brackets;
    }

    /**
     * 获取节点的返回类型
     *
     * @return
     */
    @Override
    public int getReturnType() {
        return Type.BRACKETS.getCode();
    }

    /**
     * 获取节点对应的sql
     *
     * @return
     */
    @Override
    public String getSql() {
        return String.format(FORMAT, subNodes.get(0).getSql());
    }

    /**
     * 获取节点对应的表达式
     *
     * @return
     */
    public String getExpr() {
        return String.format(FORMAT, subNodes.get(0).getExpr());
    }

    public Brackets getBrackets() {
        return brackets;
    }

}
