package com.sinosoft.ops.cimp.service.sys.sysapp.acess;

import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.access.SysAppFieldAccessAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.access.SysAppFieldAccessModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.access.SysAppFieldAccessSearchModel;
import com.sinosoft.ops.cimp.vo.to.sys.sysapp.access.SysAppFieldAccessModel;

import java.util.List;

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
     * 删除对表字段的访问权限
     */
    void deleteFieldAccess(List<String> ids);

    /**
     * 修改对表字段的访问权限
     */
    boolean modifyFieldAccess(SysAppFieldAccessModifyModel modifyModel);
}
