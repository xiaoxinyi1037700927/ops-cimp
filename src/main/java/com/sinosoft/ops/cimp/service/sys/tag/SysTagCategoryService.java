package com.sinosoft.ops.cimp.service.sys.tag;

import com.sinosoft.ops.cimp.entity.sys.tag.SysTagCategory;

import java.util.List;

public interface SysTagCategoryService {

    /**
     * 根据模型名称查询tagCategory
     */
    List<SysTagCategory> findAllByModelName(String modelName);
}
