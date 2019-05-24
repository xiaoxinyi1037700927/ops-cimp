package com.sinosoft.ops.cimp.vo.to.cadre.combinedQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(description = "函数模型")
public class FunctionModel {
    /**
     * 函数名称
     */
    @ApiModelProperty(value = "函数名称")
    private String name;
    /**
     * 返回类型
     */
    @ApiModelProperty(value = "返回类型")
    private TypeModel returnType;
    /**
     * 参数类型
     */
    @ApiModelProperty(value = "参数类型")
    private List<TypeModel> paramsType;
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

    public TypeModel getReturnType() {
        return returnType;
    }

    public void setReturnType(TypeModel returnType) {
        this.returnType = returnType;
    }

    public List<TypeModel> getParamsType() {
        return paramsType;
    }

    public void setParamsType(List<TypeModel> paramsType) {
        this.paramsType = paramsType;
    }

    public int getParamsNum() {
        return paramsNum;
    }

    public void setParamsNum(int paramsNum) {
        this.paramsNum = paramsNum;
    }
}
