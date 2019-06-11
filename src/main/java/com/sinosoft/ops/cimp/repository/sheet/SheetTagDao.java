package com.sinosoft.ops.cimp.repository.sheet;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDao;
import com.sinosoft.ops.cimp.entity.sheet.SheetTag;

import java.util.Collection;
import java.util.UUID;



/**
 * 
 * @ClassName:  SheetTagDao
 * @description: 报表批注数据访问接口
 */
public interface SheetTagDao extends BaseEntityDao<SheetTag> {

	Integer deleteBySheetId(UUID sheetId);

	Collection<SheetTag> getBySheetId(UUID sheetId);

}
