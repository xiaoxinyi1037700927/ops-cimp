package com.sinosoft.ops.cimp.vo.to.organization;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "干部在单位内的排序模型")
public class EmpSortInDepModel {
    /**
     * 干部名称
     */
    @ApiModelProperty(value = "干部名称")
    private String name;
    /**
     * 职务名称
     */
    @ApiModelProperty(value = "职务名称")
    private String positionName;
    /**
     * 排序号
     */
    @ApiModelProperty(value = "排序号")
    private String sortNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(String sortNumber) {
        this.sortNumber = sortNumber;
    }
}
