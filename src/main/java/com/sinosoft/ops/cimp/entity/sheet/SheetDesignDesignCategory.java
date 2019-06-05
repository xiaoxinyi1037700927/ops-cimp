package com.sinosoft.ops.cimp.entity.sheet;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * SheetDesignDesignCategory entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SHEET_DESIGN_DESIGN_CATEGORY", uniqueConstraints = @UniqueConstraint(columnNames = {
        "DESIGN_ID", "CATEGORY_ID" }))
public class SheetDesignDesignCategory implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = -9071916935968005560L;
    private UUID id;
    private UUID designId;
    private UUID categoryId;
    private Byte status;
    private Timestamp createdTime;
    private UUID createdBy;
    private Timestamp lastModifiedTime;
    private UUID lastModifiedBy;

    // Constructors

    /** default constructor */
    public SheetDesignDesignCategory() {
    }

    /** minimal constructor */
    public SheetDesignDesignCategory(UUID id, UUID designId, UUID categoryId, Byte status,
            Timestamp createdTime, Timestamp lastModifiedTime) {
        this.id = id;
        this.designId = designId;
        this.categoryId = categoryId;
        this.status = status;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
    }

    /** full constructor */
    public SheetDesignDesignCategory(UUID id, UUID designId, UUID categoryId, Byte status,
            Timestamp createdTime, UUID createdBy, Timestamp lastModifiedTime, UUID lastModifiedBy) {
        this.id = id;
        this.designId = designId;
        this.categoryId = categoryId;
        this.status = status;
        this.createdTime = createdTime;
        this.createdBy = createdBy;
        this.lastModifiedTime = lastModifiedTime;
        this.lastModifiedBy = lastModifiedBy;
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

    @Column(name = "CATEGORY_ID", nullable = false)
    public UUID getCategoryId() {
        return this.categoryId;
    }
    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
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

}