package com.sinosoft.ops.cimp.service.sheet;

import com.sinosoft.ops.cimp.common.service.BaseEntityService;
import com.sinosoft.ops.cimp.entity.sheet.SheetDataSourceCategory;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public interface SheetDataSourceCategoryService extends BaseEntityService<SheetDataSourceCategory>{

	Collection<SheetDataSourceCategory> findAllChildren();
	Collection<SheetDataSourceCategory> findByParentId(Integer parentid);
	List<HashMap<String, Object>> GetCategoryTree();
	public String getMaxId();
	Integer getFisrtId();
	boolean moveUp(SheetDataSourceCategory entity);
	boolean moveDown(SheetDataSourceCategory entity);
}
