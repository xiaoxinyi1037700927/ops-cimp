package com.sinosoft.ops.cimp.vo.to.cadre.combinedQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description = "修改表达式模型")
public class ExprStrModifyModel {
    /**
     * 组合查询id
     */
    @ApiModelProperty(value = "组合查询id")
    private String combinedQueryId;
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

    public String getExprStr() {
        return exprStr;
    }

    public void setExprStr(String exprStr) {
        this.exprStr = exprStr;
    }
}
