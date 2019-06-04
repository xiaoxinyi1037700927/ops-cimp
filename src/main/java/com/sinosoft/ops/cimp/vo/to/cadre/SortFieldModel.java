package com.sinosoft.ops.cimp.vo.to.cadre;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "排序字段模型")
public class SortFieldModel {
    /**
     * 排序名称
     */
    @ApiModelProperty(value = "排序名称")
    private String name;
    /**
     * 排序字段名
     */
    @ApiModelProperty(value = "排序字段名")
    private String fieldName;
    /**
     * 是否是默认排序
     */
    @ApiModelProperty(value = "是否是默认排序")
    private boolean isDefault;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
