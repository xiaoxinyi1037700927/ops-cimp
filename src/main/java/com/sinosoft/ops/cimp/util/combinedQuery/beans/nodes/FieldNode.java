package com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes;


/**
 * 字段节点
 */
public class FieldNode extends Node {
    public static final int CODE = 1 << 4;
    public static final int SUPPORT_NODES = NONE;
    private static final String SQL = " %s.%s ";

    private String tableName;
    private String fieldName;
    private String fieldType;

    public FieldNode(String expr, String tableName, String fieldName, String fieldType) {
        super(expr, true, 0, SUPPORT_NODES, CODE);
        this.tableName = tableName;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }

    /**
     * 获取节点对应的sql
     *
     * @return
     */
    @Override
    public String getSql() {
        return String.format(SQL, tableName, fieldName);
    }

}

