package com.sinosoft.ops.cimp.export.bijie;

import com.aspose.words.Document;
import com.aspose.words.PdfSaveOptions;

public class ExportPdfBiJie extends AbstractExportHandlerBiJie {
    /**
     * 保存文件
     *
     * @param doc
     * @param path
     * @throws Exception
     */
    @Override
    protected void saveFile(Document doc, String path) throws Exception {
        doc.save(path, new PdfSaveOptions());
    }
}