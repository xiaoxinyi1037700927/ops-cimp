package com.sinosoft.ops.cimp.entity.sheet;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * SheetDesignField entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SHEET_DESIGN_FIELD")

public class SheetDesignField implements java.io.Serializable {

    // Fields
    private static final long serialVersionUID = 7418827377347328006L;
    private UUID id;
    private UUID designId;
    private String name;
    private String functionName;
    private String dataRange;
    private String parameter1;
    private Byte parameter1Type;
    private String parameter2;
    private Byte parameter2Type;
    private String parameter3;
    private Byte parameter3Type;
    private Boolean usingDistinct;
    private String referenceTable;
    private String referenceValueColumn;
    private String referenceDisplayColumn;
    private String alias;
    private Integer endColumnNo;
    private Integer endRowNo;
    private Integer startColumnNo;
    private Integer startRowNo;
    private String description;
    private Integer ordinal;
    private Byte status;
    private Timestamp createdTime;
    private UUID createdBy;
    private Timestamp lastModifiedTime;
    private UUID lastModifiedBy;
    private String sectionNo;

    private String jsondata;
    private String sql;
    //--新加字段
    /**数据源id*/
    private UUID dataSourceId;

    private String dataSourceName;
    

    // Constructors

    /** default constructor */
    public SheetDesignField() {
    }

    /** minimal constructor */
    public SheetDesignField(UUID id, UUID designId, Integer endColumnNo, Integer endRowNo, Integer startColumnNo,
            Integer startRowNo, Byte status, Timestamp createdTime, Timestamp lastModifiedTime,String sectionNo) {
        this.id = id;
        this.designId = designId;
        this.endColumnNo = endColumnNo;
        this.endRowNo = endRowNo;
        this.startColumnNo = startColumnNo;
        this.startRowNo = startRowNo;
        this.status = status;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
        this.sectionNo=sectionNo;
    }

    /** full constructor */
    public SheetDesignField(UUID id, UUID designId, String name, String functionName, String parameter1,
            Byte parameter1Type, String parameter2, Byte parameter2Type, String parameter3, Byte parameter3Type,
            Boolean usingDistinct, String referenceTable, String referenceValueColumn, String referenceDisplayColumn,
            String alias, Integer endColumnNo, Integer endRowNo, Integer startColumnNo, Integer startRowNo,
            String description, Integer ordinal, Byte status, Timestamp createdTime, UUID createdBy,
            Timestamp lastModifiedTime, UUID lastModifiedBy,String sectionNo) {
        this.id = id;
        this.designId = designId;
        this.name = name;
        this.functionName = functionName;
        this.parameter1 = parameter1;
        this.parameter1Type = parameter1Type;
        this.parameter2 = parameter2;
        this.parameter2Type = parameter2Type;
        this.parameter3 = parameter3;
        this.parameter3Type = parameter3Type;
        this.usingDistinct = usingDistinct;
        this.referenceTable = referenceTable;
        this.referenceValueColumn = referenceValueColumn;
        this.referenceDisplayColumn = referenceDisplayColumn;
        this.alias = alias;
        this.endColumnNo = endColumnNo;
        this.endRowNo = endRowNo;
        this.startColumnNo = startColumnNo;
        this.startRowNo = startRowNo;
        this.description = description;
        this.ordinal = ordinal;
        this.status = status;
        this.createdTime = createdTime;
        this.createdBy = createdBy;
        this.lastModifiedTime = lastModifiedTime;
        this.lastModifiedBy = lastModifiedBy;
        this.sectionNo=sectionNo;
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

    @Column(name = "NAME", length = 100)

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "SECTION_NO", length = 10)
    public String getSectionNo() {        return this.sectionNo;    }
    public void setSectionNo(String sectionNo) {        this.sectionNo = sectionNo;    }

    @Column(name = "FUNCTION_NAME", length = 30)

