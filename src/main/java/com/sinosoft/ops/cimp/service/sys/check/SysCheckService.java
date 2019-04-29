package com.sinosoft.ops.cimp.service.sys.check;

import com.sinosoft.ops.cimp.vo.from.sys.check.SysCheckQueryDataModel;
import com.sinosoft.ops.cimp.vo.to.sys.check.SysCheckConditionModel;
import com.sinosoft.ops.cimp.vo.to.sys.check.SysCheckStatisticsData;

import java.util.List;

public interface SysCheckService {

    /**
     * 系统查错条件列表
     */
    List<SysCheckConditionModel> listSysCheckCondition();

    /**
     * 获取指定查错条件的分组统计数据
     */
    List<SysCheckStatisticsData> queryData(SysCheckQueryDataModel queryModel);
}
