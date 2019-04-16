package com.sinosoft.ops.cimp.entity.sys.user;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 附件表
 */
@Entity
@Table(name = "ATTACHMENT")
public class Attachment {
    /**
     * 编号标识ID
     */
    private String id;

    /**
     * 关联业务ID
     */
    private String businessId;
    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 模板code
     * */
    private String moduleCode;

    /**
     * 区分是(自己上传 or 编办回函的)
     */
    private String uploadByType;
    /**
     * 文件新名称(UUID名称)
     */
    private String fileName;
    /**
     * 文件路径
     */
    private String filePath;
    /**
     * 文件类型(pdf,doc,docx等等)
     */
    private String fileType;
    /**
     * 文件原始名
     */
    private String originalName;
    /**
     * 文件大小
     */
    private long fileSize;
    /**
     * 文件后缀
     */
    private String fileSuffix;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(length = 50)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(length = 50)
    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    @Column(length = 1)
    public String getUploadByType() {
        return uploadByType;
    }

    public void setUploadByType(String uploadByType) {
        this.uploadByType = uploadByType;
    }

    @Column(length = 100)
    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    @Column(length = 1)
    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Column(length = 200)
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Column(length = 200)
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Column(length = 200)
    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    @Column(length = 10)
    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    @Column(length = 10)
    public String getFileSuffix() {
        return fileSuffix;
    }

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }

    @Column(length = 50)
    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }
}
