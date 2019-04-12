package com.sinosoft.ops.cimp.service.sys.app;

import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.vo.from.sys.app.sysApp.SysAppAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.app.sysApp.SysAppModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.app.sysApp.SysAppSearchModel;
import com.sinosoft.ops.cimp.vo.to.sys.app.sysApp.SysAppModel;

import java.util.List;

public interface SysAppService {

    /**
     * 获取系统应用列表
     */
    PaginationViewModel<SysAppModel> listSysApp(SysAppSearchModel searchModel);

    /**
     * 添加系统应用
     */
    void addSysApp(SysAppAddModel addModel);

    /**
     * 删除系统应用
     */
    void deleteSysApp(List<String> ids);

    /**
     * 修改系统应用
     */
    boolean modifySysApp(SysAppModifyModel modifyModel);
}
