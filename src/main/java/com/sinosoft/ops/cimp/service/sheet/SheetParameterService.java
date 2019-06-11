package com.sinosoft.ops.cimp.service.sheet;


import com.sinosoft.ops.cimp.common.service.BaseEntityService;
import com.sinosoft.ops.cimp.entity.sheet.SheetParameter;

import java.util.Collection;
import java.util.UUID;

public interface SheetParameterService extends BaseEntityService<SheetParameter> {

    Collection<SheetParameter> getBySheetId(UUID sheetId);

	void deleteBySheetId(UUID sheetId);
}
