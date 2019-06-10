package com.sinosoft.ops.cimp.service.sheet.impl;

import com.google.common.base.Throwables;
import com.sinosoft.ops.cimp.common.model.DataStatus;
import com.sinosoft.ops.cimp.common.service.BaseEntityServiceImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignSqlParameter;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignSqlParameterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sinosoft.ops.cimp.util.BeanUtils;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignSqlParameterDao;

import java.sql.Timestamp;
import java.util.UUID;


/**
 * @ClassName:  SheetDesignSqlParameterServiceImpl
 * @description: 表设计SQL参数实现类
 * @author:        kanglin
 * @date:            2018年6月6日 下午
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Service("SheetDesignSqlParameterService")
public class SheetDesignSqlParameterServiceImpl extends BaseEntityServiceImpl<SheetDesignSqlParameter> implements SheetDesignSqlParameterService {
	 private static final Logger logger = LoggerFactory.getLogger(SheetDesignSqlParameterServiceImpl.class);

	@Autowired
	 private SheetDesignSqlParameterDao SheetDesignSqlParameterDao;
	@Override
	@Transactional(readOnly=true)
	public SheetDesignSqlParameter getBySqlParameterId(UUID id) {

		return SheetDesignSqlParameterDao.getById(id);
	}
	@Override
	@Transactional	
	public UUID save(SheetDesignSqlParameter sheetDesignEntity) {
		    	if(sheetDesignEntity.getId() == null){
		    		sheetDesignEntity.setId(UUID.randomUUID());
		        }
		    	if(sheetDesignEntity.getDesignSqlId() == null){
		    		sheetDesignEntity.setDesignSqlId(UUID.randomUUID());
		        }
		    	if(sheetDesignEntity.getParameterId() == null){
		    		sheetDesignEntity.setParameterId("");
		        }
		    	if(sheetDesignEntity.getParameterName() == null){
		    		sheetDesignEntity.setParameterName("");
		        }
		        if(sheetDesignEntity.getStatus() == null){
		        	sheetDesignEntity.setStatus(DataStatus.NORMAL.getValue());
		        }
		        if(sheetDesignEntity.getCreatedBy() == null){
		        	sheetDesignEntity.setCreatedBy(UUID.randomUUID());
		        }
		        if(sheetDesignEntity.getLastModifiedTime() == null){
		        	sheetDesignEntity.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
		        }
		        if(sheetDesignEntity.getLastModifiedBy() == null){
		        	sheetDesignEntity.setLastModifiedBy(UUID.randomUUID());
		        }
		      
		return SheetDesignSqlParameterDao.save(sheetDesignEntity);
	}
	@Override
	@Transactional	
	public void update(SheetDesignSqlParameter sheetDesignEntity, UUID id) {
		 try{
				SheetDesignSqlParameter old = SheetDesignSqlParameterDao.getById(id);
	            BeanUtils.copyProperties(sheetDesignEntity,old); 
	            SheetDesignSqlParameterDao.update(old);
	        }catch (Exception e){
	            logger.error("SheetDesignSqlParameterService error:{}", Throwables.getStackTraceAsString(e));
	            throw  new RuntimeException("更新操作失败……");
	        }
		
	}
	@Override
	@Transactional	
	public void delete(UUID id) {
		
		SheetDesignSqlParameterDao.deleteById(id);
		
	}

}
