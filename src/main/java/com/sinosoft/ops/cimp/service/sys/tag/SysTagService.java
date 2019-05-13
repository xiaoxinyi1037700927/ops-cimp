package com.sinosoft.ops.cimp.service.sys.tag;

import com.sinosoft.ops.cimp.entity.sys.tag.SysTag;

import java.util.List;
import java.util.Optional;

public interface SysTagService {
    /**
     * 查询所有tag
     */
    List<SysTag> findAll();

    /**
     * 根据tagCategoryIds查询所有符合条件的Tag
     */
    List<SysTag> findAll(List<String> tagCategoryIds);

    /**
     * 根据sysTagId查询SysTag
     */
    Optional<SysTag> findSysTag(String sysTagId);

    /**
     * 保存一个标签
     */
    SysTag save(SysTag sysTag);

    /**
     * 修改一个标签
     */
    SysTag update(SysTag sysTag);

    /**
     * 删除一个标签并且包括干部计算的标签
     */
    void delete(String sysTagId);

}
