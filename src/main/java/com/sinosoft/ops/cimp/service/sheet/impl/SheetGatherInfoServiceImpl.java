package com.sinosoft.ops.cimp.service.sheet.impl;

import com.sinosoft.ops.cimp.common.service.BaseEntityServiceImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetGatherInfo;
import com.sinosoft.ops.cimp.repository.sheet.SheetGatherInfoDao;
import com.sinosoft.ops.cimp.service.sheet.SheetGatherInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;



@Service
public class SheetGatherInfoServiceImpl extends BaseEntityServiceImpl<SheetGatherInfo> implements SheetGatherInfoService {

	@Autowired
	private SheetGatherInfoDao sheetGatherInfoDao;

	@Override
	@Transactional
	public void deleteBySheetId(UUID sheetId) {
		sheetGatherInfoDao.deleteBySheetId(sheetId);
		
	}

	@Override
	@Transactional(readOnly=true)
	public List<UUID> getGatherIdsBySheetId(UUID sheetId) {
		return sheetGatherInfoDao.getGatherIdsBySheetId(sheetId);
	}
}
