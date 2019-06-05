package com.sinosoft.ops.cimp.service.sheet;

import com.sinosoft.ops.cimp.common.service.BaseEntityService;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignCell;

import java.util.UUID;



/**
 * @ClassName:  SheetDesignCellService
 * @description: 表格设计单元格数据访问接口
 * @author:        lixianfu
 * @date:            2018年6月5日 下午1:23:25
 * @version        1.0.0
 * @since            JDK 1.7
 */
public interface SheetDesignCellService extends BaseEntityService<SheetDesignCell> {
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
		 * 表格设计单元格数据修改
		 * @param sheetDesignCell
		 * @return	UUID
		 * @author lixianfu
		 * @date:    2018年6月5日 上午10:11:45
		 * @since JDK 1.7
		 */
		void updateDesignCell(SheetDesignCell sheetDesignCell);
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
	     * 按设计标识删除
	     * @param designId 设计标识
	     * @return 影响的记录数
	     * @author lixianfu
	     * @since JDK 1.7
	     */
	    int deleteById(UUID Id);
	    
	    /**
	     * 
	     * 根据模板id和位置获取单元格
	     * @param designId
	     * @param rowNo
	     * @param columnNo
	     * @author sunch
	     * @date:    2018年9月28日 下午2:45:04
	     * @since JDK 1.7
	     */
		SheetDesignCell getByPosition(UUID designId, Integer rowNo, Integer columnNo);
	
}
