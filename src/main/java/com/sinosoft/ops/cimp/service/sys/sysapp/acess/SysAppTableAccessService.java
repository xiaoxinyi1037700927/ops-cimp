package com.sinosoft.ops.cimp.service.sys.sysapp.acess;

import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.access.SysAppTableAccessAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.access.SysAppTableAccessModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.access.SysAppTableAccessSearchModel;
import com.sinosoft.ops.cimp.vo.to.sys.sysapp.access.SysAppTableAccessModel;

import java.util.List;

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
}
