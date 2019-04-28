package com.sinosoft.ops.cimp.service.sys.sysapp.acess;

import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.access.SysAppTableAccessAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.access.SysAppTableAccessModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.access.SysAppTableAccessSearchModel;
import com.sinosoft.ops.cimp.vo.to.sys.sysapp.access.SysAppTableAccessModel;
import com.sinosoft.ops.cimp.vo.to.sys.sysapp.sysApp.SysAppTreeModel;

import java.util.List;
import java.util.Map;

public interface SysAppTableAccessService {

    /**
     * 获取对表的访问权限列表
     */
    PaginationViewModel<SysAppTableAccessModel> listTableAccess(SysAppTableAccessSearchModel searchModel);

    /**
     * 添加对表的访问权限
     */
    void addTableAccess(SysAppTableAccessAddModel addModel);

    /**
     * 删除对表的访问权限
     */
    void deleteTableAccess(List<String> ids);

    /**
     * 修改对表的访问权限
     */
    boolean modifyTableAccess(SysAppTableAccessModifyModel modifyModel);

    /**
     * 获取当前用户对app的表访问权限
     */
    Map<String, SysAppTableAccessModel> getTableAccess(String appCode);

    /**
     * 获取系统表分组树
     */
    List<SysAppTreeModel> getTree();
}
