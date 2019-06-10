package com.sinosoft.ops.cimp.controller.export.dwldzs;

import com.sinosoft.ops.cimp.annotation.OrganizationApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.export.ExportManager;
import com.sinosoft.ops.cimp.export.handlers.impl.ExportDwldzs;
import com.sinosoft.ops.cimp.service.export.ExportService;
import com.sinosoft.ops.cimp.service.sys.syscode.SysCodeItemService;
import com.sinosoft.ops.cimp.util.ParticularUtils;
import com.sinosoft.ops.cimp.vo.from.sys.syscode.SysCodeItemModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.syscode.SysCodeItemPageModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


@OrganizationApiGroup
@Api(description = "导出单位领导职数表")
@Controller
@RequestMapping("/export/dwldzs")
public class ExportDwldzsController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ExportDwldzsController.class);

    private final SysCodeItemService sysCodeItemService;

    @Autowired
    private ExportService exportService;


    @Autowired
    public ExportDwldzsController(SysCodeItemService sysCodeItemService) {
        this.sysCodeItemService = sysCodeItemService;
    }

    @ApiOperation("查询领导职数条件")
    @RequestMapping(value = "/getDepCondition", method = RequestMethod.POST)
    public ResponseEntity getDepCondition() throws BusinessException {
        SysCodeItemPageModel sysCodeItemPageModel=new SysCodeItemPageModel();
        int[][] deps={{9,1025},{86,1031},{166,1027},{107,2021}};
        Map<String,Object> map=new HashMap<String, Object>();
        for (int[] dep :deps){
            sysCodeItemPageModel.setId(dep[0]);
            sysCodeItemPageModel.setNameCn("");
            sysCodeItemPageModel.setOrderBy("");
            sysCodeItemPageModel.setPageIndex(1);
            sysCodeItemPageModel.setPageSize(35);
            PaginationViewModel<SysCodeItemModifyModel> sysCodeItemModifyModel = sysCodeItemService.getSysCodeItemBySearchModel(sysCodeItemPageModel);
            map.put("b0"+dep[1],sysCodeItemModifyModel);
        }
        return ok(map);
    }


    @ApiOperation("单位领导职数表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "选择类型", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "lines", value = "数据条数，0为所有", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "treeId", value = "treeId，多个以 ,隔开", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name="danWeiJiBie",value = "单位级别，多个以 ，隔开",dataType = "String", paramType = "query"),
            @ApiImplicitParam(name="liShuDanWei",value = "隶属关系层次，多个以 ，隔开",dataType = "String", paramType = "query"),
            @ApiImplicitParam(name="danWeiBianZhiXingZhi",value = "编制性质，多个以 ，隔开",dataType = "String", paramType = "query"),
            @ApiImplicitParam(name="danWeiXingZhi",value = "单位性质类别，多个以 ，隔开",dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/generateLdSet", method = RequestMethod.GET)
    public void generateLdSet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long startTime = System.currentTimeMillis();    //获取开始时
        try {
            int type = ParticularUtils.toNumber(request.getParameter("type"), 0);
            String outFile = ExportManager.generate(new ExportDwldzs(request),false);
            long endTime = System.currentTimeMillis();
            System.out.println(endTime-startTime);
            writeFileToResponse(request,response,outFile,type);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        writeJson(response, "导出单位领导职数失败！");
    }


    private void writeFileToResponse(HttpServletRequest request,HttpServletResponse response, String outFile, Integer type) throws Exception {
        String downloadName = "领导职数设置及配备.xlsx";
        if (type == 0) {
            downloadName = "领导职数(当前单位管理的单位).xlsx";
        } else if (type == 1) {
            downloadName = "领导职数(市级党委管理的单位).xlsx";
        } else if (type == 2) {
            downloadName = "领导职数(县级党委管理的单位).xlsx";
        }
        downloadName = new String(downloadName.getBytes("utf-8"), "utf-8");
        downloadName = URLEncoder.encode(downloadName, "UTF-8");
        downloadName = downloadName.replaceAll("%28", "\\(").replaceAll("%29", "\\)");

        //设置向浏览器端传送的文件格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.addHeader("Content-Disposition", "attachment;filename=" + downloadName);
        FileInputStream fis = null;
        OutputStream os = null;

        //写入文件流
        try {
            os = response.getOutputStream();
            fis = new FileInputStream(outFile);
            byte[] b = new byte[1024 * 10];
            int j = 0;
            while ((j = fis.read(b)) > 0) {
                os.write(b, 0, j);
            }
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        File file = new File(outFile);
        if (file.exists() && file.isFile() && file.delete()) ;

    }


}
