package com.sinosoft.ops.cimp.service.sys.check;

import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.vo.from.sys.check.SysCheckConditionAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.check.SysCheckConditionModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.check.SysCheckConditionSearchModel;
import com.sinosoft.ops.cimp.vo.from.sys.check.SysCheckQueryDataModel;
import com.sinosoft.ops.cimp.vo.to.sys.check.SysCheckConditionModel;
import com.sinosoft.ops.cimp.vo.to.sys.check.SysCheckStatisticsData;
import com.sinosoft.ops.cimp.vo.to.sys.check.SysCheckTypeModel;

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
     * 获取指定查错条件的分组统计数据
     */
    List<SysCheckStatisticsData> queryData(SysCheckQueryDataModel queryModel);
}
