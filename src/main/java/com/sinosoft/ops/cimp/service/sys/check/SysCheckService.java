package com.sinosoft.ops.cimp.service.sys.check;

import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.vo.from.sys.check.*;
import com.sinosoft.ops.cimp.vo.to.sys.check.*;

import java.util.List;

public interface SysCheckService {

    /**
     * 系统查错类型列表
     */
    List<SysCheckTypeModel> listSysCheckType();

    /**
     * 系统查错条件列表
     */
    PaginationViewModel<SysCheckConditionModel> listSysCheckCondition(SysCheckConditionSearchModel searchModel);

    /**
     * 添加查错条件
     */
    void addSysCheckCondition(SysCheckConditionAddModel addModel);

    /**
     * 修改查错条件
     */
    boolean modifySysCheckCondition(SysCheckConditionModifyModel modifyModel);

    /**
     * 删除查错条件
     */
    void deleteSysCheckCondition(List<String> ids);

    /**
     * 查错结果
     */
    SysCheckResultModel listSysCheckResult(SysCheckSearchModel searchModel);

    /**
     * 查错机构树
     */
    List<SysCheckTreeNode> getOrgTree(SysCheckOrgTreeSearchModel searchModel);

    /**
     * 查错干部列表
     */
    PaginationViewModel<SysCheckEmpModel> getEmpList(SysCheckEmpSearchModel searchModel);
}
