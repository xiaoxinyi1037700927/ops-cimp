package com.sinosoft.ops.cimp.service.sheet;

import com.sinosoft.ops.cimp.common.service.BaseEntityService;
import com.sinosoft.ops.cimp.entity.sheet.SheetGatherInfo;

import java.util.List;
import java.util.UUID;



public interface SheetGatherInfoService extends BaseEntityService<SheetGatherInfo> {

	void deleteBySheetId(UUID sheetId);

	/**
	 * 
	 * 获取该报表的汇总信息id
	 * @param sheetId
	 * @return
	 */
	List<UUID> getGatherIdsBySheetId(UUID sheetId);

}
