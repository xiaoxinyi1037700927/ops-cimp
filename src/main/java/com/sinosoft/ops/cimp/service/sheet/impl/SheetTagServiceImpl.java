package com.sinosoft.ops.cimp.service.sheet.impl;


import com.sinosoft.ops.cimp.common.service.BaseEntityServiceImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetTag;
import com.sinosoft.ops.cimp.repository.sheet.SheetTagDao;
import com.sinosoft.ops.cimp.service.sheet.SheetTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.UUID;

@Service
public class SheetTagServiceImpl extends BaseEntityServiceImpl<SheetTag> implements SheetTagService {

	@Autowired
	private SheetTagDao sheetTagDao;
	
	@Override
	@Transactional
	public Integer deleteBySheetId(UUID sheetId) {
		return sheetTagDao.deleteBySheetId(sheetId);
	}

	@Override
	@Transactional(readOnly=true)
	public Collection<SheetTag> getBySheetId(UUID sheetId) {
		return sheetTagDao.getBySheetId(sheetId);
	}

	
}
