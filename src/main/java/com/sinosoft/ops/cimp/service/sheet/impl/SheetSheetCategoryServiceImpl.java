/**
 * @project:     IIMP
 * @title:          SheetSheetCategoryServiceImpl.java
 * @copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.service.sheet.impl;


import com.sinosoft.ops.cimp.common.service.BaseEntityServiceImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetSheetCategory;
import com.sinosoft.ops.cimp.repository.sheet.SheetSheetCategoryDao;
import com.sinosoft.ops.cimp.service.sheet.SheetSheetCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @ClassName:  SheetSheetCategoryServiceImpl
 * @description:  表格所属分类服务实现类
 * @author:        Nil
 * @date:            2018年4月12日 上午9:15:26
 * @version        1.0.0
 */
@Service("sheetSheetCategoryService")
public class SheetSheetCategoryServiceImpl extends BaseEntityServiceImpl<SheetSheetCategory> implements SheetSheetCategoryService {
    @Autowired
    private SheetSheetCategoryDao sheetSheetCategoryDao;
    
    @Transactional
    @Override
    public int deleteBySheetId(UUID sheetId) {
        return sheetSheetCategoryDao.deleteBySheetId(sheetId);
    }

    @Transactional
    @Override
    public int deleteByCategoryId(UUID categoryId) {
        return sheetSheetCategoryDao.deleteByCategoryId(categoryId);
    }

    @Transactional
    @Override
    public int deleteBySheetIdAndCategoryId(UUID sheetId, UUID categoryId) {
        return sheetSheetCategoryDao.deleteBySheetIdAndCategoryId(sheetId, categoryId);
    }

	@Override
	@Transactional(readOnly=true)
	public SheetSheetCategory getBySheetId(UUID sheetId) {
		return sheetSheetCategoryDao.getBySheetId(sheetId);
	}
}
