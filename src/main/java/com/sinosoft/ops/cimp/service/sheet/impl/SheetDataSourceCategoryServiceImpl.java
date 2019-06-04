package com.sinosoft.ops.cimp.service.sheet.impl;

import com.sinosoft.ops.cimp.common.service.BaseEntityServiceImpl;
import com.sinosoft.ops.cimp.dao.sheet.SheetDataSourceCategoryDao;
import com.sinosoft.ops.cimp.dao.sheet.SheetDataSourceDao;
import com.sinosoft.ops.cimp.entity.sheet.SheetDataSource;
import com.sinosoft.ops.cimp.entity.sheet.SheetDataSourceCategory;
import com.sinosoft.ops.cimp.service.sheet.SheetDataSourceCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("SheetDataSourceCategoryService")
public class SheetDataSourceCategoryServiceImpl extends BaseEntityServiceImpl<SheetDataSourceCategory> implements SheetDataSourceCategoryService {

	@Autowired
	private SheetDataSourceCategoryDao sheetDataSourceCategoryDao;
	@Autowired
	private SheetDataSourceDao sheetDataSourceDao;

	@Override
	@Transactional(readOnly=true)
	public Collection<SheetDataSourceCategory> findByParentId(Integer parentid) {
		Collection<SheetDataSourceCategory> collection = sheetDataSourceCategoryDao.findByParentId(parentid);
		return collection;
	}

	@Override
	@Transactional(readOnly=true)
	public List<HashMap<String, Object>> GetCategoryTree() {
		Collection<SheetDataSourceCategory> collection = sheetDataSourceCategoryDao.getRootData();
		List<HashMap<String, Object>> root = new ArrayList<HashMap<String, Object>>();
		for(SheetDataSourceCategory sdsc:collection)
		{
			root.add(RecursionCategory(sdsc));
		}
		return root;
	}

	public HashMap<String, Object> RecursionCategory(SheetDataSourceCategory sdsc) {

		HashMap map= new HashMap<String, Object>();
		map.put("treeid",sdsc.getId());
		map.put("name",sdsc.getName());
		Collection<SheetDataSourceCategory> childs = sheetDataSourceCategoryDao.findByParentId(sdsc.getId());
		if(childs.size()>0)
		{
			List<HashMap<String, Object>> childmap = new ArrayList<HashMap<String, Object>>();
			map.put("leaf",false);
			for(SheetDataSourceCategory child :childs)
			{
				childmap.add(RecursionCategory(child));
			}
			map.put("child",childmap);
		}else
		{
			map.put("leaf",true);
		}
		return map;
	}

	@Override
	@Transactional(readOnly=true)
	public Collection<SheetDataSourceCategory> findAllChildren() {
		Collection<SheetDataSourceCategory> SheetDataSourceCategoryCollection = sheetDataSourceCategoryDao.findAllChildren();
		Collection<SheetDataSourceCategory> collection = new LinkedList<SheetDataSourceCategory>();
		Collection<SheetDataSource> SheetDataSourceCollection = sheetDataSourceDao.findAll();
		Map<Integer,SheetDataSourceCategory> map = new HashMap<Integer,SheetDataSourceCategory>();
		for(SheetDataSourceCategory SheetDataSourceCategory:SheetDataSourceCategoryCollection){
			SheetDataSourceCategory.setLeaf(false);
			map.put(SheetDataSourceCategory.getId(), SheetDataSourceCategory);
		}
		for(SheetDataSource SheetDataSource:SheetDataSourceCollection){
//			Integer key = sheetDataSource.getCategoryId();
//			if(key == null){
//				continue;
//			}
//			sheetDataSource.setLeaf(true);
//			map.get(key).getChildren().add(SheetDataSource);
		}
		for(Integer key:map.keySet()){
			collection.add(map.get(key));
		}
		return collection;
	}

	@Override
	@Transactional(readOnly=true)
	public String getMaxId()
	{
		return sheetDataSourceCategoryDao.getMaxId();
	}

	@Override
	@Transactional
	public Integer getFisrtId() {

		return sheetDataSourceCategoryDao.getFisrtId();
	}

	@Override
	@Transactional
	public boolean moveUp(SheetDataSourceCategory entity) {
		Integer id = entity.getId();
		SheetDataSourceCategory curr = sheetDataSourceCategoryDao.getById(entity.getId());
		int ordinal = curr.getOrdinal();
		UUID userName = entity.getLastModifiedBy();
		SheetDataSourceCategory previous = sheetDataSourceCategoryDao.findPrevious(id);
		if (previous != null) {
			Integer preId = previous.getId();
			int preOrdinal = previous.getOrdinal();
			int cnt = sheetDataSourceCategoryDao.updateOrdinal(preId, ordinal, userName);
			if (cnt > 0) {
				cnt = sheetDataSourceCategoryDao.updateOrdinal(id, preOrdinal, userName);
				if (cnt > 0) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	@Transactional
	public boolean moveDown(SheetDataSourceCategory entity) {
		Integer id = entity.getId();
		SheetDataSourceCategory curr = sheetDataSourceCategoryDao.getById(entity.getId());
		int ordinal = curr.getOrdinal();
		UUID userName = entity.getLastModifiedBy();
		SheetDataSourceCategory nextvious = sheetDataSourceCategoryDao.findNext(id);
		if (nextvious != null) {
			Integer nextId = nextvious.getId();
			int nextOrdinal = nextvious.getOrdinal();
			int cnt = sheetDataSourceCategoryDao.updateOrdinal(nextId, ordinal, userName);
			if (cnt > 0) {
				cnt = sheetDataSourceCategoryDao.updateOrdinal(id, nextOrdinal, userName);
				if (cnt > 0) {
					return true;
				}
			}
		}
		return false;
	}
}
