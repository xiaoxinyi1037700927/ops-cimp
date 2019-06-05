package com.sinosoft.ops.cimp.repository.sheet.impl;

import com.alibaba.fastjson.JSONObject;
import com.sinosoft.ops.cimp.common.dao.BaseDaoImpl;

import javax.persistence.EntityManagerFactory;
import java.util.UUID;

/**
 * 
 * @ClassName:  SheetDesignBind
 * @description: 报表数据绑定行列的设定
 * @author:        kanglin
 * @date:            2018年6月12日 下午1:35:30
 * @version        1.0.0
 * @since            JDK 1.7
 */
@SuppressWarnings("serial")
public class SheetDesignBind extends BaseDaoImpl implements java.io.Serializable{

	private String tableName;
	private String idFieldName;
	private String startRowFieldName;
	private String endRowFieldName;
	private String startColFieldName;
	private String endColFieldName;
	private String idValue;
	private int startRowValue = -1;
	private int endRowValue = -1;
	private int startColValue = -1;
	private int endColValue = -1;
	private String sectionNo;
	private String type;
	private String designId;
	private String message = null;

	public SheetDesignBind(EntityManagerFactory factory) {
		super(factory);
	}

	public void SheetDesignBind(String tableName,String idFieldName,String startRowFieldName,String endRowFieldName,String startColFieldName,String endColFieldName,String idValue,int startRowValue,
			 int endRowValue,
			 int startColValue,
			 int endColValue ,
			 String sectionNo,
			 String message)
	{

	}

	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getIdFieldName() {
		return idFieldName;
	}
	public void setIdFieldName(String idFieldName) {
		this.idFieldName = idFieldName;
	}
	public String getStartRowFieldName() {
		return startRowFieldName;
	}
	public void setStartRowFieldName(String startRowFieldName) {
		this.startRowFieldName = startRowFieldName;
	}
	public String getEndRowFieldName() {
		return endRowFieldName;
	}
	public void setEndRowFieldName(String endRowFieldName) {
		this.endRowFieldName = endRowFieldName;
	}
	public String getStartColFieldName() {
		return startColFieldName;
	}
	public void setStartColFieldName(String startColFieldName) {
		this.startColFieldName = startColFieldName;
	}
	public String getEndColFieldName() {
		return endColFieldName;
	}
	public void setEndColFieldName(String endColFieldName) {
		this.endColFieldName = endColFieldName;
	}
	public String getIdValue() {
		return idValue;
	}
	public void setIdValue(String idValue) {
		this.idValue = idValue;
	}
	public int getStartRowValue() {
		return startRowValue;
	}
	public void setStartRowValue(int startRowValue) {
		this.startRowValue = startRowValue;
	}
	public int getEndRowValue() {
		return endRowValue;
	}
	public void setEndRowValue(int endRowValue) {
		this.endRowValue = endRowValue;
	}
	public int getStartColValue() {
		return startColValue;
	}
	public void setStartColValue(int startColValue) {
		this.startColValue = startColValue;
	}
	public int getEndColValue() {
		return endColValue;
	}
	public void setEndColValue(int endColValue) {
		this.endColValue = endColValue;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getSectionNo() {
		return sectionNo;
	}
	public void setSectionNo(String sectionNo) {
		this.sectionNo = sectionNo;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getDesignId() {
		return designId;
	}
	public void setDesignId(String designId) {
		this.designId = designId;
	}
	
	public boolean isWordType() {
		return sectionNo != null && !"".equals(sectionNo) ? true : false;
	}
	
	public boolean isExcelType() {
		return sectionNo == null || "".equals(sectionNo) ? true : false;
	}

	//生成UpdateSql
	public String hasDataSql() {
		if (idFieldName == null || "".equals(idFieldName)) {
			idFieldName = "ID";
		}
		if (startRowFieldName == null || "".equals(startRowFieldName)) {
			startRowFieldName = "START_ROW_NO";
		}
		if (endRowFieldName == null || "".equals(endRowFieldName)) {
			endRowFieldName = "END_ROW_NO";
		}
		if (startColFieldName == null || "".equals(startColFieldName)) {
			startColFieldName = "START_COLUMN_NO";
		}
		if (endColFieldName == null || "".equals(endColFieldName)) {
			endColFieldName = "END_COLUMN_NO";
		}
		if (tableName == null || "".equals(tableName)) {
			message = "tableName没赋值.";
		} else if (idValue == null || "".equals(idValue)) {
			message = "idValue没赋值.";
		}
		if (designId == null || "".equals(designId)) {
			message = "designId没赋值.";
		}
		if (message != null && !"".equals(message) && !"SUCCESS".equals(message)) {
			return null;
		}
		message = "SUCCESS";
		JSONObject obj = new JSONObject().parseObject(idValue);
		String newIdValue = obj.getString(idFieldName);
		String sql = "SELECT 1 FROM " + tableName + " WHERE DESIGN_ID='" + designId + "' AND SECTION_NO='" + sectionNo + "' AND " + idFieldName + "='" + newIdValue + "'";
		return sql;
	}
	
	//生成UpdateSql
	public String hasDataExcelSql() {
		if (idFieldName == null || "".equals(idFieldName)) {
			idFieldName = "ID";
		}
		if (startRowFieldName == null || "".equals(startRowFieldName)) {
			startRowFieldName = "START_ROW_NO";
		}
		if (endRowFieldName == null || "".equals(endRowFieldName)) {
			endRowFieldName = "END_ROW_NO";
		}
		if (startColFieldName == null || "".equals(startColFieldName)) {
			startColFieldName = "START_COLUMN_NO";
		}
		if (endColFieldName == null || "".equals(endColFieldName)) {
			endColFieldName = "END_COLUMN_NO";
		}
		if (tableName == null || "".equals(tableName)) {
			message = "tableName没赋值.";
		} else if (idValue == null || "".equals(idValue)) {
			message = "idValue没赋值.";
		}
		if (designId == null || "".equals(designId)) {
			message = "designId没赋值.";
		}
		if (message != null && !"".equals(message) && !"SUCCESS".equals(message)) {
			return null;
		}
		message = "SUCCESS";
		JSONObject obj = new JSONObject().parseObject(idValue);
		String newIdValue = obj.getString(idFieldName);
		String sql = "SELECT 1 FROM " + tableName + " WHERE DESIGN_ID='" + designId + "' AND " + idFieldName + "='" + newIdValue + 
				"' AND ((" + startRowFieldName + "=" + startRowValue + " AND " + 
				endRowFieldName + "=" + endRowValue + " AND " + startColFieldName + "=" + startColValue + " AND " +
				endColFieldName + "=" + endColValue + ") or (" + startRowFieldName + " is null AND " + 
				endRowFieldName + " is null AND " + startColFieldName + " is null AND " +
				endColFieldName + " is null))";
		return sql;
	}
	
	public String hasDeleteExcelSql() {
		if (idFieldName == null || "".equals(idFieldName)) {
			idFieldName = "ID";
		}
		if (startRowFieldName == null || "".equals(startRowFieldName)) {
			startRowFieldName = "START_ROW_NO";
		}
		if (endRowFieldName == null || "".equals(endRowFieldName)) {
			endRowFieldName = "END_ROW_NO";
		}
		if (startColFieldName == null || "".equals(startColFieldName)) {
			startColFieldName = "START_COLUMN_NO";
		}
		if (endColFieldName == null || "".equals(endColFieldName)) {
			endColFieldName = "END_COLUMN_NO";
		}
		if (tableName == null || "".equals(tableName)) {
			message = "tableName没赋值.";
		} else if (idValue == null || "".equals(idValue)) {
			message = "idValue没赋值.";
		}
		if (designId == null || "".equals(designId)) {
			message = "designId没赋值.";
		}
		if (message != null && !"".equals(message) && !"SUCCESS".equals(message)) {
			return null;
		}
		message = "SUCCESS";
		JSONObject obj = new JSONObject().parseObject(idValue);
		String newIdValue = obj.getString(idFieldName);
		String sql = "SELECT 1 FROM " + tableName + " WHERE DESIGN_ID='" + designId + "' AND " + idFieldName + "='" + newIdValue + "'";
		return sql;
	}
	
	public String queryExcelSql() {
		if (idFieldName == null || "".equals(idFieldName)) {
			idFieldName = "ID";
		}
		if (startRowFieldName == null || "".equals(startRowFieldName)) {
			startRowFieldName = "START_ROW_NO";
		}
		if (endRowFieldName == null || "".equals(endRowFieldName)) {
			endRowFieldName = "END_ROW_NO";
		}
		if (startColFieldName == null || "".equals(startColFieldName)) {
			startColFieldName = "START_COLUMN_NO";
		}
		if (endColFieldName == null || "".equals(endColFieldName)) {
			endColFieldName = "END_COLUMN_NO";
		}
		if (tableName == null || "".equals(tableName)) {
			message = "tableName没赋值.";
		} else if (idValue == null || "".equals(idValue)) {
			message = "idValue没赋值.";
		}
		if (designId == null || "".equals(designId)) {
			message = "designId没赋值.";
		}
		if (message != null && !"".equals(message) && !"SUCCESS".equals(message)) {
			return null;
		}
		message = "SUCCESS";
		JSONObject obj = new JSONObject().parseObject(idValue);
		String newIdValue = obj.getString(idFieldName);
		String sql = "SELECT ID,DESIGN_ID," + idFieldName + "," + startRowFieldName + "," + 
				endRowFieldName + "," + startColFieldName + "," + endColFieldName + " FROM " + 
				tableName + " WHERE DESIGN_ID='" + designId + "' AND " + idFieldName + "='" + newIdValue + 
				"' AND (" + startRowFieldName + " is not null OR " + 
				endRowFieldName + " is not null OR " + startColFieldName + " is not null OR " +
				endColFieldName + " is not null)";
		return sql;
	}

	//生成UpdateSql
	public String toUpdateSql(String userId, boolean hasDatas) {
		//绑定excel
		if (idFieldName == null || "".equals(idFieldName)) {
			idFieldName = "ID";
		}
		if (startRowFieldName == null || "".equals(startRowFieldName)) {
			startRowFieldName = "START_ROW_NO";
		}
		if (endRowFieldName == null || "".equals(endRowFieldName)) {
			endRowFieldName = "END_ROW_NO";
		}
		if (startColFieldName == null || "".equals(startColFieldName)) {
			startColFieldName = "START_COLUMN_NO";
		}
		if (endColFieldName == null || "".equals(endColFieldName)) {
			endColFieldName = "END_COLUMN_NO";
		}
		if (tableName == null || "".equals(tableName)) {
			message = "tableName没赋值.";
		} else if (idValue == null || "".equals(idValue)) {
			message = "idValue没赋值.";
		}
		if (designId == null || "".equals(designId)) {
			message = "designId没赋值.";
		}
		if (message != null && !"".equals(message) && !"SUCCESS".equals(message)) {
			return null;
		}
		message = "SUCCESS";

		JSONObject obj = new JSONObject().parseObject(idValue);
		String newIdValue = obj.getString(idFieldName);
		if (isWordType()) {
			String sql = "";
			if (hasDatas) {
				if(obj.containsKey("FLAG"))
				{
					sql = "delete FROM " + tableName + " WHERE DESIGN_ID='" + designId + "' AND SECTION_NO='" + sectionNo + "' AND " + idFieldName + "='" + newIdValue + "'";
				}
				else
				{
					//已经有记录，update
					sql =  "UPDATE " + tableName + " SET SECTION_NO='" + sectionNo + "', LAST_MODIFIED_BY='" + userId +
							"', LAST_MODIFIED_TIME=SYSDATE WHERE DESIGN_ID='" + designId + "' AND SECTION_NO='" + sectionNo + "' AND " + idFieldName + "= '" +  newIdValue + "'";
				}
			} else {
				//没有记录，insert
				String newId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
				String fields = idFieldName + ",DESIGN_ID,SECTION_NO,ORDINAL,STATUS,CREATED_BY,CREATED_TIME,LAST_MODIFIED_BY,LAST_MODIFIED_TIME,ID";
				String values = "'" + newIdValue + "','" + designId + "','" + sectionNo + "',1,0,'" + userId + "',SYSDATE,'" + userId + "',SYSDATE,'" + newId + "'";
				sql = "INSERT INTO " + tableName + " (" + fields + ") VALUES (" + values + ")";
			}
			return sql;
		}

		String sql = "";
		if (hasDatas) {
			sql = "UPDATE " + tableName + " SET " + startRowFieldName + "=" + startRowValue + ", " + 
				endRowFieldName + "=" + endRowValue + ", " + startColFieldName + "=" + startColValue + ", " +
				endColFieldName + "=" + endColValue + ", LAST_MODIFIED_BY='" + userId + 
				"', LAST_MODIFIED_TIME=SYSDATE WHERE " + idFieldName + "='" + newIdValue + "' and DESIGN_ID='" + designId + 
				"' AND ((" + startRowFieldName + "=" + startRowValue + " AND " + 
				endRowFieldName + "=" + endRowValue + " AND " + startColFieldName + "=" + startColValue + " AND " +
				endColFieldName + "=" + endColValue + ") or (" + startRowFieldName + " is null AND " + 
				endRowFieldName + " is null AND " + startColFieldName + " is null AND " +
				endColFieldName + " is null))";
		} else {
			String newId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
			String fields = idFieldName + ",DESIGN_ID," + startRowFieldName + ", " + endRowFieldName + ", " + startColFieldName + ", " +
				endColFieldName + ",ORDINAL,STATUS,CREATED_BY,CREATED_TIME,LAST_MODIFIED_BY,LAST_MODIFIED_TIME,ID";
			String values = "'" + newIdValue + "','" + designId + "'," + startRowValue + ", "  + endRowValue + ", " + startColValue + ", " + 
				endColValue + ",1,0,'" + userId + "',SYSDATE,'" + userId + "',SYSDATE,'" + newId + "'";
			sql = "INSERT INTO " + tableName + " (" + fields + ") VALUES (" + values + ")";
		}
		return sql;
	}
	
	public String toExcelDeleteSql(String userId, int dataCnt) {
		if (idFieldName == null || "".equals(idFieldName)) {
			idFieldName = "ID";
		}
		if (startRowFieldName == null || "".equals(startRowFieldName)) {
			startRowFieldName = "START_ROW_NO";
		}
		if (endRowFieldName == null || "".equals(endRowFieldName)) {
			endRowFieldName = "END_ROW_NO";
		}
		if (startColFieldName == null || "".equals(startColFieldName)) {
			startColFieldName = "START_COLUMN_NO";
		}
		if (endColFieldName == null || "".equals(endColFieldName)) {
			endColFieldName = "END_COLUMN_NO";
		}
		if (tableName == null || "".equals(tableName)) {
			message = "tableName没赋值.";
		} else if (idValue == null || "".equals(idValue)) {
			message = "idValue没赋值.";
		}
		if (designId == null || "".equals(designId)) {
			message = "designId没赋值.";
		}
		if (message != null && !"".equals(message) && !"SUCCESS".equals(message)) {
			return null;
		}
		message = "SUCCESS";
		JSONObject obj = new JSONObject().parseObject(idValue);
		String newIdValue = obj.getString("ID");
		String sql = "";
		if (dataCnt > 1) {
			sql = "DELETE FROM " + tableName + " WHERE ID='" + newIdValue + "'";
		} else {
			sql = "UPDATE " + tableName + " SET " + startRowFieldName + "=null, " + 
					endRowFieldName + "=null, " + startColFieldName + "=null, " +
					endColFieldName + "=null, LAST_MODIFIED_BY='" + userId + 
					"', LAST_MODIFIED_TIME=SYSDATE WHERE ID='" + newIdValue + "'";
		}
		return sql;
	}
}
