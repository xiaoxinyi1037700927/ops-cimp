package com.sinosoft.ops.cimp.repository.sheet;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDao;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignTag;

import java.util.UUID;

/**
 * @ClassName:  SheetDesignTagDao
 * @description: 表格设计标签数据访问接口
 * @author:        lixianfu
 * @date:            2018年6月6日 下午2:52:04
 * @version        1.0.0
 * @since            JDK 1.7
 */
public interface SheetDesignTagDao extends BaseEntityDao<SheetDesignTag>{
		/**
		 * 表格设计标签数据存入
		 * @param sheetDesignTag
		 * @return	UUID
		 * @author lixianfu
		 * @date:    2018年6月6日 下午2:56:00
		 * @since JDK 1.7
		 */
		UUID saveDesignTag(SheetDesignTag sheetDesignTag);
		
		/**
		 * 按id查表格设计标签数据
		 * @param id
		 * @return	lixianfu
		 * @author SheetDesignTag 
		 * @date:    2018年6月6日 下午2:58:48
		 * @since JDK 1.7
		 */
		SheetDesignTag getById(UUID id);
		/**
		 * 按对象修改表格设计标签数据
		 * @param sheetDesignTag
		 * @author lixianfu
		 * @date:    2018年6月6日 下午3:00:16
		 * @since JDK 1.7
		 */
		void updateDesignTag(SheetDesignTag sheetDesignTag);
		/**
		 * 按id删除表格设计标签数据
		 * @param id
		 * @author lixianfu
		 * @date:    2018年6月6日 下午3:02:01
		 * @since JDK 1.7
		 */
		void deleteDesignTag(UUID id);
}
