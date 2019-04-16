package com.sinosoft.ops.cimp.vo.from.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 删除文件模型
 */
@ApiModel(description = "删除文件模型")
public class DeleteAttachmentViewModel {

    /**
     * 文件路径
     */
    @ApiModelProperty(value = "文件路径")
    private String pathName;
    /**
     * 文件名称
     */
    @ApiModelProperty(value = "文件名称")
    private String fileName;
    /**
     * 业务类型
     */
    @ApiModelProperty(value = "业务类型")
    private String businessType;
    /**
     * 关联业务ID
     */
    @ApiModelProperty(value = "关联业务ID")
    private String businessId;
    /**
     * 方案干部id
     */
    @ApiModelProperty(value = "方案干部id")
    private String projectCadreId;
    /**
     * 文件上传类型(比如: 1 说明文件 2 回函文件)
     */
    @ApiModelProperty(value = "文件上传类型(比如: 1 说明文件 2 回函文件)")
    private String uploadByType;
    /**
     * 是否需要上传
     */
    @ApiModelProperty(value = "是否需要上传")
    private String needStore;
    /**
     * 所属模块
     */
    @ApiModelProperty(value = "所属模块")
    private String moduleCode;

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getUploadByType() {
        return uploadByType;
    }

    public void setUploadByType(String uploadByType) {
        this.uploadByType = uploadByType;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getNeedStore() {
        return needStore;
    }

    public void setNeedStore(String needStore) {
        this.needStore = needStore;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getProjectCadreId() {
        return projectCadreId;
    }

    public void setProjectCadreId(String projectCadreId) {
        this.projectCadreId = projectCadreId;
    }
}
