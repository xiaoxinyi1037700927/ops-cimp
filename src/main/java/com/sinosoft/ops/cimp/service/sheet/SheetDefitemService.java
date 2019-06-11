package com.sinosoft.ops.cimp.service.sheet;


import com.sinosoft.ops.cimp.common.service.BaseEntityService;
import com.sinosoft.ops.cimp.entity.sheet.SheetDefitem;

import java.util.List;

public interface SheetDefitemService extends BaseEntityService<SheetDefitem> {
	List<SheetDefitem> findAll();
	List<SheetDefitem> getByInfoSetId(Integer infoSetId);
}
