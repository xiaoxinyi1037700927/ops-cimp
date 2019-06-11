package com.sinosoft.ops.cimp.entity.sheet;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * SheetParameter entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SHEET_PARAMETER")
public class SheetParameter implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = 6286186553206991690L;
    private UUID id;
    private UUID sheetId;
    private String parameterId;
    private String parameterValue;
    private String description;
    private Integer ordinal;
    private Byte status;
    private Timestamp createdTime;
    private UUID createdBy;
    private Timestamp lastModifiedTime;
    private UUID lastModifiedBy;

    private String nameCn;
    private String parameterName;
    private String referenceCodeSet;
    private Byte valueType;

    // Constructors

    /** default constructor */
    public SheetParameter() {
    }

    /** minimal constructor */
    public SheetParameter(UUID id, UUID sheetId, String parameterId, Byte status, Timestamp createdTime,
            Timestamp lastModifiedTime) {
        this.id = id;
        this.sheetId = sheetId;
        this.parameterId = parameterId;
        this.status = status;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
    }

    /** full constructor */
    public SheetParameter(UUID id, UUID sheetId, String parameterId, String parameterValue,
            String description, Integer ordinal, Byte status, Timestamp createdTime, UUID createdBy,
            Timestamp lastModifiedTime, UUID lastModifiedBy) {
        this.id = id;
        this.sheetId = sheetId;
        this.parameterId = parameterId;
        this.parameterValue = parameterValue;
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

    @Column(name = "SHEET_ID", nullable = false)
    public UUID getSheetId() {
        return this.sheetId;
    }
    public void setSheetId(UUID sheetId) {
        this.sheetId = sheetId;
    }

    @Column(name = "PARAMETER_ID", nullable = false, length = 30)
    public String getParameterId() {
        return this.parameterId;
    }
    public void setParameterId(String parameterId) {
        this.parameterId = parameterId;
    }

    @Column(name = "PARAMETER_VALUE")
    public String getParameterValue() {
        return this.parameterValue;
    }
    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
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

    @Transient
    public String getParameterName() {
        return this.parameterName;
    }
    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    @Transient
    public String getReferenceCodeSet() {
        return this.referenceCodeSet;
    }
    public void setReferenceCodeSet(String referenceCodeSet) {
        this.referenceCodeSet = referenceCodeSet;
    }

    @Transient
    public String getNameCn() {
        return this.nameCn;
    }
    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    @Transient
    public Byte getValueType() {
        return this.valueType;
    }
    public void setValueType(Byte valueType) {
        this.valueType = valueType;
    }

}