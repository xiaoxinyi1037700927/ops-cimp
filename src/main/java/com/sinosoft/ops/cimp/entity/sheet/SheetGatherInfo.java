package com.sinosoft.ops.cimp.entity.sheet;

import com.sinosoft.ops.cimp.common.model.Trackable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;



@Entity
@Table(name="SHEET_GATHER_INFO")
public class SheetGatherInfo implements Serializable,Trackable {

	private static final long serialVersionUID = 9205193922267463076L;
	
	private UUID id;
	private UUID sheetId;
	private UUID gatherSheetId;
	private Integer ordinal;
    private Byte status;
    private Timestamp createdTime;
    private UUID createdBy;
    private Timestamp lastModifiedTime;
    private UUID lastModifiedBy;
    
    @Id
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	@Column(name="SHEET_ID")
	public UUID getSheetId() {
		return sheetId;
	}
	public void setSheetId(UUID sheetId) {
		this.sheetId = sheetId;
	}
	@Column(name="ORDINAL")
	public Integer getOrdinal() {
		return ordinal;
	}
	public void setOrdinal(Integer ordinal) {
		this.ordinal = ordinal;
	}
	@Column(name="STATUS")
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	@Column(name="CREATED_TIME")
	public Timestamp getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}
	@Column(name="CREATED_BY")
	public UUID getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(UUID createdBy) {
		this.createdBy = createdBy;
	}
	@Column(name="LAST_MODIFIED_TIME")
	public Timestamp getLastModifiedTime() {
		return lastModifiedTime;
	}
	public void setLastModifiedTime(Timestamp lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}
	@Column(name="LAST_MODIFIED_BY")
	public UUID getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(UUID lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	@Column(name="GATHER_SHEET_ID")
	public UUID getGatherSheetId() {
		return gatherSheetId;
	}
	public void setGatherSheetId(UUID gatherSheetId) {
		this.gatherSheetId = gatherSheetId;
	}
    
    
}
