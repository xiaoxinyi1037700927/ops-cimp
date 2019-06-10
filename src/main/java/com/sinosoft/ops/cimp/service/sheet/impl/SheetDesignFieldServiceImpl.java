package com.sinosoft.ops.cimp.service.sheet.impl;


import com.sinosoft.ops.cimp.common.service.BaseEntityServiceImpl;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoItem;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoSet;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignField;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignFieldDao;
import com.sinosoft.ops.cimp.service.infostruct.SysInfoItemService;
import com.sinosoft.ops.cimp.service.infostruct.SysInfoSetService;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 
 * @ClassName:  SheetDesignFieldServiceImpl
 * @description: SheetDesignField 服务实现类
 * @author:        sunch
 * @date:            2018年6月5日 上午11:22:35
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Service("sheetDesignFieldService")
public class SheetDesignFieldServiceImpl extends BaseEntityServiceImpl<SheetDesignField> implements SheetDesignFieldService {

	@Autowired
	private SheetDesignFieldDao designFieldDao;

	@Autowired
	private SysInfoItemService sysInfoItemService;

	@Autowired
	private SysInfoSetService sysInfoSetService;

	@Override
	@Transactional(readOnly=true)
	public Integer getMaxOrdinal() {
		return designFieldDao.getMaxOrdinal();
	}

	@Override
	@Transactional
	public Collection<SheetDesignField> getByDesignId(UUID designId) {
		return designFieldDao.getByDesignId(designId);
	}

	@Override
	@Transactional
	public void setCountData(SheetDesignField sheetDesignField) {
		if(sheetDesignField.getFunctionName()!=null && sheetDesignField.getFunctionName().equals("count"))
		{
			SysInfoSet sysInfoSet = sysInfoSetService.getByTableName(sheetDesignField.getReferenceTable().toUpperCase());
			Collection<SysInfoItem> sysInfoItems=sysInfoItemService.getBySetId(sysInfoSet.getId());
			if(sysInfoItems.stream().filter(item -> item.getPrimaryKey()).count()>0)
			{
				SysInfoItem sysInfoItem = sysInfoItems.stream().filter(item -> item.getPrimaryKey()).collect(Collectors.toList()).get(0);
				sheetDesignField.setReferenceValueColumn(sysInfoItem.getColumnName());
			}
		}
	}

	@Override
	@Transactional
	public SheetDesignField getBingData(UUID designId,String sectionNo) {
		return designFieldDao.getBingData(designId,sectionNo);
	}

	@Override
	@Transactional
	public boolean moveUp(SheetDesignField entity, UUID designId) {
		UUID id = entity.getId();
		SheetDesignField curr = designFieldDao.getById(entity.getId());
		int ordinal = curr.getOrdinal();
		UUID userName = entity.getLastModifiedBy();
		SheetDesignField previous = designFieldDao.findPrevious(id,designId);
		if (previous != null) {
			UUID preId = previous.getId();
			int preOrdinal = previous.getOrdinal();
			int cnt = designFieldDao.updateOrdinal(preId, ordinal, userName);
			if (cnt > 0) {
				cnt = designFieldDao.updateOrdinal(id, preOrdinal, userName);
				if (cnt > 0) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	@Transactional
	public boolean moveDown(SheetDesignField entity, UUID designId) {
		UUID id = entity.getId();
		SheetDesignField curr = designFieldDao.getById(entity.getId());
		int ordinal = curr.getOrdinal();
		UUID userName = entity.getLastModifiedBy();
		SheetDesignField nextvious = designFieldDao.findNext(id, designId);
		if (nextvious != null) {
			UUID nextId = nextvious.getId();
			int nextOrdinal = nextvious.getOrdinal();
			int cnt = designFieldDao.updateOrdinal(nextId, ordinal, userName);
			if (cnt > 0) {
				cnt = designFieldDao.updateOrdinal(id, nextOrdinal, userName);
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
		designFieldDao.deleteByDesignId(designId);
		designFieldDao.deleteFieldBindingByDesignId(designId);
	}

	@Override
	@Transactional
	public void deleteById(Serializable id){
		designFieldDao.deleteById(id);
		designFieldDao.deleteFieldBindingByFieldId((UUID)id);
	}

}
