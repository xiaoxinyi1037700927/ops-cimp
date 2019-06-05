/**
 * @Project:      IIMP
 * @Title:          SheetDesignCellDao.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.repository.sheet;

import java.util.Collection;
import java.util.UUID;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDao;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignExpression;

/**
 * @ClassName:  SheetDesignExpressionDaoImpl
 * @description: 表格设计公式数据访问接口
 * @author:        Wangyg
 * @date:            2018年5月29日 下午6:10:44
 * @version        1.0.0
 * @since            JDK 1.7
 */
public interface SheetDesignExpressionDao extends BaseEntityDao<SheetDesignExpression> {
	/**
     * 按设计标识获取
     * @param designId 设计标识
     * @return 表格设计公式
     * @author Wa
     * @since JDK 1.7
     */
    Collection<SheetDesignExpression> getByDesignId(UUID designId);
    
    /**
     * 按设计标识删除
     * @param designId 设计标识
     * @return 影响的记录数
     * @author Wa
     * @since JDK 1.7
     */
    int deleteByDesignId(UUID Id);
    
    /**
     * 按设计标识修改数据接口
     * @param designId
     * @author lixianfu
     * @date:    2018年6月7日 上午11:13:16
     * @since JDK 1.7
     */
    void updateByDesignId(SheetDesignExpression sheetDesignExpression); 
    /**
     * 按对象进行添加数据接口
     * @param sheetDesignExpression
     * @return	UUID
     * @author lixianfu
     * @date:    2018年6月7日 上午11:17:43
     * @since JDK 1.7
     */
    UUID saveDesignExpression(SheetDesignExpression sheetDesignExpression);

	SheetDesignExpression findPrevious(UUID id, UUID designId);

	int updateOrdinal(UUID nextId, int ordinal, UUID userName);

	SheetDesignExpression findNext(UUID id, UUID designId);

	/**
	 * 
	 * 获取校核表达式
	 * @param designId
	 * @return
	 * @author sunch
	 * @date:    2018年8月31日 下午4:17:58
	 * @since JDK 1.7
	 */
	Collection<SheetDesignExpression> getCheckFormulaByDesignId(UUID designId);

	/**
	 * 
	 * 获取运算表达式
	 * @param designId
	 * @return
	 */
	Collection<SheetDesignExpression> getCaculationFormulaByDesignId(UUID designId);

	/**
	 * 
	 * 获取填报单位表达式
	 * @param designId
	 * @return
	 */
	Collection<SheetDesignExpression> getFillUnitFormulaByDesignId(UUID designId);
   
   
}
    