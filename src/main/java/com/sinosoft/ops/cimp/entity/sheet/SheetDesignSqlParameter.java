package com.sinosoft.ops.cimp.entity.sheet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * SheetDesignSqlParameter entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SHEET_DESIGN_SQL_PARAMETER")

public class SheetDesignSqlParameter implements java.io.Serializable {

    // Fields
    private static final long serialVersionUID = -6363484446766687486L;
    private UUID id;
    private UUID designSqlId;
    private String parameterId;
    private String parameterValue;
    private Byte parameterValueType;
    private Integer ordinal;
    private Byte status;
    private Timestamp createdTime;
    private UUID createdBy;
    private Timestamp lastModifiedTime;
    private UUID lastModifiedBy;
    private String parameterName;

    // Constructors

    /** default constructor */
    public SheetDesignSqlParameter() {
    }

    /** minimal constructor */
    public SheetDesignSqlParameter(UUID id, UUID designSqlId, String parameterId, Byte status,
            Timestamp createdTime, Timestamp lastModifiedTime, String parameterName) {
        this.id = id;
        this.designSqlId = designSqlId;
        this.parameterId = parameterId;
        this.status = status;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
        this.parameterName = parameterName;
    }

    /** full constructor */
    public SheetDesignSqlParameter(UUID id, UUID designSqlId, String parameterId, String parameterValue,
            Byte parameterValueType, Integer ordinal, Byte status, Timestamp createdTime, UUID createdBy,
            Timestamp lastModifiedTime, UUID lastModifiedBy, String parameterName) {
        this.id = id;
        this.designSqlId = designSqlId;
        this.parameterId = parameterId;
        this.parameterValue = parameterValue;
        this.parameterValueType = parameterValueType;
        this.ordinal = ordinal;
        this.status = status;
        this.createdTime = createdTime;
        this.createdBy = createdBy;
        this.lastModifiedTime = lastModifiedTime;
        this.lastModifiedBy = lastModifiedBy;
        this.parameterName = parameterName;
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

    @Column(name = "DESIGN_SQL_ID", nullable = false)

    public UUID getDesignSqlId() {
        return this.designSqlId;
    }

    public void setDesignSqlId(UUID designSqlId) {
        this.designSqlId = designSqlId;
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

    @Column(name = "PARAMETER_VALUE_TYPE", precision = 2, scale = 0)

    public Byte getParameterValueType() {
        return this.parameterValueType;
    }

    public void setParameterValueType(Byte parameterValueType) {
        this.parameterValueType = parameterValueType;
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

    @Column(name = "PARAMETER_NAME", nullable = false, length = 50)

    public String getParameterName() {
        return this.parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

}