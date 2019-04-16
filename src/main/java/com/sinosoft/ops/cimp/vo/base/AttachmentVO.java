package com.sinosoft.ops.cimp.vo.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 附件信息VO createBy skz 20180613
 */
@ApiModel(description = "附件信息VO createBy skz 20180613")
public class AttachmentVO {
    /**
     * 文件id,保存时候用GUID替换文件名
     */
    @ApiModelProperty(value = "文件id,保存时候用GUID替换文件名")
    private String fileName;
    /**
     * 文件实际路径
     */
    @ApiModelProperty(value = "文件实际路径")
    private String filePath;
    /**
     * 文件实际名称
     */
    @ApiModelProperty(value = "文件实际名称")
    private String originalName;
    /**
     * 文件上传是否成功
     */
    @ApiModelProperty(value = "文件上传是否成功")
    private boolean booleanUpload;
    /**
     * 文件上传返回信息
     * 是否成功，错误信息等
     */
    @ApiModelProperty(value = "文件上传返回信息       是否成功，错误信息等")
    private String uploadMessage;

    /**
     * 区分是(自己上传 or 编办回函的)
     */
    @ApiModelProperty(value = "区分是(自己上传 or 编办回函的)")
    private String uploadByType;
    /**
     * 业务类型
     */
    @ApiModelProperty(value = "业务类型")
    private String businessType;

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

    public boolean isBooleanUpload() {
        return booleanUpload;
    }

    public void setBooleanUpload(boolean booleanUpload) {
        this.booleanUpload = booleanUpload;
    }

    public String getUploadMessage() {
        return uploadMessage;
    }

    public void setUploadMessage(String uploadMessage) {
        this.uploadMessage = uploadMessage;
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
}
