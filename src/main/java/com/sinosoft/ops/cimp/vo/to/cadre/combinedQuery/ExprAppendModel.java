package com.sinosoft.ops.cimp.vo.to.cadre.combinedQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;


@ApiModel(description = "添加表达式模型")
public class ExprAppendModel {
    /**
     * 组合查询id
     */
    @ApiModelProperty(value = "组合查询id,为空时新增")
    private String combinedQueryId;
    /**
     * 括号表达式的id
     */
    @ApiModelProperty(value = "括号表达式的id，如果为空或不是括号，添加在最外层")
    private String exprId;
    /**
     * 是否是括号
     */
    @ApiModelProperty(value = "是否是括号 1：是,0:不是")
    private boolean isBrackets;
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
     * 运算符参数
     */
    @ApiModelProperty(value = "运算符参数")
    private List<String> params;

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

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public boolean IsBrackets() {
        return isBrackets;
    }

    public void setBrackets(boolean isBrackets) {
        this.isBrackets = isBrackets;
    }
}
