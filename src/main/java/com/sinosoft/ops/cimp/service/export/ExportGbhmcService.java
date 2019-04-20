package com.sinosoft.ops.cimp.service.export;

import com.sinosoft.ops.cimp.vo.from.export.ExportGbhmcModel;

public interface ExportGbhmcService {

    /**
     * 生成干部花名册
     *
     * @param model
     * @return
     * @throws Exception
     */
    String generateGbhmc(ExportGbhmcModel model) throws Exception;
}
