/**
 * @Project:      IIMP
 * @Title:          SheetDesignCarrierDao.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.repository.sheet;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDao;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignCarrier;

import java.util.Collection;
import java.util.UUID;

/**
 * @ClassName:  SheetDesignCarrierDao
 * @Description: 表格设计载体数据访问接口
 * @Author:        Nil
 * @Date:            2017年8月18日 下午1:05:34
 * @Version        1.0.0
 */
public interface SheetDesignCarrierDao extends BaseEntityDao<SheetDesignCarrier>{

    /**   
     * 根据设计标识获取
     * @param designId 表格设计标识
     * @return 表格设计载体
     */
    SheetDesignCarrier getByDesignId(UUID designId);
    
    /**
     *  根据designId删除表的载体内容
     * @param designId 表格设计标识
     */
    int deleteByDesignId(UUID designId);
    
    Collection<UUID> getAllIds();
}
