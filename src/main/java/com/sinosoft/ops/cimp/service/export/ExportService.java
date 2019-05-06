package com.sinosoft.ops.cimp.service.export;


import java.util.List;
import java.util.Map;

public interface ExportService {

    List<Map<String, Object>> findBySQL(String sql);


    List<Map<String, Object>> findBySQL(String sql, Object... args);

    /**
     * 解析 html, 为html增加一些事件
     *
     * @param htmlFilePathName : html 文件路径名称
     * @return
     */
    String analysisHtml(String htmlFilePathName);
}
