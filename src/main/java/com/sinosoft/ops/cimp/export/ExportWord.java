package com.sinosoft.ops.cimp.export;

import java.util.Map;

public interface ExportWord {

    /**
     * 根据empId获取所有属性值
     *
     * @param empId
     * @return
     * @throws Exception
     */
    Map<String, Object> getAllAttrValue(String empId) throws Exception;

    /**
     * 属性样式处理
     *
     * @param templateFilePath
     * @param attrValues
     * @param outputFilePath
     * @return
     * @throws Exception
     */
    String processAttrValue(String templateFilePath, Map<String, Object> attrValues, String outputFilePath) throws Exception;
}
