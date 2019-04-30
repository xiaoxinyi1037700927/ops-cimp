package com.sinosoft.ops.cimp.service.sys.tag;

import com.sinosoft.ops.cimp.entity.sys.tag.SysTag;

import java.util.List;

public interface SysTagService {
    /**
     * 查询所有tag
     */
    List<SysTag> findAll();

    /**
     * 根据tagCategoryIds查询所有符合条件的Tag
     */
    List<SysTag> findAll(List<String> tagCategoryIds);
}
