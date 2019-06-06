package com.sinosoft.ops.cimp.service.sys.sysapp;

import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysAppTableFieldGroup.SysAppTableFieldGroupAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysAppTableFieldGroup.SysAppTableFieldGroupModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysAppTableFieldGroup.SysAppTableFieldGroupSearchModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysAppTableFieldGroup.SysAppTableFieldGroupSortModel;
import com.sinosoft.ops.cimp.vo.to.sys.sysapp.sysAppTableFieldGroup.SysAppTableFieldGroupModel;

import java.util.List;

public interface SysAppTableFieldGroupService {
    /**
     * 获取系统应用字段分组列表
     */
    PaginationViewModel<SysAppTableFieldGroupModel> listFieldGroup(SysAppTableFieldGroupSearchModel searchModel);

    /**
     * 添加系统应用字段分组
     */
    void addFieldGroup(SysAppTableFieldGroupAddModel addModel);

    /**
     * 删除系统应用字段分组
     */
    void deleteFieldGroup(List<String> ids);

    /**
     * 修改系统应用字段分组
     */
    boolean modifyFieldGroup(SysAppTableFieldGroupModifyModel modifyModel);

    /**
     * 删除系统应用表集合下的所有字段分组
     */
    void deleteByTableSetIds(List<String> tableSetIds);

    /**
     * 修改排序
     * @param sortModel
     * @return
     */
    boolean modifySort(SysAppTableFieldGroupSortModel sortModel);
}
