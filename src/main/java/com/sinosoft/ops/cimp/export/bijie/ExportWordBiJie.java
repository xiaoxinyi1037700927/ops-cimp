package com.sinosoft.ops.cimp.export.bijie;

import com.aspose.words.Document;

public class ExportWordBiJie extends AbstractExportHandlerBiJie {

    @Override
    protected void saveFile(Document doc, String path) throws Exception {
        doc.save(path);
    }
}
