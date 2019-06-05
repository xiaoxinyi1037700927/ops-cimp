/**
 * @Project:      IIMP
 * @Title:          SheetDesignCarrierService.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.service.sheet;


import com.sinosoft.ops.cimp.common.service.BaseEntityService;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignCarrier;

import java.util.Collection;
import java.util.UUID;

/**
 * @ClassName:  SheetDesignCarrierService
 * @Description: 表格设计载体服务接口
 * @Author:        Nil
 * @Date:            2017年8月18日 下午1:26:36
 * @Version        1.0.0
 */
public interface SheetDesignCarrierService extends BaseEntityService<SheetDesignCarrier> {
    SheetDesignCarrier getByDesignId(UUID id);
    
    /**
     *  根据designId删除表的载体内容
     * @param designId 表格设计标识
     */
    int deleteByDesignId(UUID designId);
    
    /**
     * 
     * 解析word成树，并保存
     * @param content  载体内容
     * @param designId  表格设计id
     * @throws Exception 
     */
    void analyzeWord2Tree(byte[] content, UUID designId) throws Exception;

    Integer word2html(UUID designId, String templateFilePath, String tempPath) throws Exception ;
    
    Collection<UUID> getAllIds();
}