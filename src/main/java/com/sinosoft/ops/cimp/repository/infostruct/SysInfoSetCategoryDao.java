package com.sinosoft.ops.cimp.repository.infostruct;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDao;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoSetCategory;

import java.util.Collection;




/**
 * @ClassName:  SysInfoSetCategoryDao
 * @Description: 信息集的分类
 * @Author:        wft
 * @Date:            2017年11月11日 下午4:07:04
 * @Version        1.0.0
 */
public interface SysInfoSetCategoryDao extends BaseEntityDao<SysInfoSetCategory> {
    /** 
     * 根据信息集标识获取信息项集合
     * @param setId 信息集标识
     * @return 信息项集合
     */
    Collection<SysInfoSetCategory> getAll();
}
