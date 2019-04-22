package com.sinosoft.ops.cimp.controller.export.gbhmc;

import com.sinosoft.ops.cimp.annotation.BusinessApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.service.export.ExportGbhmcService;
import com.sinosoft.ops.cimp.vo.from.export.ExportGbhmcModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

@BusinessApiGroup
@Api(description = "导出干部花名册")
@Controller
@RequestMapping("/export/gbhmc")
public class ExportGbhmcController extends BaseController {

    private final ExportGbhmcService exportGbhmcService;

    @Autowired
    public ExportGbhmcController(ExportGbhmcService exportGbhmcService) {
        this.exportGbhmcService = exportGbhmcService;
    }

    @ApiOperation(value = "导出干部花名册")
    @GetMapping("generateAndExport")
    public void generateAndExportGbhmc(ExportGbhmcModel model, HttpServletResponse response) {
        try {
            String filePath = exportGbhmcService.generateGbhmc(model);
            if (null != filePath) {
                writeFileToResponse(response, filePath);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        writeJson(response, "导出花名册失败！");

    }

    private void writeFileToResponse(HttpServletResponse response, String filePath) throws Exception {
        String fileName = "干部花名册.xlsx";
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
