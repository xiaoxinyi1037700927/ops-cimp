package com.sinosoft.ops.cimp.entity.sheet;

import com.sinosoft.ops.cimp.common.model.Trackable;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;



/**
 * 
 * @ClassName:  SheetTag
 * @description: 报表批注
 */
@Entity
@Table(name="SHEET_TAG")
public class SheetTag implements Serializable,Trackable {

	private static final long serialVersionUID = 1L;

	private UUID id;
	
	private Integer columnNo;
	
	private Integer rowNo;
	
	private UUID createdBy;
	
	private Timestamp createdTime;
	
	private String description;
	
	private UUID sheetId;
	
	private UUID lastModifiedBy;
	
	private Timestamp lastModifiedTime;
	
	private String name;
	
	private Integer ordinal;
	
	private Byte status;
	
	private Integer ctrlColumnNo;
	
	private Integer ctrlRowNo;
	

	@Id
	@Column(name="ID")
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	@Column(name="COLUMN_NO")
	public Integer getColumnNo() {
		return columnNo;
	}

	public void setColumnNo(Integer columnNo) {
		this.columnNo = columnNo;
	}

	@Column(name="ROW_NO")
	public Integer getRowNo() {
		return rowNo;
	}

	public void setRowNo(Integer rowNo) {
		this.rowNo = rowNo;
	}

	@Column(name="CREATED_BY")
	public UUID getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UUID createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name="CREATED_TIME")
	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	@Column(name="DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name="SHEET_ID")
	public UUID getSheetId() {
		return sheetId;
	}

	public void setSheetId(UUID sheetId) {
		this.sheetId = sheetId;
	}

	@Column(name="LAST_MODIFIED_BY")
	public UUID getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(UUID lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Column(name="LAST_MODIFIED_TIME")
	public Timestamp getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(Timestamp lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	@Column(name="NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name="ORDINAL")
	public Integer getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(Integer ordinal) {
		this.ordinal = ordinal;
	}

	@Column(name="STATUS")
	@Override
	public Byte getStatus() {
		return status;
	}

	@Override
	public void setStatus(Byte status) {
		this.status = status;
	}

	@Transient
	public Integer getCtrlColumnNo() {
		return ctrlColumnNo;
	}

	public void setCtrlColumnNo(Integer ctrlColumnNo) {
		this.ctrlColumnNo = ctrlColumnNo;
	}

	@Transient
	public Integer getCtrlRowNo() {
		return ctrlRowNo;
	}

	public void setCtrlRowNo(Integer ctrlRowNo) {
		this.ctrlRowNo = ctrlRowNo;
	}
	
	
	
}
