package com.sinosoft.ops.cimp.service.sys.sysapp.acess;

import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.access.SysAppFieldAccessAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.access.SysAppFieldAccessModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.access.SysAppFieldAccessSearchModel;
import com.sinosoft.ops.cimp.vo.to.sys.sysapp.access.SysAppFieldAccessModel;

import java.util.List;
import java.util.Map;

public interface SysAppFieldAccessService {
    /**
     * 获取对表字段的访问权限列表
     */
    PaginationViewModel<SysAppFieldAccessModel> listFieldAccess(SysAppFieldAccessSearchModel searchModel);

    /**
     * 添加对表字段的访问权限
     */
    void addFieldAccess(SysAppFieldAccessAddModel addModel);


    /**
     * 修改对表字段的访问权限
     */
    boolean modifyFieldAccess(SysAppFieldAccessModifyModel modifyModel);

    /**
     * 获取用户对app下表字段的访问权限
     */
    Map<String, SysAppFieldAccessModel> getFieldAccess(String appCode, String sysTableId);

    /**
     * 系统应用添加字段同步至角色访问权限
     *
     * @param tableSetId
     * @param fieldSetId
     */
    void addField(String tableSetId, String fieldSetId);

    /**
     * 系统应用删除字段同步至角色访问权限
     *
     * @param fieldSetIds
     */
    void deleteField(List<String> fieldSetIds);

}
