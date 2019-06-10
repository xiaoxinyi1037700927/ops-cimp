package com.sinosoft.ops.cimp.service.infostruct.impl;


import com.sinosoft.ops.cimp.common.service.BaseEntityServiceImpl;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoSetCategory;
import com.sinosoft.ops.cimp.repository.infostruct.SysInfoSetCategoryDao;
import com.sinosoft.ops.cimp.service.infostruct.SysInfoSetCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service("sysInfoSetCategoryService")
public class SysInfoSetCategoryServiceImpl extends BaseEntityServiceImpl<SysInfoSetCategory> implements SysInfoSetCategoryService {

	@Autowired
	private SysInfoSetCategoryDao sysInfoSetCategoryDao = null;
	
	@Override
	@Transactional
	public Collection<SysInfoSetCategory> getAll() {
		return sysInfoSetCategoryDao.getAll();
	}
}
