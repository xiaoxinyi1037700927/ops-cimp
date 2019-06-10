/**
 * @project:     IIMP
 * @title:          SheetSheetCategoryService.java
 * @copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.service.sheet;

import com.sinosoft.ops.cimp.common.service.BaseEntityService;
import com.sinosoft.ops.cimp.entity.sheet.SheetSheetCategory;

import java.util.UUID;



/**
 * @ClassName:  SheetSheetCategoryService
 * @description: 表格所属分类服务接口
 * @author:        Nil
 * @date:            2018年4月12日 上午9:15:03
 * @version        1.0.0
 */
public interface SheetSheetCategoryService extends BaseEntityService<SheetSheetCategory> {
    /** 
     * 根据表格标识删除
     * @param sheetId 表格标识
     * @author Ni
     * @since JDK 1.7
     */
    int deleteBySheetId(UUID sheetId);
    
    /** 
     * 根据分类标识删除
     * @param categoryId 分类标识
     * @author Ni
     * @since JDK 1.7
     */
    int deleteByCategoryId(UUID categoryId);
    
    /**
     * 根据表格标识和分类标识删除
     * @param sheetId 表格标识
     * @param categoryId 分类标识
     * @author Ni
     * @since JDK 1.7
     */
    int deleteBySheetIdAndCategoryId(UUID sheetId, UUID categoryId);

	SheetSheetCategory getBySheetId(UUID sheetId);    
}
