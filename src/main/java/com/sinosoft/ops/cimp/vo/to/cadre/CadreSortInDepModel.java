package com.sinosoft.ops.cimp.vo.to.cadre;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "干部在单位内的排序模型")
public class CadreSortInDepModel {
    /**
     * 单位id
     */
    @ApiModelProperty(value = "单位id")
    private String orgId;
    /**
     * 干部id
     */
    @ApiModelProperty(value = "干部id")
    private String empId;
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

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

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
