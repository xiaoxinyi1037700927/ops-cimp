package com.sinosoft.ops.cimp.vo.from.user.organization;


import com.sinosoft.ops.cimp.vo.base.AttachmentVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * 移入/移出业务部门实体类
 */
@ApiModel(description = "移入/移出业务部门实体类")
public class BusinessUnitOrgChangeViewModel {
    /**
     * 业务部门Id
     */
    @ApiModelProperty(value = "业务部门Id", required = true)
    @NotBlank(message = "businessUnitId不能为空")
    private String businessUnitId;

    /**
     * 业务部门名称
     */
    @ApiModelProperty(value = "业务部门名称", required = true)
    @NotBlank(message = "businessUnitName不能为空")
    private String businessUnitName;

    /**
     * 类型 1:移入  2:移出
     */
    @ApiModelProperty(value = "类型 1:移入  2:移出", required = true)
    @NotBlank(message = "类型不能为空")
    private String type;

    @ApiModelProperty(value = "单位Id的集合", required = true)
    private List<String> organizationIds;

    /**
     * 有效期
     */
    @ApiModelProperty(value = "有效期", required = true)
    private Date validityDate;

    /**
     * 附件信息
     */
    @ApiModelProperty(value = "附件信息",required = true)
    private List<AttachmentVO> attachmentVOList;

    public String getBusinessUnitId() {
        return businessUnitId;
    }

    public String getBusinessUnitName() {
        return businessUnitName;
    }

    public void setBusinessUnitName(String businessUnitName) {
        this.businessUnitName = businessUnitName;
    }

    public void setBusinessUnitId(String businessUnitId) {
        this.businessUnitId = businessUnitId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getOrganizationIds() {
        return organizationIds;
    }

    public void setOrganizationIds(List<String> organizationIds) {
        this.organizationIds = organizationIds;
    }

    public Date getValidityDate() {
        return validityDate;
    }

    public void setValidityDate(Date validityDate) {
        this.validityDate = validityDate;
    }

    public List<AttachmentVO> getAttachmentVOList() {
        return attachmentVOList;
    }

    public void setAttachmentVOList(List<AttachmentVO> attachmentVOList) {
        this.attachmentVOList = attachmentVOList;
    }
}
