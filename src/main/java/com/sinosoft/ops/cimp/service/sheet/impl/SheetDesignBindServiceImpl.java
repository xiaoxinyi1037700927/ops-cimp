package com.sinosoft.ops.cimp.service.sheet.impl;

import com.alibaba.fastjson.JSONObject;
import com.sinosoft.ops.cimp.common.dao.BaseEntityDao;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignBindDao;
import com.sinosoft.ops.cimp.repository.sheet.impl.SheetDesignBind;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignBindService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("SheetDesignBindService")
public class SheetDesignBindServiceImpl implements SheetDesignBindService {
	
	private static final Logger logger = LoggerFactory.getLogger(SheetDesignBindServiceImpl.class);

	@Autowired
	SheetDesignBindDao sheetDesignBindDao;

	@Autowired
	BaseEntityDao baseEntityDao;
	
	/**
	 * 用户传来的表名参数做转换，表名称对照表
	 */
	private static Map<String, String> excelTableNameMap = null;
	private static Map<String, String> wordTableNameMap = null;
	//绑定源ID字段名
	private static Map<String, String> idFieldNameMap = null;
	
	static {
		if (excelTableNameMap == null) {
			excelTableNameMap = new HashMap<String, String>();
			excelTableNameMap.put("3", "SHEET_DESIGN_DATA_SOURCE");
			excelTableNameMap.put("4", "SHEET_DESIGN_CONDITION");
			excelTableNameMap.put("2", "SHEET_DESIGN_FIELD_BINDING");
		}
		if (wordTableNameMap == null) {
			wordTableNameMap = new HashMap<String, String>();
			wordTableNameMap.put("3", "SHEET_DESIGN_DATA_SOURCE");
			wordTableNameMap.put("4", "SHEET_DESIGN_CONDITION");
			wordTableNameMap.put("2", "SHEET_DESIGN_FIELD_BINDING");
		}
		if (idFieldNameMap == null) {
			idFieldNameMap = new HashMap<String, String>();
			idFieldNameMap.put("3", "DATASOURCE_ID");
			idFieldNameMap.put("4", "CONDITION_ID");
			idFieldNameMap.put("2", "FIELD_ID");
		}
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public int updateRowCol(SheetDesignBind sheetDesignBind, String userId) throws Exception {
		String typeOrg = sheetDesignBind.getType();
		List<String> typeList = new ArrayList<String>();
		typeList.add(typeOrg);
		if (sheetDesignBind.isWordType() || sheetDesignBind.isExcelType()) {
			//word遍历idValue取绑定类型
			typeList = new ArrayList<String>();
			JSONObject obj = new JSONObject().parseObject(sheetDesignBind.getIdValue());
			for (Map.Entry<String, String> entry : idFieldNameMap.entrySet()) {
				if (obj.getString(entry.getValue()) != null && !"".equals(obj.getString(entry.getValue()))) {
					typeList.add(entry.getKey());
				}
			}
		}
		int cnt = 0;
		for (String type : typeList) {
			sheetDesignBind.setType(type);
			sheetDesignBind.setMessage(null);
			if (sheetDesignBind.isWordType()) {
				//word表名称转换
				if (wordTableNameMap != null && wordTableNameMap.containsKey(type)) {
					sheetDesignBind.setTableName(wordTableNameMap.get(type));
				}
			} else {
				//excel表名称转换
				if (excelTableNameMap != null && excelTableNameMap.containsKey(type)) {
					sheetDesignBind.setTableName(excelTableNameMap.get(type));
				}
			}
			if (sheetDesignBind.isWordType() && idFieldNameMap != null && idFieldNameMap.containsKey(type)) {
				//word绑定源ID字段名
				sheetDesignBind.setIdFieldName(idFieldNameMap.get(type));
			} else {
				//excel绑定源ID字段名
				sheetDesignBind.setIdFieldName(idFieldNameMap.get(type));
			}
			String idValue = sheetDesignBind.getIdValue();
			if (idValue != null) {
				sheetDesignBind.setIdValue(idValue.replace("-", "").toUpperCase());
			}
			String designId = sheetDesignBind.getDesignId();
			if (designId != null) {
				sheetDesignBind.setDesignId(designId.replace("-", "").toUpperCase());
			}
			String sql = "";
			if (sheetDesignBind.isWordType()) {
				//word
				String hasDataSql  = sheetDesignBind.hasDataSql();
				String message = sheetDesignBind.getMessage();
				if (message != null && message.equalsIgnoreCase("SUCCESS")) {
					boolean hasDatas = sheetDesignBindDao.hasDatas(hasDataSql);
					sql = sheetDesignBind.toUpdateSql(userId, hasDatas);
				} else {
					//条件不全
					Exception me = new Exception("修改表数据绑定失败：" + message);
					logger.error("修改表数据绑定失败：" + message, me);
					throw me;
				}
			} else {
				//excel
				String hasDataSql  = sheetDesignBind.hasDataExcelSql();
				String message = sheetDesignBind.getMessage();
				if (message != null && message.equalsIgnoreCase("SUCCESS")) {
					boolean hasDatas = sheetDesignBindDao.hasDatas(hasDataSql);
					sql = sheetDesignBind.toUpdateSql(userId, hasDatas);
				} else {
					//条件不全
					Exception me = new Exception("修改表数据绑定失败：" + message);
					logger.error("修改表数据绑定失败：" + message, me);
					throw me;
				}
			}
			String message = sheetDesignBind.getMessage();
			if (message != null && message.equalsIgnoreCase("SUCCESS")) {
				//拼sql成功
				cnt += sheetDesignBindDao.updateDesignBind(sql);
			} else {
				Exception me = new Exception("修改表数据绑定失败：" + message);
				logger.error("修改表数据绑定失败：" + message, me);
				throw me;
			}
		}
		logger.info("成功修改表数据绑定" + cnt + "条数据！");
		return cnt;
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public int deleteRowCol(SheetDesignBind sheetDesignBind, String userId) throws Exception {
		List<String> typeList = new ArrayList<String>();
		JSONObject obj = new JSONObject().parseObject(sheetDesignBind.getIdValue());
		for (Map.Entry<String, String> entry : idFieldNameMap.entrySet()) {
			if (obj.getString(entry.getValue()) != null && !"".equals(obj.getString(entry.getValue()))) {
				typeList.add(entry.getKey());
			}
		}
		int cnt = 0;
		for (String type : typeList) {
			sheetDesignBind.setType(type);
			sheetDesignBind.setMessage(null);
			//excel表名称转换
			if (excelTableNameMap != null && excelTableNameMap.containsKey(type)) {
				sheetDesignBind.setTableName(excelTableNameMap.get(type));
			}
			//excel绑定源ID字段名
			sheetDesignBind.setIdFieldName(idFieldNameMap.get(type));
			String idValue = sheetDesignBind.getIdValue();
			if (idValue != null) {
				sheetDesignBind.setIdValue(idValue.replace("-", "").toUpperCase());
			}
			String designId = sheetDesignBind.getDesignId();
			if (designId != null) {
				sheetDesignBind.setDesignId(designId.replace("-", "").toUpperCase());
			}
			String sql = "";
			//excel
			String hasDataSql  = sheetDesignBind.hasDeleteExcelSql();
			String message = sheetDesignBind.getMessage();
			if (message != null && message.equalsIgnoreCase("SUCCESS")) {
				int dataCnt = sheetDesignBindDao.dataCnt(hasDataSql);
				sql = sheetDesignBind.toExcelDeleteSql(userId, dataCnt);
			} else {
				//条件不全
				Exception me = new Exception("修改表数据绑定失败：" + message);
				logger.error("修改表数据绑定失败：" + message, me);
				throw me;
			}
			message = sheetDesignBind.getMessage();
			if (message != null && message.equalsIgnoreCase("SUCCESS")) {
				//拼sql成功
				cnt += sheetDesignBindDao.updateDesignBind(sql);
			} else {
				Exception me = new Exception("修改表数据绑定失败：" + message);
				logger.error("修改表数据绑定失败：" + message, me);
				throw me;
			}
		}
		logger.info("成功修改表数据绑定" + cnt + "条数据！");
		return cnt;
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public List<Map<String, Object>> queryExcel(SheetDesignBind sheetDesignBind) throws Exception {
		List<String> typeList = new ArrayList<String>();
		JSONObject obj = new JSONObject().parseObject(sheetDesignBind.getIdValue());
		for (Map.Entry<String, String> entry : idFieldNameMap.entrySet()) {
			if (obj.getString(entry.getValue()) != null && !"".equals(obj.getString(entry.getValue()))) {
				typeList.add(entry.getKey());
			}
		}
		for (String type : typeList) {
			sheetDesignBind.setType(type);
			sheetDesignBind.setMessage(null);
			//excel表名称转换
			if (excelTableNameMap != null && excelTableNameMap.containsKey(type)) {
				sheetDesignBind.setTableName(excelTableNameMap.get(type));
			}
			//excel绑定源ID字段名
			sheetDesignBind.setIdFieldName(idFieldNameMap.get(type));
			String idValue = sheetDesignBind.getIdValue();
			if (idValue != null) {
				sheetDesignBind.setIdValue(idValue.replace("-", "").toUpperCase());
			}
			String designId = sheetDesignBind.getDesignId();
			if (designId != null) {
				sheetDesignBind.setDesignId(designId.replace("-", "").toUpperCase());
			}
			String sql = sheetDesignBind.queryExcelSql();
			String message = sheetDesignBind.getMessage();
			if (message != null && message.equalsIgnoreCase("SUCCESS")) {
				//拼sql成功
				List<Map<String, Object>> list = sheetDesignBindDao.findBySQL(sql);
				List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
				for (Map<String, Object> map : list) {
					Map<String, Object> newMap = new HashMap<String, Object>();
					for (Map.Entry<String, Object> entry : map.entrySet()) {
						Object value = entry.getValue();
						if (entry.getKey().toUpperCase().endsWith("ID")) {
							value = (String)(baseEntityDao.toUUIDStringByRaw(entry.getValue()));
						}
						newMap.put(entry.getKey(), value);
					}
					newList.add(newMap);
				}
				return newList;
			} else {
				Exception me = new Exception("修改表数据绑定失败：" + message);
				logger.error("修改表数据绑定失败：" + message, me);
				throw me;
			}
		}
		logger.info("成功查询表数据表数据！");
		return null;
	}
}
