package com.sinosoft.ops.cimp.vo.from.user.organization;

import com.sinosoft.ops.cimp.vo.base.AttachmentVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * 新增业务部门
 */
@ApiModel(description = "新增业务部门")
public class BusinessUnitAddViewModel {
    /**
     * 业务部门名称
     */
    @ApiModelProperty(value = "业务部门名称", required = true)
    @NotBlank(message = "名称不能为空")
    private String name;

    /**
     * 有效期
     */
    @ApiModelProperty(value = "有效期", required = true)
    private Date validityDate;

    @ApiModelProperty(value = "单位Id的集合", required = true)
    private List<String> organizationIds;

    /**
     * 附件信息
     */
    @ApiModelProperty(value = "附件信息",required = true)
    private List<AttachmentVO> attachmentVOList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getValidityDate() {
        return validityDate;
    }

    public void setValidityDate(Date validityDate) {
        this.validityDate = validityDate;
    }

    public List<String> getOrganizationIds() {
        return organizationIds;
    }

    public void setOrganizationIds(List<String> organizationIds) {
        this.organizationIds = organizationIds;
    }

    public List<AttachmentVO> getAttachmentVOList() {
        return attachmentVOList;
    }

    public void setAttachmentVOList(List<AttachmentVO> attachmentVOList) {
        this.attachmentVOList = attachmentVOList;
    }
}
