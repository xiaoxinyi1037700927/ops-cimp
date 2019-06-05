package com.sinosoft.ops.cimp.entity.sheet;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * SheetDesignCondition entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SHEET_DESIGN_CONDITION")

public class SheetDesignCondition implements java.io.Serializable {

    // Fields
    private static final long serialVersionUID = 6728644515905649428L;
    private UUID id;
    private UUID designId;
    private UUID conditionId;
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
    private String conditionName;
private String sectionNo;
    /**条件sql*/
    private String sql;

    // Constructors

    /** default constructor */
    public SheetDesignCondition() {
    }

    /** minimal constructor */
    public SheetDesignCondition(UUID id, UUID designId, UUID conditionId, Integer endColumnNo, Integer endRowNo,
            Integer startColumnNo, Integer startRowNo, Byte status, Timestamp createdTime, Timestamp lastModifiedTime,String sectionNo) {
        this.id = id;
        this.designId = designId;
        this.conditionId = conditionId;
        this.endColumnNo = endColumnNo;
        this.endRowNo = endRowNo;
        this.startColumnNo = startColumnNo;
        this.startRowNo = startRowNo;
        this.status = status;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
        this.sectionNo=sectionNo;
    }

    public SheetDesignCondition(UUID id, UUID designId, UUID conditionId,String conditionName){
        this.conditionId = conditionId;
        this.id=id;
        this.designId=designId;
        this.conditionName=conditionName;
    }
    
    public SheetDesignCondition(UUID id, UUID designId, UUID conditionId,String conditionName,String sql,
    		Integer endColumnNo, Integer endRowNo,
            Integer startColumnNo, Integer startRowNo,String sectionNo){
        this.conditionId = conditionId;
        this.id=id;
        this.designId=designId;
        this.conditionName=conditionName;
        this.sql=sql;
        this.endColumnNo = endColumnNo;
        this.endRowNo = endRowNo;
        this.startColumnNo = startColumnNo;
        this.startRowNo = startRowNo;
        this.sectionNo=sectionNo;
    }

    /** full constructor */
    public SheetDesignCondition(UUID id, UUID designId, UUID conditionId, Integer endColumnNo, Integer endRowNo,
            Integer startColumnNo, Integer startRowNo, Integer ordinal, Byte status, Timestamp createdTime,
            UUID createdBy, Timestamp lastModifiedTime, UUID lastModifiedBy) {
        this.id = id;
        this.designId = designId;
        this.conditionId = conditionId;
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
        this.conditionName=conditionName;
    }
    
    

    public SheetDesignCondition(UUID designId, UUID conditionId, String conditionName) {
		super();
		this.designId = designId;
		this.conditionId = conditionId;
		this.conditionName = conditionName;
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

    @Column(name = "CONDITION_ID", nullable = false)

    public UUID getConditionId() {
        return this.conditionId;
    }

    public void setConditionId(UUID conditionId) {
        this.conditionId = conditionId;
    }

    @Column(name = "SECTION_NO", length = 10)
    public String getSectionNo() {        return this.sectionNo;    }
    public void setSectionNo(String sectionNo) {        this.sectionNo = sectionNo;    }

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
    public String getConditionName() {
        return conditionName;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }

    @Transient
	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
    
    
}