package com.sinosoft.ops.cimp.service.sheet.impl;

import com.alibaba.fastjson.serializer.JavaBeanSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.sinosoft.ops.cimp.common.service.BaseEntityServiceImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetCondition;
import com.sinosoft.ops.cimp.entity.sheet.SheetConditionCategory;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignCondition;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignConditionDao;
import com.sinosoft.ops.cimp.service.sheet.SheetConditionCategoryService;
import com.sinosoft.ops.cimp.service.sheet.SheetConditionService;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;



@Service("sheetDesignConditionService")
@Transactional(readOnly=true)
public class SheetDesignConditionServiceImpl extends BaseEntityServiceImpl<SheetDesignCondition> implements SheetDesignConditionService {

	@Autowired
	private SheetDesignConditionDao sheetDesignConditionDao;

	@Autowired
	private SheetConditionService sheetConditionService;

	@Autowired
	private SheetConditionCategoryService sheetConditionCategoryService;

	@Override
	@Transactional
	public List<SheetDesignCondition> getByDesignId(UUID designId) {
		return sheetDesignConditionDao.getByDesignId(designId);
	}


	@Override
	@Transactional
	public List<Map<String, Object>> getTreeByDesignId(UUID designId) {
		List<SheetDesignCondition> sheetDesignConditions=sheetDesignConditionDao.getHasCategoryByDesignId(designId);

		List<SheetDesignCondition> lstReturn= new ArrayList<>();
		if(sheetDesignConditions.size()==0) return new ArrayList<>();

		Map<String,SheetDesignCondition> map = new LinkedHashMap<>();
		for(SheetDesignCondition sheetDesignCondition:sheetDesignConditions)
		{
			map.put(sheetDesignCondition.getConditionId().toString(),sheetDesignCondition);
		}

		for(String key :map.keySet())
		{
			lstReturn.add(map.get(key));
		}

		List<UUID> ids =new ArrayList<>();
		for(SheetDesignCondition sheetDesignCondition : lstReturn)
		{
			SheetCondition sheetCondition = sheetConditionService.getById(sheetDesignCondition.getConditionId());
			if(sheetCondition.getCategoryId()!=null)
			{
				SheetConditionCategory sheetConditionCategory=sheetConditionCategoryService.getById(sheetCondition.getCategoryId());
				ids = RecursionUp(sheetConditionCategory,ids);
			}
		}
		List<Map<String, Object>> root = sheetConditionCategoryService.getCategoryTreeByIds(ids);
		for(SheetDesignCondition sheetDesignCondition : lstReturn)
		{
			SheetCondition sheetCondition = sheetConditionService.getById(sheetDesignCondition.getConditionId());
			root = RecursionDown(sheetDesignCondition,sheetCondition,root);
		}
		return root;
	}

	private List<UUID> RecursionUp(SheetConditionCategory sheetConditionCategory,List<UUID> ids)
	{
		if(!ids.contains(sheetConditionCategory.getId()))
			ids.add(sheetConditionCategory.getId());
		if(sheetConditionCategory.getParentId()==null)
		{
			return ids;
		}
		else
		{
			SheetConditionCategory tmp=sheetConditionCategoryService.getById(sheetConditionCategory.getParentId());
			return RecursionUp(tmp,ids);
		}
	}

