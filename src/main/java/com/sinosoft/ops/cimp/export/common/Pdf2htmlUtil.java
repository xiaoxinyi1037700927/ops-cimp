package com.sinosoft.ops.cimp.export.common;

import java.io.File;

public class Pdf2htmlUtil {

    /**
     * pdf2html
     */
    public static boolean pdf2Html(String pdfFilePath, String pdfFileName,
                                   String htmlFilePath, String htmlFileName) throws Exception {
        String command = ExportConstant.PDF2HTML_PATH +
                // 尽量减少用于文本的HTML元素的数目 (default: 0)
                " --optimize-text 1 " +
                " --zoom 1.4 " +
                // html中显示链接：0 - false, 1- true
                " --process-outline 0 " +
                // 嵌入html中的字体后缀(ttf,otf,woff,svg) (default: "woff")
                " --font-format woff " +
                pdfFilePath + pdfFileName + " " +
                htmlFileName;

        Runtime rt = Runtime.getRuntime();
        Process p = rt.exec(command, null, new File(pdfFilePath));

        StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");
        // 开启屏幕标准错误流
        errorGobbler.start();
        StreamGobbler outGobbler = new StreamGobbler(p.getInputStream(), "STDOUT");
        // 开启屏幕标准输出流
        outGobbler.start();

        int w = p.waitFor();
        int v = p.exitValue();
        return w == 0 && v == 0;
    }
}
