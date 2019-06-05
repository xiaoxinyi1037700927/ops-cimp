package com.sinosoft.ops.cimp.entity.sheet;

import com.sinosoft.ops.cimp.common.model.DefaultTreeNode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * SheetDesignCategory entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SHEET_DESIGN_CATEGORY")

public class SheetDesignCategory extends DefaultTreeNode implements java.io.Serializable {

    // Fields
    private static final long serialVersionUID = 8264365546054251003L;
    private UUID id;
    private String name;
    private UUID parentId;
    private Byte type;
    private Byte scope;
    private Integer appId;
    private String infoCategoryId;
    private Boolean systemDefined;
    private String description;
    private Integer ordinal;
    private Byte status;
    private Timestamp createdTime;
    private UUID createdBy;
    private Timestamp lastModifiedTime;
    private UUID lastModifiedBy;

    // Constructors

    /** default constructor */
    public SheetDesignCategory() {
    }

    /** minimal constructor */
    public SheetDesignCategory(UUID id, String name, Byte type, Byte status, Timestamp createdTime,
            Timestamp lastModifiedTime) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.status = status;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
    }

    /** full constructor */
    public SheetDesignCategory(UUID id, String name, UUID parentId, Byte type, Byte scope, Integer appId,
            String infoCategoryId, Boolean systemDefined, String description, Integer ordinal, Byte status,
            Timestamp createdTime, UUID createdBy, Timestamp lastModifiedTime, UUID lastModifiedBy) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.type = type;
        this.scope = scope;
        this.appId = appId;
        this.infoCategoryId = infoCategoryId;
        this.systemDefined = systemDefined;
        this.description = description;
        this.ordinal = ordinal;
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

    @Column(name = "NAME", nullable = false)

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "PARENT_ID")

    public UUID getParentId() {
        return this.parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    @Column(name = "TYPE", nullable = false, precision = 2, scale = 0)

    public Byte getType() {
        return this.type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    @Column(name = "SCOPE", precision = 2, scale = 0)
    public Byte getScope() {
        return this.scope;
    }
    public void setScope(Byte scope) {
        this.scope = scope;
    }

    @Column(name = "APP_ID", precision = 8, scale = 0)

    public Integer getAppId() {
        return this.appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    @Column(name = "INFO_CATEGORY_ID", length = 20)

    public String getInfoCategoryId() {
        return this.infoCategoryId;
    }

    public void setInfoCategoryId(String infoCategoryId) {
        this.infoCategoryId = infoCategoryId;
    }

    @Column(name = "SYSTEM_DEFINED", precision = 1, scale = 0)

    public Boolean getSystemDefined() {
        return this.systemDefined;
    }

    public void setSystemDefined(Boolean systemDefined) {
        this.systemDefined = systemDefined;
    }

    @Column(name = "DESCRIPTION")

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
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

}