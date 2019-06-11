package com.sinosoft.ops.cimp.service.sheet.impl;

import com.google.common.base.Throwables;
import com.sinosoft.ops.cimp.common.service.BaseEntityServiceImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignCell;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignCellDao;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignCellService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import com.sinosoft.ops.cimp.util.BeanUtils;

import java.util.Collection;
import java.util.List;
import java.util.UUID;


/**
 * @ClassName:  SheetDesignCellServiceImpl
 * @description: 表格设计单元格数据访问实现类
 * @author:        lixianfu
 * @date:            2018年6月5日 下午1:26:15
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Service("SheetDesignCellService")
public class SheetDesignCellServiceImpl extends BaseEntityServiceImpl<SheetDesignCell> implements SheetDesignCellService {
	private static final Logger logger = LoggerFactory.getLogger(SheetDesignCellService.class);
	
	@Autowired
	 private SheetDesignCellDao sheetDesignCellDao;
	
	@Override
	@Transactional
	public UUID saveDesignCell(SheetDesignCell sheetDesignCell) {

		return sheetDesignCellDao.saveDesignCell(sheetDesignCell);
	}
	
	@Override
	@Transactional(readOnly=true)
	public SheetDesignCell getById(UUID Id) {
		
		return sheetDesignCellDao.getById(Id);
	}
	
	@Override
	@Transactional
	public void updateDesignCell(SheetDesignCell sheetDesignCell) {
		try {
			System.out.println("id------>" + sheetDesignCell.getId());
			SheetDesignCell old = sheetDesignCellDao.getById(sheetDesignCell.getId());
			System.out.println(old);
			BeanUtils.copyProperties(sheetDesignCell,old); 
			sheetDesignCellDao.updateDesignCell(old);
		} catch (BeansException e) {
			logger.error("sheetDesignCellService error:{}", Throwables.getStackTraceAsString(e));
			throw  new RuntimeException("更新操作失败……");
		}
		
	}
	@Override
	@Transactional
	public int deleteById(UUID Id) {
		try {
			sheetDesignCellDao.deleteById(Id);
			return 1;
		} catch (Exception e) {
			logger.error("sheetDesignCellService error:{}", Throwables.getStackTraceAsString(e));
			throw  new RuntimeException("更新操作失败……");
		}
	}

	@Override
	@Transactional(readOnly=true)
	public SheetDesignCell getByPosition(UUID designId, Integer rowNo, Integer columnNo) {
		Collection<SheetDesignCell> collection = sheetDesignCellDao.getByPosition(designId,rowNo,columnNo);
		Assert.isTrue(collection.size()<2, "同一位置绑定多个属性");
		if(CollectionUtils.isEmpty(collection)){
			return null;
		}else{
			return ((List<SheetDesignCell>)collection).get(0);
		}
	}

	

}
