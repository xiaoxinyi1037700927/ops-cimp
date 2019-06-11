package com.sinosoft.ops.cimp.service.sheet;


import com.sinosoft.ops.cimp.common.service.BaseEntityService;
import com.sinosoft.ops.cimp.entity.sheet.SheetConditionItem;

import java.util.List;
import java.util.UUID;

public interface SheetConditionItemService extends BaseEntityService<SheetConditionItem> {
    List<SheetConditionItem> findAll();

    List<SheetConditionItem> GetDataByConditionID(UUID conditionid);

    void save(SheetConditionItem sheetconditonitems);
}
