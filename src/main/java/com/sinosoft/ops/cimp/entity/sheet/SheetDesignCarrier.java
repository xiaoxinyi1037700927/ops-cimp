package com.sinosoft.ops.cimp.entity.sheet;

import com.sinosoft.ops.cimp.common.model.Trackable;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * @classname:  SheetDesignCarrier
 * @description: 表格设计载体
 * @author:        Nil
 * @date:            2017年10月22日 下午5:47:47
 * @version        1.0.0
 */
@Entity
@Table(name = "SHEET_DESIGN_CARRIER")
public class SheetDesignCarrier implements java.io.Serializable,Trackable {
    private static final long serialVersionUID = 8990810551165026642L;
    // Fields
    /*** 标识 */
    private UUID id;
    /*** 表格设计标识 */
    private UUID designId;
    /*** 类型 */
    private Byte type;
    /*** 内容 */
    private byte[] content;
    /*** 内容长度 */
    private Long contentLength;
    /*** 内容摘要 */
    private String contentDigest;
    /*** 次序 */
    private Integer ordinal;
    /*** 状态 */
    private Byte status;
    /*** 创建时间 */
    private Timestamp createdTime;
    /*** 创建人 */
    private UUID createdBy;
    /*** 最后修改时间 */
    private Timestamp lastModifiedTime;
    /*** 最后修改人 */
    private UUID lastModifiedBy;
    /*** 文件名 */
    private String fileName;

    // Constructors

    /** default constructor */
    public SheetDesignCarrier() {
    }

    /** minimal constructor */
    public SheetDesignCarrier(UUID id, UUID designId, Byte type, Byte status, Timestamp createdTime,
            Timestamp lastModifiedTime) {
        this.id = id;
        this.designId = designId;
        this.type = type;
        this.status = status;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
    }

    /** full constructor */
    public SheetDesignCarrier(UUID id, UUID designId, Byte type, byte[] content,Long contentLength, String contentDigest, Integer ordinal, Byte status,
            Timestamp createdTime, UUID createdBy, Timestamp lastModifiedTime, UUID lastModifiedBy, String fileName) {
        this.id = id;
        this.designId = designId;
        this.type = type;
        this.content = content;
        this.contentLength = contentLength;
        this.contentDigest = contentDigest;
        this.ordinal = ordinal;
        this.status = status;
        this.createdTime = createdTime;
        this.createdBy = createdBy;
        this.lastModifiedTime = lastModifiedTime;
        this.lastModifiedBy = lastModifiedBy;
        this.fileName = fileName;
    }

    // Property accessors
    @Id
    @Column(name = "ID", unique = true, nullable = false)
    public UUID getId() {
        return this.id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    @Column(name = "DESIGN_ID", nullable = false)
    public UUID getDesignId() {
        return this.designId;
    }
    public void setDesignId(UUID designId) {
        this.designId = designId;
    }

    @Column(name = "TYPE", nullable = false, precision = 2, scale = 0)
    public Byte getType() {
        return this.type;
    }
    public void setType(Byte type) {
        this.type = type;
    }

    @Lob
    @Column(name = "CONTENT")
    public byte[] getContent() {
        return this.content;
    }
    public void setContent(byte[] content) {
        this.content = content;
    }

    @Column(name = "CONTENT_LENGTH", precision = 18, scale = 0)
    public Long getContentLength() {
        return contentLength;
    }
    public void setContentLength(Long contentLength) {
        this.contentLength = contentLength;
    }

    @Column(name = "CONTENT_DIGEST")
    public String getContentDigest() {
        return contentDigest;
    }
    public void setContentDigest(String contentDigest) {
        this.contentDigest = contentDigest;
    }

    @Column(name = "ORDINAL", precision = 8, scale = 0)
    public Integer getOrdinal() {
        return this.ordinal;
    }
    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    @Column(name = "STATUS", nullable = false, precision = 2, scale = 0)
    public Byte getStatus() {
        return this.status;
    }
    public void setStatus(Byte status) {
        this.status = status;
    }

    @Column(name = "CREATED_TIME", nullable = false, length = 11)
    public Timestamp getCreatedTime() {
        return this.createdTime;
    }
    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    @Column(name = "CREATED_BY")
    public UUID getCreatedBy() {
        return this.createdBy;
    }
    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name = "LAST_MODIFIED_TIME", nullable = false, length = 11)
    public Timestamp getLastModifiedTime() {
        return this.lastModifiedTime;
    }
    public void setLastModifiedTime(Timestamp lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    @Column(name = "LAST_MODIFIED_BY")
    public UUID getLastModifiedBy() {
        return this.lastModifiedBy;
    }
    public void setLastModifiedBy(UUID lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Column(name = "FILE_NAME")
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
    
}