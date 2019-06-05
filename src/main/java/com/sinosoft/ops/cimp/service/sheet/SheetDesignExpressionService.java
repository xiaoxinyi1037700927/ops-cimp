/**
 * @Project:      IIMP
 * @Title:          SheetDesignCarrierService.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.service.sheet;

import com.sinosoft.ops.cimp.common.model.DefaultTreeNode;
import com.sinosoft.ops.cimp.common.service.BaseEntityService;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignExpression;
import com.sinosoft.ops.cimp.common.model.TreeNode;

import java.util.Collection;
import java.util.List;
import java.util.UUID;




public interface SheetDesignExpressionService extends BaseEntityService<SheetDesignExpression> {
	 /**
     * 按设计标识获取
     * @param designId 设计标识
     * @return SheetDesignExpression
     * @author lixianfu
     * @since JDK 1.7
     */
	Collection<TreeNode> getByType(Collection<SheetDesignExpression> itemList, String type);
	 
	/**
     * 按设计标识获取
     * @param designId 设计标识
     * @return 表格设计表达式
     * @author lixianfu
     * @since JDK 1.7
     */
    Collection<SheetDesignExpression> getByDesignId(UUID designId);

    /**
     *  根据designId删除表的表达式内容
     * @param designId 表格设计标识
     */
    int deleteByDesignId(UUID designId);
    /**
     * 按设计标识修改服务接口
     * @param designId
     * @author lixianfu
     * @date:    2018年6月7日 上午11:13:16
     * @since JDK 1.7
     */
    void updateByDesignId(SheetDesignExpression sheetDesignExpression); 
    /**
     * 按对象进行添加服务接口
     * @param sheetDesignExpression
     * @return	UUID
     * @author lixianfu
     * @date:    2018年6月7日 上午11:17:43
     * @since JDK 1.7
     */
    UUID saveDesignExpression(SheetDesignExpression sheetDesignExpression);

	/**
	 * 根据list获取树的子节点
	 * @param list
	 * @return List<TreeModel>
	 * @author lixianfu
	 * @date:    2018年6月12日 下午10:11:03
	 * @since JDK 1.7
	 */
	List<DefaultTreeNode> conversionTree(Collection<SheetDesignExpression> list);

	boolean moveUp(SheetDesignExpression entity, UUID designId);

	boolean moveDown(SheetDesignExpression entity, UUID designId);

	/**
	 * 
	 * 获取校核公式
	 * @param designId
	 * @return
	 * @author sunch
	 * @date:    2018年8月31日 下午4:19:14
	 * @since JDK 1.7
	 */
	Collection<SheetDesignExpression> getCheckFormulaByDesignId(UUID designId);

	/**
	 * 
	 * 获取运算公式
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