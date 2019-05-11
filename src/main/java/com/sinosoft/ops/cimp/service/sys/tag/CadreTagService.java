package com.sinosoft.ops.cimp.service.sys.tag;

import com.sinosoft.ops.cimp.entity.sys.tag.SysTag;

import java.util.List;

public interface CadreTagService {
    /**
     * 对干部进行打标签
     *
     * @param sysTag 需要打的标签
     */
    int markingTag(SysTag sysTag);

    /**
     * 并行对干部进行批量打多个标签
     */
    void parallelMarkingTag(List<SysTag> sysTagList);

    /**
     * 删除干部某项标签
     */
    void deleteTag(String tagId);
}
