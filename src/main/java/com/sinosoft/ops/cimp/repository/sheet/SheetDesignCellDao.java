/**
 * @Project:      IIMP
 * @Title:          SheetDesignCellDao.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.repository.sheet;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDao;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignCell;

import java.util.Collection;
import java.util.UUID;

/**
 * @ClassName:  SheetDesignCellDao
 * @description: 表格设计单元格数据访问接口
 * @author:        Ni
 * @date:            2018年5月25日 下午3:10:03
 * @version        1.0.0
 * @since            JDK 1.7
 */

public interface SheetDesignCellDao extends BaseEntityDao<SheetDesignCell> {
	
	/**
	 * 表格设计单元格数据存入
	 * @param sheetDesignCell
	 * @return	String
	 * @author lixianfu
	 * @date:    2018年6月5日 上午10:11:45
	 * @since JDK 1.7
	 */
	UUID saveDesignCell(SheetDesignCell sheetDesignCell);
	/**
	 * 单个表格设计单元格数据查询
	 * @param Id
	 * @return sheetDesignCell
	 * @author lixianfu
	 * @date:    2018年6月5日 下午1:50:48
	 * @since JDK 1.7
	 */
	SheetDesignCell getById(UUID Id);
	/**
	 * 表格设计单元格数据修改
	 * @param sheetDesignCell
	 * @return	UUID
	 * @author lixianfu
	 * @date:    2018年6月5日 上午10:11:45
	 * @since JDK 1.7
	 */
	void updateDesignCell(SheetDesignCell sheetDesignCell);
    /**
     * 按设计标识获取
     * @param designId 设计标识
     * @return 表格设计单元格集
     * @author Ni
     * @since JDK 1.7
     */
    Collection<SheetDesignCell> getByDesignId(UUID designId);
    
    /**
     * 按id删除
     * @param Id 设计标识
     * @return 影响的记录数
     * @author Ni
     * @since JDK 1.7
     */
    int deleteById(UUID Id);
    
    /**
     * 
     * 根据模板id和位置获取单元格
     * @param designId
     * @param rowNo
     * @param columnNo
     * @return
     */
	Collection<SheetDesignCell> getByPosition(UUID designId, Integer rowNo, Integer columnNo);
    
}
