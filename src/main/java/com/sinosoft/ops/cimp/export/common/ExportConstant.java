package com.sinosoft.ops.cimp.export.common;

import com.sinosoft.ops.cimp.service.word.ExportService;
import com.sinosoft.ops.cimp.service.sys.syscode.SysCodeItemService;
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

    public static ExportService exportService;

    public static SysCodeItemService sysCodeItemService;

    /**
     * pdf2html.exe的路径
     */
    public static String PDF2HTML_PATH;
    /**
     * 导出根目录
     */
    public static String EXPORT_BASE_PATH;

    /**
     * 干部任免表导出的xml元素
     */
    public static String exportXMLElements;

    /**
     * 导出文件模板
     */
    public static final String TEMPLATE_DWLDZS = "template/excel/lingdaoshezhi_Templet.xlsx";
    public static final String TEMPLATE_GBHMC = "template/excel/hmc-1.1.xlsx";
    public static final String TEMPLATE_WORD_GBRMB_BJ = "template/word/gbrmb_template_bj.docx";

    /**
     * 导出文件目录
     */
    public static final String EXPORT_GBRMB_WORD = "download/gbrmb/word/";
    public static final String EXPORT_GBRMB_HTML = "download/gbrmb/html/";
    public static final String EXPORT_GBRMB_WORD_ZIP = "download/gbrmb/word/zip/";
    public static final String EXPORT_LRMX = "lrmx/";
    public static final String EXPORT_LRMX_ZIP = "lrmx/zip/";
    public static final String EXPORT_GBHMC_EXCEL = "download/gbhmc/excel/";
    public static final String EXPORT_DWLDZS_EXCEL = "download/dwldzs/excel/";
    /**
     * 导出文件后缀名
     */
    public static final String RMB_SUFFIX_WORD = "任免表.docx";
    public static final String RMB_SUFFIX_HTML = "RMB.html";
    public static final String RMB_SUFFIX_PDF = "RMB.pdf";
    public static final String SUFFIX_LRMX = ".lrmx";
    public static final String SUFFIX_XLSX = ".xlsx";

    @Autowired
    public void setExportService(ExportService exportService) {
        ExportConstant.exportService = exportService;
    }

    @Autowired
    public void setSysCodeItemService(SysCodeItemService sysCodeItemService) {
        ExportConstant.sysCodeItemService = sysCodeItemService;
    }

    @Value("${export.path.pdf2html}")
    public void setPdf2htmlPath(String pdf2htmlPath) {
        PDF2HTML_PATH = pdf2htmlPath;
    }

    @Value("${export.path.base}")
    public void setExportBasePath(String exportBasePath) {
        EXPORT_BASE_PATH = exportBasePath;
    }

    @Value("${exportXMLElements}")
    public void setExportXMLElements(String exportXMLElements) {
        ExportConstant.exportXMLElements = exportXMLElements;
    }
}
