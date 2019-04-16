package com.sinosoft.ops.cimp.vo.user.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 附件返回信息
 */
@ApiModel(description = "附件返回信息")
public class AttachmentViewModel {
    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private String id;

    /**
     * 业务ID
     */
    @ApiModelProperty(value = "业务ID")
    private String businessId;
    /**
     * 文件类型
     */
    @ApiModelProperty(value = "文件类型")
    private String fileType;
    /**
     * 文件UUID名
     */
    @ApiModelProperty(value = "文件UUID名")
    private String fileName;
    /**
     * 文件路径
     */
    @ApiModelProperty(value = "文件路径")
    private String filePath;
    /**
     * 文件原名
     */
    @ApiModelProperty(value = "文件原名")
    private String originalName;

    /**
     * 区分是(自己上传 or 编办回函的)
     */
    @ApiModelProperty(value = "区分是(自己上传 or 编办回函的)")
    private String uploadByType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getUploadByType() {
        return uploadByType;
    }

    public void setUploadByType(String uploadByType) {
        this.uploadByType = uploadByType;
    }
}