    public String getFunctionName() {
        return this.functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    @Column(name = "DATA_RANGE", length = 512)
    public String getDataRange() {
        return this.dataRange;
    }
    public void setDataRange(String dataRange) {
        this.dataRange = dataRange;
    }


    @Column(name = "PARAMETER1", length = 200)

    public String getParameter1() {
        return this.parameter1;
    }

    public void setParameter1(String parameter1) {
        this.parameter1 = parameter1;
    }

    @Column(name = "PARAMETER1_TYPE", precision = 2, scale = 0)

    public Byte getParameter1Type() {
        return this.parameter1Type;
    }

    public void setParameter1Type(Byte parameter1Type) {
        this.parameter1Type = parameter1Type;
    }

    @Column(name = "PARAMETER2", length = 200)

    public String getParameter2() {
        return this.parameter2;
    }

    public void setParameter2(String parameter2) {
        this.parameter2 = parameter2;
    }

    @Column(name = "PARAMETER2_TYPE", precision = 2, scale = 0)

    public Byte getParameter2Type() {
        return this.parameter2Type;
    }

    public void setParameter2Type(Byte parameter2Type) {
        this.parameter2Type = parameter2Type;
    }

    @Column(name = "PARAMETER3", length = 200)

    public String getParameter3() {
        return this.parameter3;
    }

    public void setParameter3(String parameter3) {
        this.parameter3 = parameter3;
    }

    @Column(name = "PARAMETER3_TYPE", precision = 2, scale = 0)

    public Byte getParameter3Type() {
        return this.parameter3Type;
    }

    public void setParameter3Type(Byte parameter3Type) {
        this.parameter3Type = parameter3Type;
    }

    @Column(name = "USING_DISTINCT", precision = 1, scale = 0)

    public Boolean getUsingDistinct() {
        return this.usingDistinct;
    }

    public void setUsingDistinct(Boolean usingDistinct) {
        this.usingDistinct = usingDistinct;
    }

    @Column(name = "REFERENCE_TABLE", length = 50)

    public String getReferenceTable() {
        return this.referenceTable;
    }

    public void setReferenceTable(String referenceTable) {
        this.referenceTable = referenceTable;
    }

    @Column(name = "REFERENCE_VALUE_COLUMN", length = 50)

    public String getReferenceValueColumn() {
        return this.referenceValueColumn;
    }

    public void setReferenceValueColumn(String referenceValueColumn) {
        this.referenceValueColumn = referenceValueColumn;
    }

    @Column(name = "REFERENCE_DISPLAY_COLUMN", length = 50)

    public String getReferenceDisplayColumn() {
        return this.referenceDisplayColumn;
    }

    public void setReferenceDisplayColumn(String referenceDisplayColumn) {
        this.referenceDisplayColumn = referenceDisplayColumn;
    }

    @Column(name = "ALIAS", length = 50)

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Column(name = "END_COLUMN_NO", nullable = false, precision = 8, scale = 0)

    public Integer getEndColumnNo() {
        return this.endColumnNo;
    }

    public void setEndColumnNo(Integer endColumnNo) {
        this.endColumnNo = endColumnNo;
    }

    @Column(name = "END_ROW_NO", nullable = false, precision = 8, scale = 0)

    public Integer getEndRowNo() {
        return this.endRowNo;
    }

    public void setEndRowNo(Integer endRowNo) {
        this.endRowNo = endRowNo;
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

    @Column(name="DATA_SOURCE_ID")
	public UUID getDataSourceId() {
		return dataSourceId;
	}

    @Column(name = "Json_Data")
    public String getJsonData()  {
        return this.jsondata;
    }
    public void setJsonData(String jsondata) {
        this.jsondata = jsondata;
    }

    @Column(name = "SQL")
    public String getSql() {
        return this.sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

	public void setDataSourceId(UUID dataSourceId) {
		this.dataSourceId = dataSourceId;
	}

    public void setDataSourceName(String dataSourceName)
    {
        this.dataSourceName=dataSourceName;
    }
    @Transient
    public String getDataSourceName()
    {
        return this.dataSourceName;
    }
}