package com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes;


/**
 * 字段节点
 */
public class FieldNode extends Node {
    private static final String FORMAT = "%s.%s";

    private String tableName;
    private String tableNameCn;
    private String fieldName;
    private String fieldNameCn;
    private String codeSetName;
    private int returnType;

    public FieldNode(String tableName, String tableNameCn, String fieldName, String fieldNameCn, int returnType, String codeSetName) {
        super(true, new int[]{});
        this.tableName = tableName;
        this.tableNameCn = tableNameCn;
        this.fieldName = fieldName;
        this.fieldNameCn = fieldNameCn;
        this.returnType = returnType;
        this.codeSetName = codeSetName;
    }

    /**
     * 获取节点的返回类型
     *
     * @return
     */
    @Override
    public int getReturnType() {
        return returnType;
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


    public String getCodeSetName() {
        return codeSetName;
    }
}

