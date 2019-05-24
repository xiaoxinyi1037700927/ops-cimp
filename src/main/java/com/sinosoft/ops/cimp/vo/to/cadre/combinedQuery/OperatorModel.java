package com.sinosoft.ops.cimp.vo.to.cadre.combinedQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "运算符模型")
public class OperatorModel {
    /**
     * 运算符名称
     */
    @ApiModelProperty(value = "运算符名称")
    private String name;
    /**
     * 参数类型
     */
    @ApiModelProperty(value = "参数类型")
    private int[] paramsType;
    /**
     * 参数数量
     */
    @ApiModelProperty(value = "参数数量")
    private int paramsNum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getParamsType() {
        return paramsType;
    }

    public void setParamsType(int[] paramsType) {
        this.paramsType = paramsType;
    }

    public int getParamsNum() {
        return paramsNum;
    }

    public void setParamsNum(int paramsNum) {
        this.paramsNum = paramsNum;
    }
}
