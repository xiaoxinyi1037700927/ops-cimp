/**
 * @Project:      IIMP
 * @Title:          SheetDesignDesignCategoryDao.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.repository.sheet;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDao;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignDesignCategory;

import java.util.UUID;

/** 
 * @ClassName: SheetDesignDesignCategoryDao
 * @description: 表格设计所属设计分类数据访问接口
 * @author:        Ni
 * @date:            2018年5月25日 下午2:10:56
 * @version        1.0.0
 * @since            JDK 1.7
 */
public interface SheetDesignDesignCategoryDao extends BaseEntityDao<SheetDesignDesignCategory> {
    /** 
     * 根据设计标识删除
     * @param designId 设计标识
     * @author Ni
     * @since JDK 1.7
     */
    int deleteByDesignId(UUID designId);
    
    /** 
     * 根据分类标识删除
     * @param categoryId 分类标识
     * @author Ni
     * @since JDK 1.7
     */
    int deleteByCategoryId(UUID categoryId);
    
    /**
     * 根据设计标识和分类标识删除
     * @param designId 设计标识
     * @param categoryId 分类标识
     * @author Ni
     * @since JDK 1.7
     */
    int deleteByDesignIdAndCategoryId(UUID designId, UUID categoryId);

    SheetDesignDesignCategory getByDesignId(UUID designId, UUID categoryId);
    SheetDesignDesignCategory getByDesignId(UUID designId);
}
