package com.sinosoft.ops.cimp.entity.sheet;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class SheetGatherParameter {

	private String name;
	private String description;
	private UUID gatherSheetId;
	private Integer rowNo;
	
	
	public UUID getGatherSheetId() {
		return gatherSheetId;
	}
	public void setGatherSheetId(UUID gatherSheetId) {
		this.gatherSheetId = gatherSheetId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public SheetGatherParameter() {
		super();
	}
	@Id
	public Integer getRowNo() {
		return rowNo;
	}
	public void setRowNo(Integer rowNo) {
		this.rowNo = rowNo;
	}
	
	
	
}
