package com.sinosoft.ops.cimp.controller.export.gbrmb.bijie;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.sinosoft.ops.cimp.config.swagger2.BusinessApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.export.ExportWord;
import com.sinosoft.ops.cimp.export.bijie.ExportWordBiJie;
import com.sinosoft.ops.cimp.export.bijie.ExportWordOfPdfBiJie;
import com.sinosoft.ops.cimp.export.common.ExportConstant;
import com.sinosoft.ops.cimp.export.data.NameAttrValue;
import com.sinosoft.ops.cimp.service.export.PdfTohtmlService;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@BusinessApiGroup
@Api(description = "导出干部任免表")
@Controller
@RequestMapping("/export/bj/gbrmb/word")
public class ExportGbrmbWordController extends BaseController {
    @Autowired
    private PdfTohtmlService pdfTohtmlService;


    @GetMapping("/generate")
    public void generateGbrmb(HttpServletRequest request, HttpServletResponse response) {

    }

    @GetMapping("/export")
    public void exportGbrmb(HttpServletRequest request, HttpServletResponse response) {
//        String templateFilePath = "classpath:public/template/word/gbrmb_template_bj.docx";
//
//        String outputFilePath = "";
//        String outputFileRealPath = ExportConstant.EXPORT_BASE_PATH + "download/word/gbrmb/";
//        String outputZipFilePath = ExportConstant.EXPORT_BASE_PATH + "download/word/gbrmb/zip/";
//        try {
//            String empId = "C3215AC3ECCB4E4D9F790497B6FC309A";
//            ExportWord exportWord = new ExportWordOfPdfBiJie();
//
//
//            Map<String, Object> allAttrValue = exportWord.getAllAttrValue(empId);
//            String pdfPath = exportWord.processAttrValue(templateFilePath, allAttrValue, outputFilePath);
//
//            int lastIndex = pdfPath.lastIndexOf("\\");
//            if (lastIndex > 0) {
//                String pdfFilePath = pdfPath.substring(0, lastIndex);
//                if (pdfFilePath.endsWith("\\")) {
//                    pdfFilePath = pdfFilePath.substring(0, pdfFilePath.length() - 1);
//                } else if (pdfFilePath.endsWith("/")) {
//                    pdfFilePath = pdfFilePath.substring(0, pdfFilePath.length() - 1);
//                }
//                String pdfFileName = pdfPath.substring(lastIndex + 1, pdfPath.length()); // "c81848ee-1bcd-40d2-a196-ce1b52875880RMB.pdf"
//                String htmlFilePath = pdfFilePath;
//                String htmlFileName = pdfFileName.substring(0, pdfFileName.lastIndexOf(".")) + ".html";
//
//                boolean isHtmlSuccess = pdfTohtmlService.pdfToHtml(ExportConstant.PDF2HTML_PATH, pdfFilePath, pdfFileName, htmlFilePath, htmlFileName);
//                if (isHtmlSuccess) {
//                    String html = pdfTohtmlService.analysisHtml(htmlFilePath + "\\" + htmlFileName);
//                } else {
//                    throw new Exception("pdf 转成 HTML 失败!");
//                }
//
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @GetMapping("/generateAndExport")
    public void generateAndExportGbrmb(HttpServletRequest request, HttpServletResponse response, String empIds) {
//        if (StringUtils.isEmpty(empIds)) {
//            writeJson(response, "empIds不能为空！");
//        }
//        String empId = "C3215AC3ECCB4E4D9F790497B6FC309A";
//
//        try {
//            String[] empIdArr = StringUtils.split(empIds, ",");
//
//            String templateFilePath = "classpath:public/template/word/gbrmb_template_bj.docx";
//
//            String outputFilePath = "";
//            String outputFileRealPath = ExportConstant.EXPORT_BASE_PATH + "download/word/gbrmb/";
//            String outputZipFilePath = ExportConstant.EXPORT_BASE_PATH + "download/word/gbrmb/zip/";
//
//            ExportWord exportWord = new ExportWordBiJie();
//            Map<String, Object> allAttrValue = exportWord.getAllAttrValue(empId);
//
//            outputFilePath = outputFileRealPath + "/" + NameAttrValue.getName() +
//                    NameAttrValue.getCardNo() + "任免表.docx";
//
//            exportWord.processAttrValue(templateFilePath, allAttrValue, outputFilePath);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
