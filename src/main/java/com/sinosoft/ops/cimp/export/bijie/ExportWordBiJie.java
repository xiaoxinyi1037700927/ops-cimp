package com.sinosoft.ops.cimp.export.bijie;

import com.aspose.words.Document;

public class ExportWordBiJie extends AbstractExportWordBiJie {

    @Override
    protected String saveFile(Document doc, String path) throws Exception {
        doc.save(path);
        return path;
    }
}
