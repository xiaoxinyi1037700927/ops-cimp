package com.sinosoft.ops.cimp.entity.sheet;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * SheetDesignParameter entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SHEET_DESIGN_PARAMETER")

public class SheetDesignParameter implements java.io.Serializable{

    // Fields
    private static final long serialVersionUID = -8456927956476581280L;
    private UUID id;
    private UUID designId;
    private String parameterId;
    private String parameterValue;
    private Byte parameterValueType;
    private Integer endColumnNo;
    private Integer endRowNo;
    private Integer startColumnNo;
    private Integer startRowNo;
    private Integer ordinal;
    private Byte status;
    private Timestamp createdTime;
    private UUID createdBy;
    private Timestamp lastModifiedTime;
    private UUID lastModifiedBy;

    private String parameterName;
    private String referenceCodeSet;

    // Constructors

    /** default constructor */
    public SheetDesignParameter() {
    }

    /** minimal constructor */
    public SheetDesignParameter(UUID id, UUID designId, String parameterId, Byte status, Timestamp createdTime,
            Timestamp lastModifiedTime) {
        this.id = id;
        this.designId = designId;
        this.parameterId = parameterId;
        this.status = status;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
    }

    /** full constructor */
    public SheetDesignParameter(UUID id, UUID designId, String parameterId, String parameterValue,
            Byte parameterValueType, Integer endColumnNo, Integer endRowNo, Integer startColumnNo, Integer startRowNo,
            Integer ordinal, Byte status, Timestamp createdTime, UUID createdBy, Timestamp lastModifiedTime,
            UUID lastModifiedBy,String parameterName,String referenceCodeSet) {
        this.id = id;
        this.designId = designId;
        this.parameterName=parameterName;
        this.referenceCodeSet=referenceCodeSet;
        this.parameterId = parameterId;
        this.parameterValue = parameterValue;
        this.parameterValueType = parameterValueType;
        this.endColumnNo = endColumnNo;
        this.endRowNo = endRowNo;
        this.startColumnNo = startColumnNo;
        this.startRowNo = startRowNo;
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

    @Column(name = "DESIGN_ID", nullable = false)

    public UUID getDesignId() {
        return this.designId;
    }

    public void setDesignId(UUID designId) {
        this.designId = designId;
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

    @Column(name = "END_COLUMN_NO", precision = 8, scale = 0)

    public Integer getEndColumnNo() {
        return this.endColumnNo;
    }

    public void setEndColumnNo(Integer endColumnNo) {
        this.endColumnNo = endColumnNo;
    }

    @Column(name = "END_ROW_NO", precision = 8, scale = 0)

    public Integer getEndRowNo() {
        return this.endRowNo;
    }

    public void setEndRowNo(Integer endRowNo) {
        this.endRowNo = endRowNo;
    }

    @Column(name = "START_COLUMN_NO", precision = 8, scale = 0)

    public Integer getStartColumnNo() {
        return this.startColumnNo;
    }

    public void setStartColumnNo(Integer startColumnNo) {
        this.startColumnNo = startColumnNo;
    }

    @Column(name = "START_ROW_NO", precision = 8, scale = 0)

    public Integer getStartRowNo() {
        return this.startRowNo;
    }

    public void setStartRowNo(Integer startRowNo) {
        this.startRowNo = startRowNo;
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

}