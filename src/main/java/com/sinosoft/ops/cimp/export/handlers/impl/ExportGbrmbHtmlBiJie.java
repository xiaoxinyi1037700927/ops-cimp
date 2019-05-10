package com.sinosoft.ops.cimp.export.handlers.impl;

import com.aspose.words.Document;
import com.aspose.words.PdfSaveOptions;
import com.sinosoft.ops.cimp.export.common.ExportConstant;
import com.sinosoft.ops.cimp.export.common.Pdf2htmlUtil;
import com.sinosoft.ops.cimp.export.handlers.AbstractExportGbrmbBiJie;
import com.sinosoft.ops.cimp.util.FileUtils;

/**
 * 生成毕节市干部任免表html文件
 */
public class ExportGbrmbHtmlBiJie extends AbstractExportGbrmbBiJie {
    private String fileDir = null;
    private String htmlFileName = null;
    private String pdfFileName = null;


    public ExportGbrmbHtmlBiJie(String empId) {
        super(empId);
        fileDir = ExportConstant.EXPORT_BASE_PATH + ExportConstant.EXPORT_GBRMB_HTML;
        htmlFileName = empId + ExportConstant.RMB_SUFFIX_HTML;
        pdfFileName = empId + ExportConstant.RMB_SUFFIX_PDF;
    }

    @Override
    protected void saveFile(Document doc) throws Exception {
        doc.save(fileDir + pdfFileName, new PdfSaveOptions());
    }

    /**
     * 获取生成的文件名
     */
    @Override
    public String getFilePath() {
        return fileDir + htmlFileName;
    }

    /**
     * 将pdf转为html
     */
    @Override
    protected void processFile() throws Exception {
        try {
            //pdf2html
            if (!Pdf2htmlUtil.pdf2Html(fileDir, pdfFileName, fileDir, htmlFileName)) {
                throw new Exception("pdf2html failed");
            }
        } finally {
            //删除生成的pdf临时文件
            FileUtils.deleteFile(fileDir + pdfFileName);
        }
    }
}
