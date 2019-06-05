package com.sinosoft.ops.cimp.service.sheet;


import com.sinosoft.ops.cimp.common.service.BaseEntityService;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignSql;

/**
 * 
 * @ClassName:  SheetDesignSqlService
 * @description: 表格设计SQL 服务接口
 * @author:        sunch
 * @date:            2018年6月6日 下午1:49:06
 * @version        1.0.0
 * @since            JDK 1.7
 */
public interface SheetDesignSqlService extends BaseEntityService<SheetDesignSql> {

	Integer getMaxOrdinal();

}
