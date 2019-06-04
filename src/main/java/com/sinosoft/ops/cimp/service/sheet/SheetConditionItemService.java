package com.sinosoft.ops.cimp.service.sheet;


import com.sinosoft.ops.cimp.common.service.BaseEntityService;
import com.sinosoft.ops.cimp.entity.sheet.SheetConditionItem;

import java.util.List;

public interface SheetConditionItemService extends BaseEntityService<SheetConditionItem> {
    List<SheetConditionItem> findAll();

    List<SheetConditionItem> GetDataByConditionID(String conditionid);

    void save(SheetConditionItem sheetconditonitems);
}
