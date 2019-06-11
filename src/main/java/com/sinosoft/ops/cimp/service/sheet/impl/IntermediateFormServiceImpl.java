package com.sinosoft.ops.cimp.service.sheet.impl;


import com.sinosoft.ops.cimp.common.service.BaseServiceImpl;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoItem;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoSet;
import com.sinosoft.ops.cimp.exception.CloneException;
import com.sinosoft.ops.cimp.repository.sheet.IntermediateFormDao;
import com.sinosoft.ops.cimp.service.infostruct.SysInfoItemService;
import com.sinosoft.ops.cimp.service.infostruct.SysInfoSetService;
import com.sinosoft.ops.cimp.service.sheet.IntermediateFormService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.sql.Timestamp;
import java.util.*;


@Service
public class IntermediateFormServiceImpl extends BaseServiceImpl implements IntermediateFormService {

	
	@Autowired
	private SysInfoSetService sysInfoSetService;
	@Autowired
	private SysInfoItemService sysInfoItemService;
	@Autowired
	private IntermediateFormDao intermediateFormDao;
	
	@Override
	@Transactional
	public void generatorIntermediateForm(String sheetName, String[] columns) throws CloneException {
		
		
		String tableNameCN = sheetName.split("_")[0];
		String tableName = sheetName.split("_")[1];
		
		//添加infoset
		SysInfoSet infoSet = getInfoSet(tableName, tableNameCN);
		Integer setId = (Integer) sysInfoSetService.create(infoSet);
		
		List<SysInfoItem> cloneItems = getInfoItem(setId,Arrays.asList(columns),null);
		
		SysInfoItem pkColumn = cloneItems.get(cloneItems.size()-1);
		cloneItems.remove(cloneItems.size()-1);
		sysInfoItemService.createAll(cloneItems);
		sysInfoItemService.create(pkColumn);
		
		//建表
		String createTableSql = buildCreateTableSql(cloneItems, tableName,pkColumn);
		intermediateFormDao.executeUpdateSql(createTableSql);
	}
	
	
	@Override
	@Transactional
	public void generatorIntermediateForm(String setInfoName, String description, Map<String, String> itemMap,Map<String,String> fieldDecMap) throws CloneException {
		SysInfoSet infoSet = getInfoSet(setInfoName, description);
		
		Integer setId = (Integer) sysInfoSetService.create(infoSet);
		List<SysInfoItem> cloneItems = getInfoItem(setId,itemMap,fieldDecMap);
		SysInfoItem pkColumn = cloneItems.get(cloneItems.size()-1);
		cloneItems.remove(cloneItems.size()-1);
		
		sysInfoItemService.createAll(cloneItems);
		sysInfoItemService.create(pkColumn);
		
		//建表
		String createTableSql = buildCreateTableSql(cloneItems, setInfoName,pkColumn);
		intermediateFormDao.executeUpdateSql(createTableSql);
	}
	
	
	/**
	 * 生成创建表格语句
	 * @param items
	 * @param tableName
	 * @return
	 */
	public String buildCreateTableSql(List<SysInfoItem> items,String tableName,SysInfoItem pkItem){
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE MT_"+tableName);
		sql.append(" (");
		if(pkItem != null){
			sql.append(pkItem.getColumnName()+" "+pkItem.getDbType()+"("+pkItem.getLength()+") not null ,");
		}
		for (SysInfoItem item : items) {
			sql.append(item.getColumnName()+" "+item.getDbType());
			if("RAW".equals(item.getDbType())||"VARCHAR2".equals(item.getDbType())
					||"COLB".equals(item.getDbType())){
				sql.append("("+item.getLength()+")");
			}else if("NUMBER".equals(item.getDbType())){
				if(item.getPrecision() != null && item.getPrecision()>0){
					sql.append("("+item.getPrecision()+","+item.getScale()+")");
				}
			}
			sql.append(",");
		}
		sql.deleteCharAt(sql.lastIndexOf(","));
		sql.append(")");
		return sql.toString();
	}
	
