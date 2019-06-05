package com.sinosoft.ops.cimp.repository.sheet;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDao;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignSql;

/**
 * 
 * @ClassName:  SheetDesignSqlDao
 * @description: 表格设计SQL 数据调用接口
 * @author:        sunch
 * @date:            2018年6月6日 下午1:41:58
 * @version        1.0.0
 * @since            JDK 1.7
 */
public interface SheetDesignSqlDao extends BaseEntityDao<SheetDesignSql>{

	Integer getMaxOrdinal();

}
