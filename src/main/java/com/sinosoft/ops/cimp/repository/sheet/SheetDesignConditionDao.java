package com.sinosoft.ops.cimp.repository.sheet;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDao;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignCondition;

import java.util.Collection;
import java.util.List;
import java.util.UUID;


 
public interface SheetDesignConditionDao extends BaseEntityDao<SheetDesignCondition> {
	public List<SheetDesignCondition> getByDesignId(UUID DesignId);
	public List<SheetDesignCondition> getHasCategoryByDesignId(UUID designId);
	public Collection<SheetDesignCondition> getByConditionId(UUID DesignId);
	SheetDesignCondition getBingData(UUID designId, String sectionNo);
	public SheetDesignCondition findNext(UUID id, UUID designId);
	public SheetDesignCondition findPrevious(UUID id, UUID designId);
	public int updateOrdinal(UUID preId, int ordinal, UUID userName);
	public Collection<SheetDesignCondition> getByDesignIdDistinct(UUID designId);
	public int deleteByDesignIdAndConditionId(UUID designId, UUID conditionId);
	public void deleteByDesignId(UUID designId);
}
