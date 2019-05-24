package com.sinosoft.ops.cimp.vo.from.cadre.combinedQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description = "添加函数模型")
public class FunctionAppendModel {
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
     * 函数名
     */
    @ApiModelProperty(value = "函数名")
    private String functionName;

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

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }
}
