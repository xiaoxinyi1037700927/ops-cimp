/**
 * @Project:      IIMP
 * @Title:          SheetDesignCarrierServiceImpl.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.service.sheet.impl;


import com.sinosoft.ops.cimp.common.service.BaseEntityServiceImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetData;
import com.sinosoft.ops.cimp.repository.sheet.SheetDataDao;
import com.sinosoft.ops.cimp.service.sheet.SheetDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName:  SheetDataServiceImpl
 * @Description: 表格设计载体服务实现
 * @Author:        Nil
 * @Date:            2017年8月18日 下午1:26:15
 * @Version        1.0.0
 */
@Service("sheetDataService")
public class SheetDataServiceImpl extends BaseEntityServiceImpl<SheetData> implements SheetDataService {
	@Autowired
	private SheetDataDao sheetDataDao;
	
	public SheetDataServiceImpl(){

	}
	
    @Override
    @Transactional(readOnly=true)
    public Collection<SheetData> getBySheetId(UUID id) {
        return sheetDataDao.getBySheetId(id);
    }
    
    @Override
	@Transactional(readOnly=true)
	public Collection<SheetData> getBySheetId(String id) {
		return sheetDataDao.getBySheetId(id);
	}

	@Override
	@Transactional
	public void deleteBySheetId(UUID sheetId) {
		sheetDataDao.deleteBySheetId(sheetId);
		
	}

	@Override
	@Transactional(readOnly=true)
	public Collection<Map<String, Object>> getBuildWordDatas(UUID sheetId) {
		return sheetDataDao.getBuildWordDatas(sheetId);
	}

	
}