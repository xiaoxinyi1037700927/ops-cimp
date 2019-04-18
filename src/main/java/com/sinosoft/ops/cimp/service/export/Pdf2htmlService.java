package com.sinosoft.ops.cimp.service.export;


import com.sinosoft.ops.cimp.service.common.BaseService;

/**
 * pdf 转为 html
 *
 * @author shixianggui
 * @Date: 20180306
 */
public interface Pdf2htmlService extends BaseService {

    /**
     * Windows 环境下 pdf 转为 HTML
     *
     * @param pdfFilePath  : pdf 路径
     * @param pdfFileName  : pdf 文件名
     * @param htmlFilePath : HTML 路径
     * @param htmlFileName : HTML 文件名
     * @return
     */
    boolean pdf2Html(String pdfFilePath, String pdfFileName,
                     String htmlFilePath, String htmlFileName) throws Exception;

    /**
     * 解析 html, 为html增加一些事件
     *
     * @param htmlFilePathName : html 文件路径名称
     * @return
     */
    String analysisHtml(String htmlFilePathName);


}
