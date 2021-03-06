package com.sinosoft.ops.cimp.repository.sheet;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDao;
import com.sinosoft.ops.cimp.entity.sheet.SheetConditionItem;

import java.util.List;
import java.util.UUID;

public interface SheetConditionItemDao extends BaseEntityDao<SheetConditionItem> {

    List<SheetConditionItem> findAll();

    List<SheetConditionItem> GetDataByConditionID(UUID conditionid);
}
