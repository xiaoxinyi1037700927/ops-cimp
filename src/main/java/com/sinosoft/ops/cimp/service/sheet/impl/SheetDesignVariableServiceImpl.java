package com.sinosoft.ops.cimp.service.sheet.impl;

import com.google.common.base.Throwables;
import com.sinosoft.ops.cimp.common.service.BaseEntityServiceImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetCondition;
import com.sinosoft.ops.cimp.entity.sheet.SheetDataSource;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignVariable;
import com.sinosoft.ops.cimp.repository.sheet.SheetConditionDao;
import com.sinosoft.ops.cimp.repository.sheet.SheetDataSourceDao;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignVariableService;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignVariableDao;
import com.sinosoft.ops.cimp.util.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName:  SheetDesignVariableServiceImpl
 * @description: 表设计变量实现类
 * @author:        lixianfu
 * @date:            2018年6月4日 下午2:30:56
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Service("SheetDesignVariableService")
public class SheetDesignVariableServiceImpl extends BaseEntityServiceImpl<SheetDesignVariable> implements SheetDesignVariableService {
	 private static final Logger logger = LoggerFactory.getLogger(SheetDesignVariableService.class);

	@Autowired
	private SheetDesignVariableDao SheetDesignVariableDao;
	
	@Autowired
	private SheetDataSourceDao sheetDataSourceDao;
	
	@Autowired
	private SheetConditionDao sheetConditionDao;

	@Override
	@Transactional(readOnly=true)
	public SheetDesignVariable getByVariableId(UUID id) {
		SheetDesignVariable variable = SheetDesignVariableDao.getById(id);
		String dataSourceId = variable.getDataSourceId();
		if (dataSourceId != null && !"".equals(dataSourceId)) {
			String[] idArray = dataSourceId.split(",");
			if (idArray != null && idArray.length > 0) {
				String[] nameArray = new String[idArray.length];
				int n = 0;
				for (String aId : idArray) {
					if(aId != null && !"".equals(aId)) {
						SheetDataSource dataSource = sheetDataSourceDao.getById(UUID.fromString(aId));
						if (dataSource != null) {
							nameArray[n] = dataSource.getName();
						} else {
							nameArray[n] = null;
						}
					} else {
						nameArray[n] = null;
					}
					n++;
				}
				variable.setDataSourceName(nameArray);
			}
		}
		String conditionId = variable.getConditionId();
		if (conditionId != null && !"".equals(conditionId)) {
			String[] idArray = conditionId.split(",");
			if (idArray != null && idArray.length > 0) {
				String[] nameArray = new String[idArray.length];
				int n = 0;
				for (String aId : idArray) {
					if(aId != null && !"".equals(aId)) {
						SheetCondition condition = sheetConditionDao.getById(UUID.fromString(aId));
						if (condition != null) {
							nameArray[n] = condition.getConditionName();
						} else {
							nameArray[n] = null;
						}
					} else {
						nameArray[n] = null;
					}
					n++;
				}
				variable.setConditionName(nameArray);
			}
		}
		return variable;
	}
	
	@Override
	@Transactional	
	public UUID saveVariable(SheetDesignVariable sheetDesignVariable) {
    	if(sheetDesignVariable.getId() == null){
    		sheetDesignVariable.setId(UUID.randomUUID());
        }
    	if(sheetDesignVariable.getDesignId() == null){
    		sheetDesignVariable.setDesignId(UUID.randomUUID());
        }
        if(sheetDesignVariable.getCreatedTime() == null){
        	sheetDesignVariable.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        }
        if(sheetDesignVariable.getCreatedBy() == null){
        	sheetDesignVariable.setCreatedBy(UUID.randomUUID());
        }
        if(sheetDesignVariable.getLastModifiedTime() == null){
        	sheetDesignVariable.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
        }
        if(sheetDesignVariable.getLastModifiedBy() == null){
        	sheetDesignVariable.setLastModifiedBy(UUID.randomUUID());
        }
		return SheetDesignVariableDao.saveVariable(sheetDesignVariable);
	}
	
	@Override
	@Transactional	
	public void updateVariable(SheetDesignVariable sheetDesignVariable, UUID id) {
		 try{
			 	System.out.println("id------>" + id);
			 	UUID userId = sheetDesignVariable.getLastModifiedBy();
				SheetDesignVariable old = SheetDesignVariableDao.getById(id);
				UUID createBy = old.getCreatedBy();
				Timestamp createTime = old.getCreatedTime();
				int ordinal = old.getOrdinal();
				System.out.println(old);
	            BeanUtils.copyProperties(sheetDesignVariable,old);
	            old.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
	            old.setLastModifiedBy(userId);
	            old.setCreatedBy(createBy);
	            old.setCreatedTime(createTime);
	            old.setOrdinal(ordinal);
	            SheetDesignVariableDao.update(old);
	        }catch (Exception e){
	            logger.error("SheetDesignVariableService error:{}", Throwables.getStackTraceAsString(e));
	            throw  new RuntimeException("更新操作失败……");
	        }
	}

	@Override
	public List<SheetDesignVariable> getByDesignId(UUID designId) {
		return SheetDesignVariableDao.getByDesignId(designId);
	}
	
	@Override
	@Transactional	
	public void deleteVariable(UUID id) {
		
		SheetDesignVariableDao.deleteById(id);
		
	}

}
