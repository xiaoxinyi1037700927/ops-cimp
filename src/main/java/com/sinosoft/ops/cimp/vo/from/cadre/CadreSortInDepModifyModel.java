package com.sinosoft.ops.cimp.vo.from.cadre;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "修改干部在单位内排序模型")
public class CadreSortInDepModifyModel {
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

    public String getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(String sortNumber) {
        this.sortNumber = sortNumber;
    }
}
