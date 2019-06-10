/**
 * @Project:      IIMP
 * @Title:          SheetDesignCarrierDao.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.repository.sheet;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDao;
import com.sinosoft.ops.cimp.entity.sheet.SheetData;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;



/**
 * @ClassName:  SheetDesignCarrierDao
 * @Description: 表格设计载体数据访问接口
 * @Author:        kanglin
 * @Date:            2017年8月18日 下午1:05:34
 * @Version        1.0.0
 */
public interface SheetDataDao extends BaseEntityDao<SheetData> {

    /**   
     * 根据设计标识获取
     * @param designId 表格设计标识
     * @return 表格设计载体
     */
    Collection<SheetData> getBySheetId(UUID sheetId);
    Collection<SheetData> getBySheetId(String sheetId);
	void deleteBySheetId(UUID sheetId);
	
	/**
	 * 关联查询获取构建word需要的数据
	 * @param sheetId
	 * @return
	 */
	Collection<Map<String, Object>> getBuildWordDatas(UUID sheetId);
}
