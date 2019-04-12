package com.sinosoft.ops.cimp.service.sys.app;

import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.vo.from.sys.app.sysAppTableGroup.SysAppTableGroupAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.app.sysAppTableGroup.SysAppTableGroupModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.app.sysAppTableGroup.SysAppTableGroupSearchModel;
import com.sinosoft.ops.cimp.vo.to.sys.app.sysAppTableGroup.SysAppTableGroupModel;

import java.util.List;

public interface SysAppTableGroupService {
    /**
     * 获取系统应用表分组列表
     */
    PaginationViewModel<SysAppTableGroupModel> listTableGroup(SysAppTableGroupSearchModel searchModel);

    /**
     * 添加系统应用表分组
     */
    void addTableGroup(SysAppTableGroupAddModel addModel);

    /**
     * 删除系统应用表分组
     */
    void deleteTableGroup(List<String> ids);

    /**
     * 修改系统应用表分组
     */
    boolean modifyTableGroup(SysAppTableGroupModifyModel modifyModel);

    /**
     * 删除系统应用下的所有分组
     */
    void deleteBySysAppIds(List<String> sysAppIds);
}
