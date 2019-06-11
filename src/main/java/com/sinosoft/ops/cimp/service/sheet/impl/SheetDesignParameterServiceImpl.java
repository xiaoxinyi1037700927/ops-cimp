/**
 * @Project:      IIMP
 * @Title:          SheetDesignCarrierServiceImpl.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.service.sheet.impl;


import com.sinosoft.ops.cimp.common.service.BaseEntityServiceImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignParameter;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignParameterDao;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.UUID;

/**
 * @ClassName:  SheetDesignParameterServiceImpl
 * @Description: 表格设计参数服务实现
 * @Version        1.0.0
 */
@Service("sheetDesignParameterService")
public class SheetDesignParameterServiceImpl extends BaseEntityServiceImpl<SheetDesignParameter> implements SheetDesignParameterService {

	@Autowired
	private SheetDesignParameterDao sheetDesignParameterDao;

	@Transactional
	@Override
	public Collection<SheetDesignParameter> getByDesignId(UUID designId)
	{
		return sheetDesignParameterDao.getByDesignId(designId);
	}

	@Override
	@Transactional
	public boolean moveDown(SheetDesignParameter entity, UUID designId) {
		UUID id = entity.getId();
		SheetDesignParameter curr = sheetDesignParameterDao.getById(entity.getId());
		int ordinal = curr.getOrdinal();
		UUID userName = entity.getLastModifiedBy();
		SheetDesignParameter nextvious = sheetDesignParameterDao.findNext(id, designId);
		if (nextvious != null) {
			UUID nextId = nextvious.getId();
			int nextOrdinal = nextvious.getOrdinal();
			int cnt = sheetDesignParameterDao.updateOrdinal(nextId, ordinal, userName);
			if (cnt > 0) {
				cnt = sheetDesignParameterDao.updateOrdinal(id, nextOrdinal, userName);
				if (cnt > 0) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	@Transactional
	public boolean moveUp(SheetDesignParameter entity, UUID designId) {
		UUID id = entity.getId();
		SheetDesignParameter curr = sheetDesignParameterDao.getById(entity.getId());
		int ordinal = curr.getOrdinal();
		UUID userName = entity.getLastModifiedBy();
		SheetDesignParameter previous = sheetDesignParameterDao.findPrevious(id,designId);
		if (previous != null) {
			UUID preId = previous.getId();
			int preOrdinal = previous.getOrdinal();
			int cnt = sheetDesignParameterDao.updateOrdinal(preId, ordinal, userName);
			if (cnt > 0) {
				cnt = sheetDesignParameterDao.updateOrdinal(id, preOrdinal, userName);
				if (cnt > 0) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	@Transactional
	public void deleteByDesignId(UUID designId) {
		sheetDesignParameterDao.deleteByDesignId(designId);
	}

}