package com.sinosoft.ops.cimp.vo.from.cadre;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "排序模型")
public class SortModel {
    /**
     * 排序字段名
     */
    @ApiModelProperty(value = "排序字段名")
    private String name;

    /**
     * 是否是倒序
     */
    @ApiModelProperty(value = "0:正序，1：倒序")
    private int isDesc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIsDesc() {
        return isDesc;
    }

    public void setIsDesc(int isDesc) {
        this.isDesc = isDesc;
    }
}

