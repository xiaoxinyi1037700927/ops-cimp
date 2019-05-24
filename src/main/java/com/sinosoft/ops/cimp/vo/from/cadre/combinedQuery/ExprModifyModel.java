package com.sinosoft.ops.cimp.vo.from.cadre.combinedQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description = "修改表达式模型")
public class ExprModifyModel {
    /**
     * 组合查询id
     */
    @ApiModelProperty(value = "组合查询id")
    private String combinedQueryId;
    /**
     * 表达式的id
     */
    @ApiModelProperty(value = "表达式的id")
    private String exprId;
    /**
     * 参数id
     */
    @ApiModelProperty(value = "参数id")
    private String paramId;
    /**
     * 逻辑操作符
     */
    @ApiModelProperty(value = "逻辑操作符")
    private String logicalOperator;
    /**
     * 运算符
     */
    @ApiModelProperty(value = "运算符")
    private String operator;
    /**
     * 函数名
     */
    @ApiModelProperty(value = "函数名")
    private String functionName;
    /**
     * 值
     */
    @ApiModelProperty(value = "值")
    private String value;

    public String getCombinedQueryId() {
        return combinedQueryId;
    }

    public void setCombinedQueryId(String combinedQueryId) {
        this.combinedQueryId = combinedQueryId;
    }

    public String getExprId() {
        return exprId;
    }

    public void setExprId(String exprId) {
        this.exprId = exprId;
    }

    public String getParamId() {
        return paramId;
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

    public String getLogicalOperator() {
        return logicalOperator;
    }

    public void setLogicalOperator(String logicalOperator) {
        this.logicalOperator = logicalOperator;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
