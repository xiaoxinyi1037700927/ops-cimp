package com.sinosoft.ops.cimp.service.export;


import com.sinosoft.ops.cimp.service.common.BaseService;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: BaseQueryService
 * @Description: 用于word导出sql查询
 * @Author: zhangxp
 * @Date: 2017年11月13日 下午12:35:39
 * @Version 1.0.0
 */
public interface ExportService extends BaseService {

    List<Map<String, Object>> findBySQL(String sql);

    List<Map<String, Object>> findBySQL1(String sql, String empId);
    //shixianggui-20180131, 入参 String sort 修改为  Map<String, Object> params

}
