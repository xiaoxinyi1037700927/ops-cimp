package com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes;


import com.sinosoft.ops.cimp.util.combinedQuery.enums.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 值节点
 */
public class ValueNode extends Node {

    private List<String> values;
    private List<String> codeNames;
    private boolean isCode;
    private boolean isArray;
    private int returnType;
    private String codeSetName;

    public ValueNode(List<String> values, List<String> codeNames, boolean isCode, boolean isArray, int returnType) {
        super(true, new int[]{});
        this.values = values;
        this.codeNames = codeNames;
        this.isCode = isCode;
        this.isArray = isArray;
        this.returnType = returnType;
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
     */
    @Override
    public String getSql() {
        if ((Type.NUMBER.getCode() & returnType) != 0) {
            return values.stream().collect(Collectors.joining(","));
        } else {
            return values.stream().collect(Collectors.joining("','", "'", "'"));
        }
    }

    /**
     * 获取节点对应的表达式
     */
    @Override
    public String getExpr() {
        if (isCode) {
            List<String> codeStr = new ArrayList<>();
            for (int i = 0; i < values.size(); i++) {
                codeStr.add("[" + values.get(i) + "]" + codeNames.get(i));
            }
            return codeStr.stream().collect(Collectors.joining("','", "'", "'"));
        } else {
            return values.stream().collect(Collectors.joining("','", "'", "'"));
        }
    }

    public void setReturnType(int returnType) {
        this.returnType = returnType;
    }

    public boolean isCode() {
        return isCode;
    }

    public List<String> getValues() {
        return values;
    }

    public List<String> getCodeNames() {
        return codeNames;
    }

    public boolean isArray() {
        return isArray;
    }

    public String getCodeSetName() {
        return codeSetName;
    }

    public void setCodeSetName(String codeSetName) {
        this.codeSetName = codeSetName;
    }
}