	public List<SysInfoItem> getInfoItem(Integer setId,Collection<String> columns,Map<String,String> itemMap) throws CloneException{
		SysInfoItem emp = (SysInfoItem) this.cloneObject(sysInfoItemService.getByName(14, "empId"));
		SysInfoItem dep = (SysInfoItem) this.cloneObject(sysInfoItemService.getByName(67, "DEP_ID"));
		
		//添加infoitem
		List<SysInfoItem> cloneItems = new ArrayList<>();
		Set<Integer> pkColumns = new HashSet<>();
		int i = 1;
		for (String name : columns) {
			Collection<SysInfoItem> collection = intermediateFormDao.getItemByName(name.trim().toLowerCase());
			Assert.isTrue(collection.size()>0,"字段："+name+"在库中不存在");
			SysInfoItem item = ((List<SysInfoItem>)collection).get(0);
			SysInfoItem cloneItem = (SysInfoItem)this.cloneObject(item);
			cloneItem.setInfoSetId(setId);
			cloneItem.setId(null);
			if(itemMap == null){
				cloneItem.setName(cloneItem.getName()+"_"+i);
				cloneItem.setColumnName(cloneItem.getColumnName()+"_"+i);
			}else{
				cloneItem.setName(itemMap.get(name));
				cloneItem.setColumnName(itemMap.get(name));
			}
			
			cloneItems.add(cloneItem);
			//如果有A001或B001的字段 则添加 EMP_ID 或 DEP_ID主键
			if(item.getInfoSetId()==14||item.getInfoSetId()==67){
				pkColumns.add(item.getInfoSetId());
			}
			i++;
		}
		//添加status字段
		Collection<SysInfoItem> collection = intermediateFormDao.getItemByName("status");
		SysInfoItem statusItem = ((List<SysInfoItem>)collection).get(0);
		SysInfoItem coloneStatusItem = (SysInfoItem) this.cloneObject(statusItem);
		coloneStatusItem.setInfoSetId(setId);
		coloneStatusItem.setId(null);
		cloneItems.add(coloneStatusItem);
		
		SysInfoItem pkColumn = null;
		if(pkColumns.contains(14)){
			pkColumn = emp;
		}else if( pkColumns.contains(67)){
			pkColumn = dep;
		}
		if(pkColumn != null){
			pkColumn.setInfoSetId(setId);
			pkColumn.setId(null);
		}
		cloneItems.add(pkColumn);
		return cloneItems;
	}
	
	public List<SysInfoItem> getInfoItem(Integer setId,Map<String,String> itemMap,Map<String,String> fieldDecMap) throws CloneException{
		SysInfoItem emp = (SysInfoItem) this.cloneObject(sysInfoItemService.getByName(14, "empId"));
		SysInfoItem dep = (SysInfoItem) this.cloneObject(sysInfoItemService.getByName(67, "DEP_ID"));
		
		//添加infoitem
		List<SysInfoItem> cloneItems = new ArrayList<>();
		Set<Integer> pkColumns = new HashSet<>();
		Set<String> keySet = itemMap.keySet();
		
		for (String key : keySet) {
			String column = itemMap.get(key);
			if(StringUtils.isNotBlank(column)){
				Collection<SysInfoItem> collection = intermediateFormDao.getItemByName(column.trim().toLowerCase());
				Assert.isTrue(collection.size()>0,"字段："+column+"在库中不存在");
				SysInfoItem item = ((List<SysInfoItem>)collection).get(0);
				SysInfoItem cloneItem = (SysInfoItem)this.cloneObject(item);
				cloneItem.setInfoSetId(setId);
				cloneItem.setId(null);
				cloneItem.setName(key);
				cloneItem.setColumnName(key);
				cloneItem.setNameCn(fieldDecMap.get(key)==null?cloneItem.getNameCn():fieldDecMap.get(key));
				cloneItems.add(cloneItem);
				
				//如果有A001或B001的字段 则添加 EMP_ID 或 DEP_ID主键
				if(item.getInfoSetId()==14||item.getInfoSetId()==67){
					pkColumns.add(item.getInfoSetId());
				}
			}
		}
	
		//添加status字段
		Collection<SysInfoItem> collection = intermediateFormDao.getItemByName("status");
		SysInfoItem statusItem = ((List<SysInfoItem>)collection).get(0);
		SysInfoItem coloneStatusItem = (SysInfoItem) this.cloneObject(statusItem);
		coloneStatusItem.setInfoSetId(setId);
		coloneStatusItem.setId(null);
		cloneItems.add(coloneStatusItem);
		
		SysInfoItem pkColumn = null;
		if(pkColumns.contains(14)){
			pkColumn = emp;
		}else if( pkColumns.contains(67)){
			pkColumn = dep;
		}
		if(pkColumn != null){
			pkColumn.setInfoSetId(setId);
			pkColumn.setId(null);
		}
		cloneItems.add(pkColumn);
		return cloneItems;
	}
	
	public SysInfoSet getInfoSet(String name,String nameCN){
		SysInfoSet infoSet = new SysInfoSet();
		infoSet.setId(null);
		infoSet.setOrdinal(null);
		infoSet.setName(name);
		infoSet.setTableName("MT_"+name.toUpperCase());
		infoSet.setNameCn(nameCN);
		infoSet.setGroupId(1);
		infoSet.setGroupMain(false);
		infoSet.setShortNameCn(null);
		infoSet.setAlias(null);
		infoSet.setType((byte)2);
		infoSet.setDescription(nameCN);
		infoSet.setCategoryId(5);
		infoSet.setInvalid(false);
		infoSet.setStatus((byte)0);
		infoSet.setCreatedTime(new Timestamp(System.currentTimeMillis()));
		infoSet.setCreatedBy(null);
		infoSet.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
		infoSet.setLastModifiedBy(null);
		infoSet.setInsertable(true);
		infoSet.setUpdatable(true);
		infoSet.setDeletable(true);
		infoSet.setSelectable(true);
		infoSet.setApprovalStatus((byte)1);
		infoSet.setApprovedTime(new Timestamp(System.currentTimeMillis()));
		infoSet.setApprovedBy(null);
		infoSet.setSecretLevel(null);
		infoSet.setDeclassifiedDate(null);
		return infoSet;
	}

	
}
