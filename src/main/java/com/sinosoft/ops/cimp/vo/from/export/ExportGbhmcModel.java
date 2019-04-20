package com.sinosoft.ops.cimp.vo.from.export;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "导出干部花名册模型")
public class ExportGbhmcModel {
    /**
     * 单位id
     */
    @ApiModelProperty(value = "单位id")
    private String orgId;
    /**
     * 是否包含下级
     */
    @ApiModelProperty(value = "是否包含下级")
    private boolean containChild;
    /**
     * 干部id列表(以','分隔)
     */
    @ApiModelProperty(value = "干部id列表(以','分隔)")
    private String empIds;
    /**
     * 是否包含单位
     */
    @ApiModelProperty(value = "是否包含单位")
    private boolean containDepartment;
    /**
     * 是否包含身份证号
     */
    @ApiModelProperty(value = "是否包含身份证号")
    private boolean containIdCardNo;
    /**
     * 职务关联单位
     */
    @ApiModelProperty(value = "职务关联单位")
    private boolean containDutyDepart;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public boolean getContainChild() {
        return containChild;
    }

    public void setContainChild(boolean containChild) {
        this.containChild = containChild;
    }

    public String getEmpIds() {
        return empIds;
    }

    public void setEmpIds(String empIds) {
        this.empIds = empIds;
    }

    public boolean getContainDepartment() {
        return containDepartment;
    }

    public void setContainDepartment(boolean containDepartment) {
        this.containDepartment = containDepartment;
    }

    public boolean getContainIdCardNo() {
        return containIdCardNo;
    }

    public void setContainIdCardNo(boolean containIdCardNo) {
        this.containIdCardNo = containIdCardNo;
    }

    public boolean getContainDutyDepart() {
        return containDutyDepart;
    }

    public void setContainDutyDepart(boolean containDutyDepart) {
        this.containDutyDepart = containDutyDepart;
    }
}
