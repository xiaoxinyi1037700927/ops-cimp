package com.sinosoft.ops.cimp.entity.sheet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * SheetVariable entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SHEET_VARIABLE")

public class SheetVariable implements java.io.Serializable {

    // Fields
    private static final long serialVersionUID = -8113284014034585142L;
    private UUID id;
    private String name;
    private String defaultValue;
    private Byte valueType;
    private String appId;
    private Boolean systemDefined;
    private Integer ordinal;
    private Byte status;
    private String description;
    private Timestamp createdTime;
    private UUID createdBy;
    private Timestamp lastModifiedTime;
    private UUID lastModifiedBy;

    // Constructors

    /** default constructor */
    public SheetVariable() {
    }

    /** minimal constructor */
    public SheetVariable(UUID id, String name, Byte status, Timestamp createdTime, Timestamp lastModifiedTime) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
    }

    /** full constructor */
    public SheetVariable(UUID id, String name, String defaultValue, Byte valueType, String appId,
            Boolean systemDefined, Integer ordinal, Byte status, String description, Timestamp createdTime,
            UUID createdBy, Timestamp lastModifiedTime, UUID lastModifiedBy) {
        this.id = id;
        this.name = name;
        this.defaultValue = defaultValue;
        this.valueType = valueType;
        this.appId = appId;
        this.systemDefined = systemDefined;
        this.ordinal = ordinal;
        this.status = status;
        this.description = description;
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

    @Column(name = "NAME", nullable = false, length = 50)

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "DEFAULT_VALUE")

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Column(name = "VALUE_TYPE", precision = 2, scale = 0)

    public Byte getValueType() {
        return this.valueType;
    }

    public void setValueType(Byte valueType) {
        this.valueType = valueType;
    }

    @Column(name = "APP_ID", length = 36)

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Column(name = "SYSTEM_DEFINED", precision = 1, scale = 0)

    public Boolean getSystemDefined() {
        return this.systemDefined;
    }

    public void setSystemDefined(Boolean systemDefined) {
        this.systemDefined = systemDefined;
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

    @Column(name = "DESCRIPTION")

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
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