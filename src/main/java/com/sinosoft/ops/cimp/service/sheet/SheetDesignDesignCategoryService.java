/**
 * @project:     IIMP
 * @title:          SheetDesignDesignCategoryService.java
 * @copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.service.sheet;


import com.sinosoft.ops.cimp.common.service.BaseEntityService;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignDesignCategory;

import java.util.UUID;

/**
 * @ClassName:  SheetDesignDesignCategoryService
 * @description: 表格设计所属分类服务接口
 * @author:        Nil
 * @date:            2018年4月12日 上午9:15:03
 * @version        1.0.0
 */
public interface SheetDesignDesignCategoryService extends BaseEntityService<SheetDesignDesignCategory> {
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

    /**
     * 根据设计标识和分类标识删除
     * @param designId 设计标识
     * @author Ni
     * @since JDK 1.7
     */
    SheetDesignDesignCategory getByDesignId(UUID designId, UUID categoryId);
    SheetDesignDesignCategory getByDesignId(UUID designId);
}
