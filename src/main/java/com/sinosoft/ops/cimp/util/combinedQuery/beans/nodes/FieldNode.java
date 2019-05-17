package com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes;


import com.sinosoft.ops.cimp.util.combinedQuery.enums.Type;

/**
 * 字段节点
 */
public class FieldNode extends Node {
    public static final int[] SUPPORT_SUB_TYPES = new int[]{};
    private static final String FORMAT = " %s.%s ";

    private String tableName;
    private String tableNameCn;
    private String fieldName;
    private String fieldNameCn;
    private Type returnType;

    public FieldNode(String expr, String tableName, String tableNameCn, String fieldName, String fieldNameCn, Type returnType) {
        super(expr, true, SUPPORT_SUB_TYPES);
        this.tableName = tableName;
        this.tableNameCn = tableNameCn;
        this.fieldName = fieldName;
        this.fieldNameCn = fieldNameCn;
        this.returnType = returnType;
    }

    /**
     * 获取节点的返回类型
     *
     * @return
     */
    @Override
    public int getReturnType() {
        return returnType.getCode();
    }

    /**
     * 获取节点对应的sql
     *
     * @return
     */
    @Override
    public String getSql() {
        return String.format(FORMAT, tableName, fieldName);
    }

    /**
     * 获取节点对应的表达式
     *
     * @return
     */
    @Override
    public String getExpr() {
        return String.format(FORMAT, tableNameCn, fieldNameCn);
    }

}

