package com.sinosoft.ops.cimp.export.bijie;

import com.aspose.words.Document;
import com.aspose.words.PdfSaveOptions;

public class ExportWordOfPdfBiJie extends AbstractExportWordBiJie {
    /**
     * 保存文件
     *
     * @param doc
     * @param path
     * @return
     * @throws Exception
     */
    @Override
    protected String saveFile(Document doc, String path) throws Exception {
        String pdfPath = path.substring(0, path.lastIndexOf(".")) + ".pdf";

        doc.save(pdfPath, new PdfSaveOptions());
        return pdfPath;
    }
}
