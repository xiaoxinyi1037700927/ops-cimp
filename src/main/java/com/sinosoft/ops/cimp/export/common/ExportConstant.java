package com.sinosoft.ops.cimp.export.common;

import com.sinosoft.ops.cimp.service.export.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by SML
 * date : 2017/11/22
 * des : 常量类
 */
@Component
public class ExportConstant {

    public static final String RESUME_SPECIAL_AREA_PROVINCE = "省";
    public static final String RESUME_SPECIAL_AREA_CITY = "市";
    public static final String RESUME_SPECIAL_AREA_REGION = "区";
    public static final String RESUME_SPECIAL_AREA_COUNTY = "县";

    public static ExportService exportWordService;

    public static String PDF2HTML_PATH;

    public static String EXPORT_BASE_PATH;

    public static final String TEMPLATE_WORD_GBRMB_BJ = "template/word/gbrmb_template_bj.docx";

    public static final String EXPORT_WORD_GBRMB = "download/word/gbrmb/";

    public static final String EXPORT_WORD_GBRMB_ZIP = "download/word/gbrmb/zip/";

    public static final String EXPORT_LRMX = "lrmx/";


    @Autowired
    public void setExportWordService(ExportService exportWordService) {
        ExportConstant.exportWordService = exportWordService;
    }

    @Value("${export.path.pdf2html}")
    public void setPdf2htmlPath(String pdf2htmlPath) {
        PDF2HTML_PATH = pdf2htmlPath;
    }

    @Value("${export.path.base}")
    public void setExportBasePath(String exportBasePath) {
        EXPORT_BASE_PATH = exportBasePath;
    }
}
