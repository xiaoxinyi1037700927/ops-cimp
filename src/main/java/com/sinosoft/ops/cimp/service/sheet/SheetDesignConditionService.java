package com.sinosoft.ops.cimp.service.sheet;


import com.sinosoft.ops.cimp.common.service.BaseEntityService;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignCondition;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
/**
 * 
 * @ClassName:  SheetDesignSectionService
 * @date:            2018年5月28日 下午2:28:22
 */
public interface SheetDesignConditionService extends BaseEntityService<SheetDesignCondition> {
  
    
   public List<SheetDesignCondition> getByDesignId(UUID designId);
   List<Map<String, Object>> getTreeByDesignId(UUID designId);
   public Collection<SheetDesignCondition> getByConditionId(UUID conditionId);
   SheetDesignCondition getBingData(UUID designId, String sectionNo);
   public boolean moveUp(SheetDesignCondition entity, UUID designId);
   public boolean moveDown(SheetDesignCondition entity, UUID designId);
   public Collection<SheetDesignCondition> getByDesignIdDistinct(UUID designId);
   public int deleteByDesignIdAndConditionId(UUID designId, UUID conditionId);
   public void deleteByDesignId(UUID designId);
}