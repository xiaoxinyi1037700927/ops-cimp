package com.sinosoft.ops.cimp.service.sheet.impl;

import com.sinosoft.ops.cimp.common.service.BaseEntityServiceImpl;
import com.sinosoft.ops.cimp.dao.sheet.SheetDataSourceDao;
import com.sinosoft.ops.cimp.entity.sheet.SheetDataSource;
import com.sinosoft.ops.cimp.service.sheet.SheetDataSourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName:  SheetDataSourceServiceImpl
 * @description: 表设计数据源实现类
 * @author:        kanglin
 * @date:            2018年6月5日 下午
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Service("SheetDataSourceService")
public class SheetDataSourceServiceImpl extends BaseEntityServiceImpl<SheetDataSource> implements SheetDataSourceService {
	 private static final Logger logger = LoggerFactory.getLogger(SheetDataSourceServiceImpl.class);

	@Autowired
	 private SheetDataSourceDao sheetDataSourceDao;
	@Override
	@Transactional(readOnly=true)
	public SheetDataSource getByDataSourceId(UUID id) {

		return sheetDataSourceDao.getById(id);
	}
	@Override
	@Transactional	
	public UUID save(SheetDataSource sheetDesignEntity) {
		      
		return sheetDataSourceDao.save(sheetDesignEntity);
	}
//	@Override
//	@Transactional	
//	public void update(SheetDataSource sheetDesignEntity, UUID id) {
////		 try{
////				SheetDataSource old = sheetDataSourceDao.getById(id);
////	            BeanUtils.copyProperties(sheetDesignEntity,old); 
////	            sheetDataSourceDao.update(old);
////	        }catch (Exception e){
////	            logger.error("SheetDataSourceService error:{}", Throwables.getStackTraceAsString(e));
////	            throw  new RuntimeException("更新操作失败……");
////	        }
//		
//	}
	@Override
	@Transactional	
	public void delete(UUID id) {
		
		sheetDataSourceDao.deleteById(id);
		
	}

	@Override
	public void analyzeSqlExpress(SheetDataSource entity) {
		String sql = entity.getSql();
		List abc= new ArrayList();
		String[] arr= sql.split("\\s{1,}");
		boolean tableflg=false;
		for(int i=0;i<arr.length;i++)
		{
			if(tableflg && !arr.equals(" "))
			{
				tableflg=false;
				abc.add(arr[i]);
			}
			if(arr[i].toLowerCase().equals("join") || arr[i].toLowerCase().equals("from"))
			{
				tableflg=true;
			}
		}
		entity.setDataRange(org.apache.commons.lang.StringUtils.join(abc.toArray(),","));
	}


	@Override
	@Transactional
	public Collection<SheetDataSource> getByCategoryId(int categoryid)
	{
		Collection<SheetDataSource> lst = sheetDataSourceDao.getByCategoryId(categoryid);
		List<Map> lstmap = sheetDataSourceDao.getRefnum(categoryid);
		for(SheetDataSource sheetDataSource:lst)
		{
			List<Map> temp = lstmap.stream().filter(item-> sheetDataSource.getId().toString().equals(item.get("ID"))).collect(Collectors.toList());
			if (temp.size()>0 && temp.get(0).get("REFNUM")!=null)
			{
				sheetDataSource.setRefNum(Integer.parseInt(temp.get(0).get("REFNUM").toString()));
			}
		}

		return lst;
	}

	@Override
	@Transactional
	public List<Map> getRefSituation(String id)
	{
		return sheetDataSourceDao.getRefSituation(id);
	}

}
