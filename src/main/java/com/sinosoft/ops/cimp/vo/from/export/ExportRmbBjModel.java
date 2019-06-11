package com.sinosoft.ops.cimp.vo.from.export;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "导出任免表模型")
public class ExportRmbBjModel {
    /**
     * 干部id
     */
    @ApiModelProperty(value = "干部id")
    private String empId;
    /**
     * 拟任职务
     */
    @ApiModelProperty(value = "拟任职务")
    private String proposedPost;
    /**
     * 拟免职务
     */
    @ApiModelProperty(value = "拟免职务")
    private String proposedRemoval;
    /**
     * 呈报单位
     */
    @ApiModelProperty(value = "呈报单位")
    private String reportingUnit;
    /**
     * 审批机关意见
     */
    @ApiModelProperty(value = "干部id")
    private String approvalOptions;
    /**
     * 行政机关任免意见
     */
    @ApiModelProperty(value = "行政机关任免意见")
    private String appointmentOptions;

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getProposedPost() {
        return proposedPost;
    }

    public void setProposedPost(String proposedPost) {
        this.proposedPost = proposedPost;
    }

    public String getProposedRemoval() {
        return proposedRemoval;
    }

    public void setProposedRemoval(String proposedRemoval) {
        this.proposedRemoval = proposedRemoval;
    }

    public String getReportingUnit() {
        return reportingUnit;
    }

    public void setReportingUnit(String reportingUnit) {
        this.reportingUnit = reportingUnit;
    }

    public String getApprovalOptions() {
        return approvalOptions;
    }

    public void setApprovalOptions(String approvalOptions) {
        this.approvalOptions = approvalOptions;
    }

    public String getAppointmentOptions() {
        return appointmentOptions;
    }

    public void setAppointmentOptions(String appointmentOptions) {
        this.appointmentOptions = appointmentOptions;
    }
}
