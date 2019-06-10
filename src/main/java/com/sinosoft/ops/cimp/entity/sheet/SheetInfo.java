package com.sinosoft.ops.cimp.entity.sheet;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

/**
 * 
 * @ClassName:  SheetInfo
 * @description: 报表信息包装类
 * @author:        sunch

 */
@Entity
public class SheetInfo {

	private UUID sheetId;
	private UUID designId;
	private String sheetNo;
	private String sheetName;
	
	@Id
	public UUID getSheetId() {
		return sheetId;
	}
	public void setSheetId(UUID sheetId) {
		this.sheetId = sheetId;
	}
	public UUID getDesignId() {
		return designId;
	}
	public void setDesignId(UUID designId) {
		this.designId = designId;
	}
	public String getSheetNo() {
		return sheetNo;
	}
	public void setSheetNo(String sheetNo) {
		this.sheetNo = sheetNo;
	}
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName){
		this.sheetName = sheetName;
	}
	
}
