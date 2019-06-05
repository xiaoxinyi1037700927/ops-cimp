package com.sinosoft.ops.cimp.entity.sheet;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * SheetDesignVariable entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SHEET_DESIGN_VARIABLE")

public class SheetDesignVariable implements java.io.Serializable {

    // Fields
    private static final long serialVersionUID = 698213100611697164L;
    private UUID id;
    private UUID designId;
    private String variableValue;//not null
    private Byte variableValueType;//not null
    private Integer startColumnNo;
    private Integer startRowNo;
    private Integer endRowNo;
    private Integer endColumnNo;
    private String description;//not null
    private Integer ordinal;//not null
    private Byte status;
    private Timestamp  createdTime;
    private UUID createdBy;//not null
    private Timestamp lastModifiedTime;
    private UUID lastModifiedBy;//not null
    private String variableName;
    private Byte variableType;
    private String dataSourceId;
    private String conditionId;
    private String variableValueCaption;
    private String[] dataSourceName;
    private String[] conditionName;
    

    // Constructors

    /** default constructor */
    public SheetDesignVariable() {
    }

    /** minimal constructor */
    public SheetDesignVariable(UUID id, UUID designId, Integer startColumnNo, Integer startRowNo,
            Integer endRowNo, Integer endColumnNo, Byte status, Timestamp createdTime, Timestamp lastModifiedTime) {
        this.id = id;
        this.designId = designId;
        this.startColumnNo = startColumnNo;
        this.startRowNo = startRowNo;
        this.endRowNo = endRowNo;
        this.endColumnNo = endColumnNo;
        this.status = status;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
    }

    /** full constructor */
    public SheetDesignVariable(UUID id, UUID designId, String variableValue,
            Byte variableValueType, Integer startColumnNo, Integer startRowNo, Integer endRowNo, Integer endColumnNo,
            String description, Integer ordinal, Byte status, Timestamp createdTime, UUID createdBy,
            Timestamp lastModifiedTime, UUID lastModifiedBy, String variableName, Byte variableType, 
            String dataSourceId, String conditionId, String variableValueCaption) {
        this.id = id;
        this.designId = designId;
        this.variableValue = variableValue;
        this.variableValueType = variableValueType;
        this.startColumnNo = startColumnNo;
        this.startRowNo = startRowNo;
        this.endRowNo = endRowNo;
        this.endColumnNo = endColumnNo;
        this.description = description;
        this.ordinal = ordinal;
        this.status = status;
        this.createdTime = createdTime;
        this.createdBy = createdBy;
        this.lastModifiedTime = lastModifiedTime;
        this.lastModifiedBy = lastModifiedBy;
        this.variableName = variableName;
        this.variableType = variableType;
        this.dataSourceId = dataSourceId;
        this.conditionId = conditionId;
        this.variableValueCaption = variableValueCaption;
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

    @Column(name = "VARIABLE_VALUE")

    public String getVariableValue() {
        return this.variableValue;
    }

    public void setVariableValue(String variableValue) {
        this.variableValue = variableValue;
    }

    @Column(name = "VARIABLE_VALUE_TYPE", precision = 2, scale = 0)

    public Byte getVariableValueType() {
        return this.variableValueType;
    }

    public void setVariableValueType(Byte variableValueType) {
        this.variableValueType = variableValueType;
    }

    @Column(name = "START_COLUMN_NO", nullable = false, precision = 8, scale = 0)

    public Integer getStartColumnNo() {
        return this.startColumnNo;
    }

    public void setStartColumnNo(Integer startColumnNo) {
        this.startColumnNo = startColumnNo;
    }

    @Column(name = "START_ROW_NO", nullable = false, precision = 8, scale = 0)

    public Integer getStartRowNo() {
        return this.startRowNo;
    }

    public void setStartRowNo(Integer startRowNo) {
        this.startRowNo = startRowNo;
    }

    @Column(name = "END_ROW_NO", nullable = false, precision = 8, scale = 0)

    public Integer getEndRowNo() {
        return this.endRowNo;
    }

    public void setEndRowNo(Integer endRowNo) {
        this.endRowNo = endRowNo;
    }

    @Column(name = "END_COLUMN_NO", nullable = false, precision = 8, scale = 0)

    public Integer getEndColumnNo() {
        return this.endColumnNo;
    }

    public void setEndColumnNo(Integer endColumnNo) {
        this.endColumnNo = endColumnNo;
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

    @Column(name = "VARIABLE_NAME")
	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

    @Column(name = "VARIABLE_TYPE")
	public Byte getVariableType() {
		return variableType;
	}

	public void setVariableType(Byte variableType) {
		this.variableType = variableType;
	}

    @Column(name = "DATA_SOURCE_ID")
	public String getDataSourceId() {
		return dataSourceId;
	}

	public void setDataSourceId(String dataSourceId) {
		this.dataSourceId = dataSourceId;
	}

    @Column(name = "CONDITION_ID")
	public String getConditionId() {
		return conditionId;
	}

	public void setConditionId(String conditionId) {
		this.conditionId = conditionId;
	}

    @Column(name = "VARIABLE_VALUE_CAPTION")
	public String getVariableValueCaption() {
		return variableValueCaption;
	}

	public void setVariableValueCaption(String variableValueCaption) {
		this.variableValueCaption = variableValueCaption;
	}

	@Transient
	public String[] getDataSourceName() {
		return dataSourceName;
	}

	public void setDataSourceName(String[] dataSourceName) {
		this.dataSourceName = dataSourceName;
	}

	@Transient
	public String[] getConditionName() {
		return conditionName;
	}

	public void setConditionName(String[] conditionName) {
		this.conditionName = conditionName;
	}

	@Override
	public String toString() {
		return "SheetDesignVariable [id=" + id + ", designId=" + designId + ", variableValue=" + variableValue + ", variableValueType=" + variableValueType + ", startColumnNo="
				+ startColumnNo + ", startRowNo=" + startRowNo + ", endRowNo=" + endRowNo + ", endColumnNo="
				+ endColumnNo + ", description=" + description + ", ordinal=" + ordinal + ", status=" + status
				+ ", createdTime=" + createdTime + ", createdBy=" + createdBy + ", lastModifiedTime=" + lastModifiedTime
				+ ", lastModifiedBy=" + lastModifiedBy + "]";
	}

}