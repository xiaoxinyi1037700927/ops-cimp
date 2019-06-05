package com.sinosoft.ops.cimp.repository.sheet;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDao;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignSection;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

 
public interface SheetDesignSectionDao extends BaseEntityDao<SheetDesignSection> {
	public Collection<SheetDesignSection> getByDesignId(UUID DesignId);

	public Integer getMaxOrdinal();
	
	public String getMaxSectionNoByDesignId(UUID designId);
	
	/**
	 * 
	 * 根据表格设计id获取数据块（可排序）
	 * @param designId
	 * @param orderField  排序字段
	 * @param desc  是否降序
	 */
	public Collection<SheetDesignSection> getByDesignId(UUID designId, String orderField, boolean desc);

	/**根据模板id删除数据块*/
	public void deleteByDesignId(UUID designId);

	public SheetDesignSection findPrevious(UUID id, UUID designId);

	public int updateOrdinal(UUID preId, int ordinal, UUID userName);

	public SheetDesignSection findNext(UUID id, UUID designId);

	public Map<String, Integer> getRangeByDesignId(UUID designId);


}
