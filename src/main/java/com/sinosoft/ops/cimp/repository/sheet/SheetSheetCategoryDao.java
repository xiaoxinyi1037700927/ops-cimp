/**
 * @Project:      IIMP
 * @Title:          SheetSheetCategoryDao.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.repository.sheet;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDao;
import com.sinosoft.ops.cimp.entity.sheet.SheetSheetCategory;

import java.util.UUID;



/** 
 * @ClassName:  SheetSheetCategoryDao
 * @description: 表格所属分类数据访问接口
 * @author:        Ni
 * @date:            2018年5月25日 下午2:10:56
 * @version        1.0.0
 * @since            JDK 1.7
 */
public interface SheetSheetCategoryDao extends BaseEntityDao<SheetSheetCategory> {
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
