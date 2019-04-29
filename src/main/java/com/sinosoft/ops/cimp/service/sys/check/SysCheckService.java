package com.sinosoft.ops.cimp.service.sys.check;

import com.sinosoft.ops.cimp.vo.to.sys.check.SysCheckConditionModel;

import java.util.List;

public interface SysCheckService {

    /**
     * 系统查错条件列表
     */
    List<SysCheckConditionModel> listSysCheckCondition();
}
