package com.sinosoft.ops.cimp.vo.from.user.organization;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 根据业务ID查询上传文件模型
 */
@ApiModel(description = "根据业务ID查询上传文件模型")
public class SearchAttachmentViewModel {

    /**
     * 业务ID
     */
    @ApiModelProperty(value = "业务ID")
    private String businessId;
    /**
     * 相关部门ID
     */
    @ApiModelProperty(value = "相关部门ID")
    private String relatedDepartmentId;
    /**
     * 业务类别
     */
    @ApiModelProperty(value = "业务类别")
    private String businessType;
    /**
     * 标识同一业务ID的文件,被那些部门上传的
     */
    @ApiModelProperty(value = "标识同一业务ID的文件,被那些部门上传的")
    private String uploadByType;
    /**
     * 方案干部ID
     */
    @ApiModelProperty(value = "方案干部ID")
    private String projectCadreInfoId;

    /**
     * 所属页面
     */
    @ApiModelProperty(value = "所属页面")
    private String moduleType;

    /**
     * 所属模块
     */
    @ApiModelProperty(value = "所属模块")
    private String moduleCode;

    /**
     * 附件代码
     */
    @ApiModelProperty(value = "附件代码")
    private String attachmentModuleCode;


    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getRelatedDepartmentId() {
        return relatedDepartmentId;
    }

    public void setRelatedDepartmentId(String relatedDepartmentId) {
        this.relatedDepartmentId = relatedDepartmentId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getUploadByType() {
        return uploadByType;
    }

    public void setUploadByType(String uploadByType) {
        this.uploadByType = uploadByType;
    }

    public String getProjectCadreInfoId() {
        return projectCadreInfoId;
    }

    public void setProjectCadreInfoId(String projectCadreInfoId) {
        this.projectCadreInfoId = projectCadreInfoId;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getAttachmentModuleCode() {
        return attachmentModuleCode;
    }

    public void setAttachmentModuleCode(String attachmentModuleCode) {
        this.attachmentModuleCode = attachmentModuleCode;
    }
}
