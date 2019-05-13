package com.sinosoft.ops.cimp.service.sys.tag;

import com.sinosoft.ops.cimp.entity.sys.tag.SysTagCategory;
import com.sinosoft.ops.cimp.exception.BusinessException;

import java.util.List;

public interface SysTagCategoryService {

    /**
     * 根据模型名称查询tagCategory
     */
    List<SysTagCategory> findAllByModelName(String modelName);

    /**
     * 保存标签类别
     */
    SysTagCategory save(SysTagCategory sysTagCategory);

    /**
     * 更新系统标签类别
     */
    SysTagCategory update(SysTagCategory sysTagCategory);

    /**
     * 根据categoryId删除类别（如果类型别有标签则抛出异常）
     */
    void delete(String sysTagCategoryId) throws BusinessException;
}
