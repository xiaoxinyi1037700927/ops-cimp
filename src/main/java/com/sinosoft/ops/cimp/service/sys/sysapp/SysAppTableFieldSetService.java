package com.sinosoft.ops.cimp.service.sys.sysapp;

import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysAppTableFieldSet.*;
import com.sinosoft.ops.cimp.vo.to.sys.sysapp.sysAppTableFieldSet.SysAppTableFieldModel;
import com.sinosoft.ops.cimp.vo.to.sys.sysapp.sysAppTableFieldSet.SysAppTableFieldSetModel;

import java.util.List;

public interface SysAppTableFieldSetService {
    /**
     * 获取系统应用字段集合列表
     */
    PaginationViewModel<SysAppTableFieldSetModel> listFieldSet(SysAppTableFieldSetSearchModel searchModel);

    /**
     * 添加系统应用字段集合
     */
    void addFieldSet(SysAppTableFieldSetAddModel addModel);

    /**
     * 删除系统应用字段集合
     */
    void deleteFieldSet(List<String> ids);

    /**
     * 修改系统应用字段集合
     */
    boolean modifyFieldSet(SysAppTableFieldSetModifyModel modifyModel);

    /**
     * 删除系统应用字段分组下的所有字段
     */
    void deleteByFieldGroupIds(List<String> fieldGroupIds);

    /**
     * 系统字段列表
     */
    List<SysAppTableFieldModel> listSysTableField(SysAppTableFieldSearchModel searchModel);

    /**
     * 修改排序
     *
     * @param sortModel
     * @return
     */
    boolean modifySort(SysAppTableFieldSetSortModel sortModel);
}
