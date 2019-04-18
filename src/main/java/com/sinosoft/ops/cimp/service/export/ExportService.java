package com.sinosoft.ops.cimp.service.export;


import com.sinosoft.ops.cimp.export.AbstractExportHandler;
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

    /**
     * 生成干部任免表word文件(毕节)
     *
     * @param empId
     * @return
     */
    String generateGbrmbWordBiJie(String empId);

    /**
     * 生成干部任免表html文件(毕节)
     *
     * @param empId
     * @return
     */
    String generateGbrmbHTMLBiJie(String empId);

    /**
     * 生成干部任免表lrmx文件
     *
     * @param empId
     * @return
     */
    String generateGbrmbLRMX(String empId);

}
