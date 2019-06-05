package com.sinosoft.ops.cimp.repository.sheet;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDao;
import com.sinosoft.ops.cimp.entity.sheet.SheetDataSourceCategory;

import java.util.Collection;
import java.util.UUID;

public interface SheetDataSourceCategoryDao extends BaseEntityDao<SheetDataSourceCategory>{

	Collection<SheetDataSourceCategory> findAllChildren();
	Collection<SheetDataSourceCategory> findByParentId(Integer parentid);
	public Collection<SheetDataSourceCategory> getRootData();
	public String getMaxId();
	Integer getFisrtId();
	SheetDataSourceCategory findPrevious(Integer id);
	int updateOrdinal(Integer preId, int ordinal, UUID userName);
	SheetDataSourceCategory findNext(Integer id);
}
