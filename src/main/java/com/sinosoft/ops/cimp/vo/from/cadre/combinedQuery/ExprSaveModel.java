package com.sinosoft.ops.cimp.vo.from.cadre.combinedQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description = "保存组合查询模型")
public class ExprSaveModel {
    /**
     * 组合查询id
     */
    @ApiModelProperty(value = "组合查询id")
    private String combinedQueryId;
    /**
     * 组合查询名称
     */
    @ApiModelProperty(value = "组合查询名称")
    private String name;
    /**
     * 表达式字符串
     */
    @ApiModelProperty(value = "表达式字符串")
    private String exprStr;

    public String getCombinedQueryId() {
        return combinedQueryId;
    }

    public void setCombinedQueryId(String combinedQueryId) {
        this.combinedQueryId = combinedQueryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExprStr() {
        return exprStr;
    }

    public void setExprStr(String exprStr) {
        this.exprStr = exprStr;
    }
}
