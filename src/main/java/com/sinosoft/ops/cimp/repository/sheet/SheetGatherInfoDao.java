package com.sinosoft.ops.cimp.repository.sheet;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDao;
import com.sinosoft.ops.cimp.entity.sheet.SheetGatherInfo;

import java.util.List;
import java.util.UUID;


public interface SheetGatherInfoDao extends BaseEntityDao<SheetGatherInfo> {

	void deleteBySheetId(UUID sheetId);

	List<UUID> getGatherIdsBySheetId(UUID sheetId);

}
