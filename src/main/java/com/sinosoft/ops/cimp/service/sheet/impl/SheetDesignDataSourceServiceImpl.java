package com.sinosoft.ops.cimp.service.sheet.impl;


import com.sinosoft.ops.cimp.common.service.BaseEntityServiceImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetDataSourceCategory;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignDataSource;
import com.sinosoft.ops.cimp.repository.sheet.SheetDataSourceCategoryDao;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignDataSourceDao;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignDataSourceService;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @ClassName:  SheetDesignDataSourceServiceImpl
 * @description: 表设计数据源实现类
 * @author:        kanglin
 * @date:            2018年6月5日 下午
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Service("sheetDesignDataSourceService")
public class SheetDesignDataSourceServiceImpl extends BaseEntityServiceImpl<SheetDesignDataSource> implements SheetDesignDataSourceService {
	 private static final Logger logger = LoggerFactory.getLogger(SheetDesignDataSourceServiceImpl.class);

	@Autowired
	 private SheetDesignDataSourceDao sheetDesignDataSourceDao;

	@Autowired
	private SheetDataSourceCategoryDao sheetDataSourceCategoryDao;

	@Override
	@Transactional(readOnly=true)
	public SheetDesignDataSource getByDataSourceId(UUID id) {

		return sheetDesignDataSourceDao.getById(id);
	}
	@Override
	@Transactional	
	public UUID save(SheetDesignDataSource sheetDesignEntity) {
		      
		return sheetDesignDataSourceDao.save(sheetDesignEntity);
	}
//	@Override
//	@Transactional	
//	public void update(SheetDesignDataSource sheetDesignEntity, UUID id) {
////		 try{
////				SheetDesignDataSource old = sheetDesignDataSourceDao.getById(id);
////	            BeanUtils.copyProperties(sheetDesignEntity,old); 
////	            sheetDesignDataSourceDao.update(old);
////	        }catch (Exception e){
////	            logger.error("SheetDesignDataSourceService error:{}", Throwables.getStackTraceAsString(e));
////	            throw  new RuntimeException("更新操作失败……");
////	        }
//		
//	}
	@Override
	@Transactional	
	public void delete(UUID id) {
		
		sheetDesignDataSourceDao.deleteById(id);
		
	}
	@Override
	@Transactional
	public Integer getMaxOrdinal() {
		return sheetDesignDataSourceDao.getMaxOrdinal();
	}
	
	@Override
	public void analyzeSqlExpress(SheetDesignDataSource entity,String sql) throws JSQLParserException {
		
		CCJSqlParserManager parser = new CCJSqlParserManager();
		Statement statement = parser.parse(new StringReader(sql));
		StringBuilder tableNames = new StringBuilder();
		if(statement instanceof Select){
			Select selectStatement = (Select)statement;
			TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
			List tableList = tablesNamesFinder.getTableList(selectStatement);
			
			for (Object object : tableList) {
				tableNames.append(object.toString()+",");
			}
			tableNames.deleteCharAt(tableNames.length()-1);
		}
		
		entity.setDataRange(tableNames.toString());
	}
	
	@Override
	@Transactional
	public Collection<SheetDesignDataSource> getByDesignId(UUID designId) {
		return sheetDesignDataSourceDao.getByDesignId(designId);
	}

	@Override
	@Transactional
	public List<Map<String, Object>> GetTree(UUID designId) {
		Collection<SheetDesignDataSource> sheetDesignDataSources =  sheetDesignDataSourceDao.getByDesignId(designId);

		Collection<SheetDataSourceCategory> collection = sheetDataSourceCategoryDao.getRootData();
		List<Map<String, Object>> root = new ArrayList<Map<String, Object>>();
		for(SheetDataSourceCategory sdsc:collection)
		{
			Map temp =RecursionCategory(sdsc,sheetDesignDataSources);
			if(temp!=null) root.add(temp);
		}
		return root;
	}


	public Map<String, Object> RecursionCategory(SheetDataSourceCategory sdsc,Collection<SheetDesignDataSource> sheetDesignDataSources) {

		Map map = new HashMap<String, Object>();
		map.put("treeid", sdsc.getId());
		map.put("name", sdsc.getName());
		Collection<SheetDataSourceCategory> childs = sheetDataSourceCategoryDao.findByParentId(sdsc.getId());
		if (childs.size() > 0) {
			List<Map<String, Object>> childmap = new ArrayList<Map<String, Object>>();
			map.put("leaf", false);
			for (SheetDataSourceCategory child : childs) {
				Map temp =RecursionCategory(child,sheetDesignDataSources);
				if(temp!=null)  childmap.add(temp);
			}
			map.put("child", childmap);
			if (sheetDesignDataSources.stream().filter(item -> item.getCategoryId() == sdsc.getId()).collect(Collectors.toList()).size() > 0) {
				childmap = new ArrayList<Map<String, Object>>();
				for (SheetDesignDataSource entity : sheetDesignDataSources.stream().filter(item -> item.getCategoryId() == sdsc.getId()).collect(Collectors.toList())) {
					Map mapentity = new HashMap<String, Object>();
					mapentity.put("treeid", entity.getId());
					mapentity.put("name", entity.getName());
					mapentity.put("dataRange", entity.getDataRange());
					mapentity.put("leaf", true);
					childmap.add(mapentity);
				}
				map.put("leaf", false);
				map.put("child", childmap);
			}
		} else {
			if (sheetDesignDataSources.stream().filter(item -> item.getCategoryId() == sdsc.getId()).collect(Collectors.toList()).size() > 0) {
				List<Map<String, Object>> childmap = new ArrayList<Map<String, Object>>();
				for (SheetDesignDataSource entity : sheetDesignDataSources.stream().filter(item -> item.getCategoryId() == sdsc.getId()).collect(Collectors.toList())) {
					Map mapentity = new HashMap<String, Object>();
					mapentity.put("treeid", entity.getId());
					mapentity.put("name", entity.getName());
					mapentity.put("dataRange", entity.getDataRange());
					mapentity.put("leaf", true);
					childmap.add(mapentity);
				}
				map.put("leaf", false);
				map.put("child", childmap);
			}
			else
			{
				return null;
			}
		}
		return map;
	}

	@Override
	@Transactional
	public SheetDesignDataSource getBingData(UUID designId,String sectionNo) {
		return sheetDesignDataSourceDao.getBingData(designId,sectionNo);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Collection<SheetDesignDataSource> getByDesignIdDistinct(UUID designId) {
		Collection<SheetDesignDataSource> sources = sheetDesignDataSourceDao.getByDesignIdDistinct(designId);
		return sources;
	}
	
	@Override
	@Transactional
	public int deleteByDesignIdAndDataSourceId(UUID designId,UUID dataSourceId) {
		return sheetDesignDataSourceDao.deleteByDesignIdAndDataSourceId(designId,dataSourceId);
	}
	@Override
	@Transactional
	public void deleteByDesignId(UUID designId) {
		sheetDesignDataSourceDao.deleteByDesignId(designId);
	}
}
