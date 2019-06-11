package com.sinosoft.ops.cimp.service.sheet.impl;

import com.sinosoft.ops.cimp.common.service.BaseEntityServiceImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignFieldBinding;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignFieldBindingDao;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignFieldBindingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.UUID;



@Service
public class SheetDesignFieldBindingServiceImpl extends BaseEntityServiceImpl<SheetDesignFieldBinding> implements SheetDesignFieldBindingService {

	@Autowired
	private SheetDesignFieldBindingDao sheetDesignFieldBindingDao;
	
	@Override
	@Transactional(readOnly=true)
	public Collection<SheetDesignFieldBinding> getByFieldId(UUID fieldId) {
		return sheetDesignFieldBindingDao.getByFieldId(fieldId);
	}

	@Override
	@Transactional
	public void deleteByDesignId(UUID designId) {
		sheetDesignFieldBindingDao.deleteByDesignId(designId);
		
	}

	
}
