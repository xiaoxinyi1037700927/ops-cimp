package com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes;


import com.sinosoft.ops.cimp.util.combinedQuery.enums.Brackets;

/**
 * 括号节点
 */
public class BracketsNode extends Node {
    public static final int CODE = 1;
    public static final int SUPPORT_NODES = LogicalOperatorNode.CODE | OperatorNode.CODE | BracketsNode.CODE;
    private static final String SQL = " (%s) ";

    private Brackets brackets;

    public BracketsNode(String expr, Brackets brackets) {
        super(expr, false, 1, SUPPORT_NODES, CODE);
        this.brackets = brackets;
    }

    /**
     * 获取节点对应的sql
     *
     * @return
     */
    @Override
    public String getSql() {
        return String.format(SQL, subNodes.get(0).getSql());
    }

    public Brackets getBrackets() {
        return brackets;
    }

}
