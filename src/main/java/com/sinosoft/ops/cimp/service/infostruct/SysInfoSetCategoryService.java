package com.sinosoft.ops.cimp.service.infostruct;

import com.sinosoft.ops.cimp.common.service.BaseEntityService;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoSetCategory;

import java.util.Collection;



public interface SysInfoSetCategoryService extends BaseEntityService<SysInfoSetCategory> {

    /** 
     * 根据信息集标识获取信息项集合
     * @param
     * @return 信息项集合
     */
    Collection<SysInfoSetCategory> getAll();
}
