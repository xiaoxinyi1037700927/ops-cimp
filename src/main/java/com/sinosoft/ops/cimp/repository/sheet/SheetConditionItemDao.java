package com.sinosoft.ops.cimp.repository.sheet;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDao;
import com.sinosoft.ops.cimp.entity.sheet.SheetConditionItem;

import java.util.List;

public interface SheetConditionItemDao extends BaseEntityDao<SheetConditionItem> {

    List<SheetConditionItem> findAll();

    List<SheetConditionItem> GetDataByConditionID(String conditionid);
}
