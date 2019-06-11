/**
 * @project:     IIMP
 * @title:          SheetDesignDesignCategoryServiceImpl.java
 * @copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.service.sheet.impl;


import com.sinosoft.ops.cimp.common.service.BaseEntityServiceImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignDesignCategory;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignDesignCategoryDao;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignDesignCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @ClassName:  SheetDesignDesignCategoryServiceImpl
 * @description:  表格所属分类服务实现类
 * @author:        Nil
 * @date:            2018年4月12日 上午9:15:26
 * @version        1.0.0
 */
@Service("sheetDesignDesignCategoryService")
public class SheetDesignDesignCategoryServiceImpl extends BaseEntityServiceImpl<SheetDesignDesignCategory> implements SheetDesignDesignCategoryService {

    @Autowired
    private SheetDesignDesignCategoryDao sheetDesignDesignCategoryDao;
    
    @Transactional
    @Override
    public int deleteByDesignId(UUID designId) {
        return sheetDesignDesignCategoryDao.deleteByDesignId(designId);
    }

    @Transactional
    @Override
    public int deleteByCategoryId(UUID categoryId) {
        return sheetDesignDesignCategoryDao.deleteByCategoryId(categoryId);
    }

    @Transactional
    @Override
    public int deleteByDesignIdAndCategoryId(UUID designId, UUID categoryId) {
        return sheetDesignDesignCategoryDao.deleteByDesignIdAndCategoryId(designId, categoryId);
    }

    @Override
    @Transactional(readOnly=true)
    public SheetDesignDesignCategory getByDesignId(UUID designId,UUID categoryId) {
        return sheetDesignDesignCategoryDao.getByDesignId(designId,categoryId);
    }

    @Override
    @Transactional(readOnly=true)
    public SheetDesignDesignCategory getByDesignId(UUID designId) {
        return sheetDesignDesignCategoryDao.getByDesignId(designId);
    }
}
