package com.sinosoft.ops.cimp.repository.sheet;


import com.sinosoft.ops.cimp.common.dao.BaseEntityDao;
import com.sinosoft.ops.cimp.entity.sheet.SheetDefitem;

import java.util.List;

public interface SheetDefitemDao extends BaseEntityDao<SheetDefitem> {
	List<SheetDefitem> findAll();
	List<SheetDefitem> getByInfoSetId(Integer infoSetId);
}
