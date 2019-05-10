package com.sinosoft.ops.cimp.controller.export.gbrmb.bijie;

import com.sinosoft.ops.cimp.annotation.BusinessApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.export.ExportManager;
import com.sinosoft.ops.cimp.export.common.ExportConstant;
import com.sinosoft.ops.cimp.export.handlers.impl.ExportGbrmbHtmlBiJie;
import com.sinosoft.ops.cimp.export.handlers.impl.ExportGbrmbLrmx;
import com.sinosoft.ops.cimp.export.handlers.impl.ExportGbrmbWordBiJie;
import com.sinosoft.ops.cimp.service.export.ExportService;
import com.sinosoft.ops.cimp.util.FileUtils;
import com.sinosoft.ops.cimp.util.MultiZipUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;

@BusinessApiGroup
@Api(description = "导出干部任免表(毕节)")
@Controller
@RequestMapping("/export/gbrmb/bj")
public class ExportGbrmbBiJieController extends BaseController {
    private final ExportService exportService;

    public ExportGbrmbBiJieController(ExportService exportService) {
        this.exportService = exportService;
    }


    @ApiOperation(value = "生成干部任免表html文件(毕节)")
    @ApiImplicitParam(name = "empId", value = "empId", required = true)
    @PostMapping("/html/generate")
    public ResponseEntity generateAndExportGbrmbHTML(String empId, @RequestParam(defaultValue = "false") boolean forceGenerate) throws BusinessException {
        if (StringUtils.isEmpty(empId)) {
            return fail("empId不能为空！");
        }

        try {
            String outFile = ExportManager.generate(new ExportGbrmbHtmlBiJie(empId), forceGenerate);
            if (outFile == null) {
                return fail("文件正在生成中，请稍后再试");
            }

            return ok(exportService.analysisHtml(outFile));
        } catch (Exception e) {
            e.printStackTrace();
            return fail("任免表生成失败！");
        }
    }

    @ApiOperation(value = "生成并导出干部任免表word文件(毕节)")
    @ApiImplicitParam(name = "empIds", value = "empId列表(以','分隔)", required = true)
    @GetMapping("/word/generateAndExport")
    public void generateAndExportGbrmb(HttpServletResponse response, String[] empIds) {
        if (null == empIds || empIds.length == 0) {
            writeJson(response, "empIds不能为空！");
            return;
        }

        String returnFilePath = "";
        boolean toZip = false;
        try {
            //生成干部任免表
            ArrayList<String> outFiles = new ArrayList<>(empIds.length);
            for (String empId : empIds) {
                String outFile = ExportManager.generate(new ExportGbrmbWordBiJie(empId), false);
                if (outFile == null) {
                    writeJson(response, "文件正在生成中，请稍后再试！");
                    return;
                }
                outFiles.add(outFile);
            }

            toZip = outFiles.size() > 1;

            returnFilePath = toZip ? toZip(outFiles, ExportConstant.EXPORT_GBRMB_WORD_ZIP, "干部任免表_") : outFiles.get(0);
            writeFileToResponse(response, returnFilePath);
        } catch (Exception e) {
            e.printStackTrace();
            writeJson(response, "任免表导出失败！");
        } finally {
            //删除生成的zip临时包
            if (toZip) {
                FileUtils.deleteFile(returnFilePath);
            }
        }
    }

    @ApiOperation(value = "生成并导出干部任免表lrmx文件(毕节)")
    @ApiImplicitParam(name = "empIds", value = "empId列表(以','分隔)", required = true)
    @GetMapping("/lrmx/generateAndExport")
    public void generateAndExportGbrmbLRMX(HttpServletResponse response, String[] empIds) {
        if (null == empIds || empIds.length == 0) {
            writeJson(response, "empIds不能为空！");
            return;
        }

        boolean toZip = false;
        String returnFilePath = "";
        try {
            //生成干部任免表lrmx文件
            ArrayList<String> outFiles = new ArrayList<>(empIds.length);
            for (String empId : empIds) {
                String outFile = ExportManager.generate(new ExportGbrmbLrmx(empId), false);
                if (outFile == null) {
                    writeJson(response, "文件正在生成中，请稍后再试！");
                    return;
                }
                outFiles.add(outFile);
            }

            toZip = outFiles.size() > 1;

            returnFilePath = toZip ? toZip(outFiles, ExportConstant.EXPORT_LRMX_ZIP, "干部任免表_lrmx") : outFiles.get(0);
            writeFileToResponse(response, returnFilePath);
        } catch (Exception e) {
            e.printStackTrace();
            writeJson(response, "任免表导出失败！");
        } finally {
            //删除生成的zip临时包
            if (toZip) {
                FileUtils.deleteFile(returnFilePath);
            }
        }
    }

    /**
     * 将所有文件压缩为zip文件
     */
    private String toZip(ArrayList<String> files, String path, String name) throws ZipException {
        FileUtils.createDir(path);

        String zipPath = path + name + System.currentTimeMillis();
        MultiZipUtil.zip(zipPath, files);
        return zipPath + ".zip";
    }

    private void writeFileToResponse(HttpServletResponse response, String filePath) throws Exception {
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        fileName = new String(fileName.getBytes("utf-8"), "utf-8");
        fileName = URLEncoder.encode(fileName, "UTF-8");

        //设置向浏览器端传送的文件格式
        response.setContentType("application/x-download");
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

        //写入文件流
        try (FileInputStream fis = new FileInputStream(filePath);
             OutputStream os = response.getOutputStream()) {
            byte[] b = new byte[1024 * 10];
            int i = 0;
            while ((i = fis.read(b)) > 0) {
                os.write(b, 0, i);
            }
            os.flush();
        }
    }
}
