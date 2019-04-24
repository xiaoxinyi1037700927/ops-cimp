package com.sinosoft.ops.cimp.service.sys.sysapp;

import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysAppTableSet.SysAppTableSearchModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysAppTableSet.SysAppTableSetAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysAppTableSet.SysAppTableSetModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysAppTableSet.SysAppTableSetSearchModel;
import com.sinosoft.ops.cimp.vo.to.sys.sysapp.sysAppTableSet.SysAppTableModel;
import com.sinosoft.ops.cimp.vo.to.sys.sysapp.sysAppTableSet.SysAppTableSetModel;
import com.sinosoft.ops.cimp.vo.to.sys.sysapp.sysAppTableSet.SysAppTableTypeModel;

import java.util.List;

public interface SysAppTableSetService {
    /**
     * 获取系统应用表集合列表
     */
    PaginationViewModel<SysAppTableSetModel> listTableSet(SysAppTableSetSearchModel searchModel);

    /**
     * 添加系统应用表集合
     */
    void addTableSet(SysAppTableSetAddModel addModel);

    /**
     * 删除系统应用表集合
     */
    void deleteTableSet(List<String> ids);

    /**
     * 修改系统应用表集合
     */
    boolean modifyTableSet(SysAppTableSetModifyModel modifyModel);

    /**
     * 删除系统应用表分组下的所有表
     */
    void deleteByTableGroupIds(List<String> tableGroupIds);

    /**
     * 系统表分类列表
     */
    List<SysAppTableTypeModel> listSysTableType();

    /**
     * 系统表列表
     */
    List<SysAppTableModel> listSysTable(SysAppTableSearchModel searchModel);

    /**
     * 交换排序
     */
    boolean swapSort(List<String> ids);
}
