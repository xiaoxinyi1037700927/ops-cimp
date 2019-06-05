/**
 * @Project:      IIMP
 * @Title:          SheetDesignCarrierService.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.service.sheet;


import com.sinosoft.ops.cimp.common.service.BaseEntityService;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignParameter;

import java.util.Collection;
import java.util.UUID;

/**
 * @ClassName:  SheetDesignParameterService
 * @Description: 表格设计参数服务接口
 * @Version        1.0.0
 */
public interface SheetDesignParameterService extends BaseEntityService<SheetDesignParameter> {

    Collection<SheetDesignParameter> getByDesignId(UUID designId);

	boolean moveDown(SheetDesignParameter entity, UUID designId);

	boolean moveUp(SheetDesignParameter entity, UUID designId);

	void deleteByDesignId(UUID designId);
}