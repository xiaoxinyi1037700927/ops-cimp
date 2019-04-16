package com.sinosoft.ops.cimp.export.common;

import com.sinosoft.ops.cimp.service.export.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.servlet.ServletContext;
import java.io.FileNotFoundException;

/**
 * Created by SML
 * date : 2017/11/22
 * des : 常量类
 */
@Component
public class ExportConstant {

    public static ExportService exportWordService;

    @Autowired
    public ExportConstant(ExportService exportService, ServletContext context) {
        exportWordService = exportService;
    }

    public static final String RESUME_SPECIAL_AREA_PROVINCE = "省";
    public static final String RESUME_SPECIAL_AREA_CITY = "市";
    public static final String RESUME_SPECIAL_AREA_REGION = "区";
    public static final String RESUME_SPECIAL_AREA_COUNTY = "县";

    public static final String PDF2HTML_PATH = "D:\\\\repositories\\\\ops-cimp\\\\src\\\\main\\\\resources\\\\public\\\\pdf2html\\\\pdf2htmlEX-win32-0.14.6-upx-with-poppler-data\\\\pdf2htmlEX.exe";

    public static final String EXPORT_BASE_PATH = "C:\\ops-cimp\\";


}
