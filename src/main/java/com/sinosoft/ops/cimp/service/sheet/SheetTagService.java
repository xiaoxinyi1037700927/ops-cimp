package com.sinosoft.ops.cimp.service.sheet;

import com.sinosoft.ops.cimp.common.service.BaseEntityService;
import com.sinosoft.ops.cimp.entity.sheet.SheetTag;

import java.util.Collection;
import java.util.UUID;



/**
 * 
 * @ClassName:  SheetTagService
 * @description: 报表批注服务接口
 */
public interface SheetTagService extends BaseEntityService<SheetTag> {

	Integer deleteBySheetId(UUID sheetId);

	Collection<SheetTag> getBySheetId(UUID sheetId);

}