	private List<Map<String, Object>> RecursionDown(SheetDesignCondition sheetDesignCondition,SheetCondition sheetCondition,List<Map<String, Object>> root)
	{
		try
		{
			for(Map map :root)
			{
				if(map.get("id").toString().equals(sheetCondition.getCategoryId().toString()))
				{
					map.put("leaf",false);
					if(map.containsKey("child")) {
						List<Map<String, Object>> lstmap = (List<Map<String, Object>>) map.get("child");
						Map temp=new HashMap();
						Class<?> clazz = sheetDesignCondition.getClass();
						ObjectSerializer serializer = SerializeConfig.globalInstance.getObjectWriter(clazz);
						JavaBeanSerializer javaBeanSerializer = (JavaBeanSerializer) serializer;
						if (serializer instanceof JavaBeanSerializer) {
							temp = javaBeanSerializer.getFieldValuesMap(sheetDesignCondition);
						}
						temp.put("leaf",true);
						temp.put("name",temp.get("conditionName"));
						lstmap.add(temp);
					}else
					{
						List<Map<String, Object>> lstmap= new ArrayList<>();
						Map temp=new HashMap();
						Class<?> clazz = sheetDesignCondition.getClass();
						ObjectSerializer serializer = SerializeConfig.globalInstance.getObjectWriter(clazz);
						JavaBeanSerializer javaBeanSerializer = (JavaBeanSerializer) serializer;
						if (serializer instanceof JavaBeanSerializer) {
							temp = javaBeanSerializer.getFieldValuesMap(sheetDesignCondition);
						}
						temp.put("leaf",true);
						temp.put("name",temp.get("conditionName"));
						lstmap.add(temp);
						map.put("child",lstmap);
					}
					break;
				}
				else if(map.containsKey("child"))
				{
					List<Map<String, Object>> lstmap = (List<Map<String, Object>>)map.get("child");
					RecursionDown(sheetDesignCondition,sheetCondition,lstmap);
				}

			}
		}
		catch (Exception e)
		{

		}

		return root;
	}

	@Override
	@Transactional
	public Collection<SheetDesignCondition> getByConditionId(UUID conditionId) {
	
		return sheetDesignConditionDao.getByConditionId(conditionId);
		
	}

	@Override
	@Transactional
	public SheetDesignCondition getBingData(UUID designId,String sectionNo) {
		return sheetDesignConditionDao.getBingData(designId,sectionNo);
	}

	@Override
	@Transactional
	public boolean moveUp(SheetDesignCondition entity, UUID designId) {
		UUID id = entity.getId();
		SheetDesignCondition curr = sheetDesignConditionDao.getById(entity.getId());
		int ordinal = curr.getOrdinal();
		UUID userName = entity.getLastModifiedBy();
		SheetDesignCondition previous = sheetDesignConditionDao.findPrevious(id,designId);
		if (previous != null) {
			UUID preId = previous.getId();
			int preOrdinal = previous.getOrdinal();
			int cnt = sheetDesignConditionDao.updateOrdinal(preId, ordinal, userName);
			if (cnt > 0) {
				cnt = sheetDesignConditionDao.updateOrdinal(id, preOrdinal, userName);
				if (cnt > 0) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	@Transactional
	public boolean moveDown(SheetDesignCondition entity, UUID designId) {
		UUID id = entity.getId();
		SheetDesignCondition curr = sheetDesignConditionDao.getById(entity.getId());
		int ordinal = curr.getOrdinal();
		UUID userName = entity.getLastModifiedBy();
		SheetDesignCondition nextvious = sheetDesignConditionDao.findNext(id, designId);
		if (nextvious != null) {
			UUID nextId = nextvious.getId();
			int nextOrdinal = nextvious.getOrdinal();
			int cnt = sheetDesignConditionDao.updateOrdinal(nextId, ordinal, userName);
			if (cnt > 0) {
				cnt = sheetDesignConditionDao.updateOrdinal(id, nextOrdinal, userName);
				if (cnt > 0) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	@Transactional(readOnly=true)
	public Collection<SheetDesignCondition> getByDesignIdDistinct(UUID designId) {
		return sheetDesignConditionDao.getByDesignIdDistinct(designId);
	}

	@Override
	@Transactional
	public int deleteByDesignIdAndConditionId(UUID designId, UUID conditionId) {
		
		return sheetDesignConditionDao.deleteByDesignIdAndConditionId(designId,conditionId);
	}

	@Override
	@Transactional
	public void deleteByDesignId(UUID designId) {
		sheetDesignConditionDao.deleteByDesignId(designId);
		
	}
}
