package com.sinosoft.ops.cimp.service.sys.homepagecount;

import com.sinosoft.ops.cimp.vo.to.sys.homepagecount.ChartsDataModel;

import java.util.List;

public interface ChartsService {

    /**
     * 获取干部统计图表数据
     */
    List<ChartsDataModel> getCadreStatisticsData();

}
