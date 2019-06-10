package com.sinosoft.ops.cimp.entity.sheet;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class GatherInfo {

	private UUID gatherSheetId;
	private String categoryName;
	private String sheetName;
	private String stringValue;
	
	@Id
	public UUID getGatherSheetId() {
		return gatherSheetId;
	}
	public void setGatherSheetId(UUID gatherSheetId) {
		this.gatherSheetId = gatherSheetId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	public String getStringValue() {
		return stringValue;
	}
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}
	
	
}
