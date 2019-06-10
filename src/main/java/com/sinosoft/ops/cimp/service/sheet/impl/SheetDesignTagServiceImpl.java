package com.sinosoft.ops.cimp.service.sheet.impl;

import com.google.common.base.Throwables;
import com.sinosoft.ops.cimp.common.service.BaseEntityServiceImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignTag;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignTagDao;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignTagService;
import com.sinosoft.ops.cimp.util.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


/**
 * @ClassName:  SheetDesignTagServiceImpl
 * @description: 表标签设计服务接口实现
 * @author:        lixianfu
 * @date:            2018年6月6日 下午3:22:28
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Service("SheetDesignTagService")
public class SheetDesignTagServiceImpl extends BaseEntityServiceImpl<SheetDesignTag> implements SheetDesignTagService {
	private static final Logger Logger = LoggerFactory.getLogger(SheetDesignTagService.class);
	@Autowired
	private SheetDesignTagDao sheetDesignTagDao;
	
	@Override
	@Transactional
	public UUID saveDesignTag(SheetDesignTag sheetDesignTag) {
		System.out.println(sheetDesignTag+"=================");
		return sheetDesignTagDao.saveDesignTag(sheetDesignTag);
	}

	@Override
	@Transactional(readOnly=true)
	public SheetDesignTag getById(UUID id) {
		
		return sheetDesignTagDao.getById(id);
	}

	@Override
	@Transactional
	public void updateDesignTag(SheetDesignTag sheetDesignTag) {
		try {
			SheetDesignTag old = sheetDesignTagDao.getById(sheetDesignTag.getId());
            BeanUtils.copyProperties(sheetDesignTag,old); 
			sheetDesignTagDao.updateDesignTag(old);
		} catch (Exception e) {
			Logger.error("SheetDesignVariableService error:{}", Throwables.getStackTraceAsString(e));//注意Logger大小写
			throw  new RuntimeException("更新操作失败……");
		}

	}

	@Override
	@Transactional
	public void deleteDesignTag(UUID id) {
		
		sheetDesignTagDao.deleteById(id);
		
	}

}
