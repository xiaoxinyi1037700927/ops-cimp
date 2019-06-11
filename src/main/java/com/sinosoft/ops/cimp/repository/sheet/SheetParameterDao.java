/**
 * @Project:      IIMP
 * @Title:          SheetDesignCellDao.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.repository.sheet;


import com.sinosoft.ops.cimp.common.dao.BaseEntityDao;
import com.sinosoft.ops.cimp.entity.sheet.SheetParameter;

import java.util.Collection;
import java.util.UUID;

/**
 * @ClassName:  SheetDesignParameterDao
 * @description: 表格设计参数访问接口
 * @version        1.0.0
 * @since            JDK 1.7
 */
public interface SheetParameterDao extends BaseEntityDao<SheetParameter> {

	Collection<SheetParameter> getBySheetId(UUID sheetId);

	void deleteBySheetId(UUID sheetId);
}
