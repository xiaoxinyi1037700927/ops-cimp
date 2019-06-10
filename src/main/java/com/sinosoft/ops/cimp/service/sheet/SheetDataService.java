/**
 * @Project:      IIMP
 * @Title:          SheetDesignCarrierService.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.service.sheet;


import com.sinosoft.ops.cimp.common.service.BaseEntityService;
import com.sinosoft.ops.cimp.entity.sheet.SheetData;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName:  SheetDataService
 * @Description: 表格设计载体服务接口
 * @Author:        kanglin
 * @Date:            2017年8月18日 下午1:26:36
 * @Version        1.0.0
 */
public interface SheetDataService extends BaseEntityService<SheetData> {
	Collection<SheetData> getBySheetId(UUID id);
	Collection<SheetData> getBySheetId(String id);
	void deleteBySheetId(UUID sheetId);
	
	/**
	 * 
	 * 关联查询获取构建word需要的数据
	 * @param sheetId
	 * @return
	 */
	Collection<Map<String, Object>> getBuildWordDatas(UUID sheetId);

}