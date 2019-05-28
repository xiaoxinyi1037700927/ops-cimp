package com.sinosoft.ops.cimp.vo.from.cadre.combinedQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description = "获取函数模型")
public class FunctionSearchModel {
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
     * 新增或编辑
     */
    @ApiModelProperty(value = "0：新增 1：编辑")
    private int addOrUpdate;

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

    public int getAddOrUpdate() {
        return addOrUpdate;
    }

    public void setAddOrUpdate(int addOrUpdate) {
        this.addOrUpdate = addOrUpdate;
    }
}
