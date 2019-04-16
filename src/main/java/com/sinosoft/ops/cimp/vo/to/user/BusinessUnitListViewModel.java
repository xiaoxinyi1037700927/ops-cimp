package com.sinosoft.ops.cimp.vo.to.user;

import com.sinosoft.ops.cimp.vo.user.organization.BusinessUnitOrgListViewModel;
import com.sinosoft.ops.cimp.vo.user.role.AttachmentViewModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * 业务部门列表
 */
@ApiModel(description = "业务部门列表")
public class BusinessUnitListViewModel {
    /**
     * 业务部门Id
     */
    @ApiModelProperty(value = "业务部门Id")
    private String businessUnitId;
    /**
     * 业务部门名称
     */
    @ApiModelProperty(value = "业务部门名称")
    private String businessUnitName;

    /**
     * 关联单位列表
     */
    @ApiModelProperty(value = "关联单位列表")
    private List<BusinessUnitOrgListViewModel> businessUnitOrgListViewModelList;

    /**
     * 关联附件
     */
    @ApiModelProperty(value = "关联附件")
    private List<AttachmentViewModel> attachmentViewModelList;

    /**
     * 有效期
     */
    @ApiModelProperty(value = "有效期", required = true)
    private Date validityDate;

    public String getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(String businessUnitId) {
        this.businessUnitId = businessUnitId;
    }

    public String getBusinessUnitName() {
        return businessUnitName;
    }

    public void setBusinessUnitName(String businessUnitName) {
        this.businessUnitName = businessUnitName;
    }

    public List<BusinessUnitOrgListViewModel> getBusinessUnitOrgListViewModelList() {
        return businessUnitOrgListViewModelList;
    }

    public void setBusinessUnitOrgListViewModelList(List<BusinessUnitOrgListViewModel> businessUnitOrgListViewModelList) {
        this.businessUnitOrgListViewModelList = businessUnitOrgListViewModelList;
    }

    public List<AttachmentViewModel> getAttachmentViewModelList() {
        return attachmentViewModelList;
    }

    public void setAttachmentViewModelList(List<AttachmentViewModel> attachmentViewModelList) {
        this.attachmentViewModelList = attachmentViewModelList;
    }

    public Date getValidityDate() {
        return validityDate;
    }

    public void setValidityDate(Date validityDate) {
        this.validityDate = validityDate;
    }
}
