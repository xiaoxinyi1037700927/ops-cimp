package com.sinosoft.ops.cimp.controller.cadre;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sinosoft.ops.cimp.annotation.BusinessApiGroup;
import com.sinosoft.ops.cimp.annotation.RequiresAuthentication;
import com.sinosoft.ops.cimp.common.BaseResult;
import com.sinosoft.ops.cimp.constant.RolePermissionPageSqlEnum;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.entity.user.UserRole;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.cadre.CadreService;
import com.sinosoft.ops.cimp.service.user.RolePermissionPageSqlService;
import com.sinosoft.ops.cimp.util.JsonUtil;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.vo.from.cadre.CadreOrgModifyModel;
import com.sinosoft.ops.cimp.vo.from.cadre.CadreSearchModel;
import com.sinosoft.ops.cimp.vo.from.cadre.CadreSortInDepModifyModel;
import com.sinosoft.ops.cimp.vo.from.cadre.CadreStatusModifyModel;
import com.sinosoft.ops.cimp.vo.from.user.rolePermissionPageSql.RPPageSqlSearchModel;
import com.sinosoft.ops.cimp.vo.to.cadre.CadreDataVO;
import com.sinosoft.ops.cimp.vo.to.cadre.CadreFieldVO;
import com.sinosoft.ops.cimp.vo.to.cadre.CadreSearchVO;
import com.sinosoft.ops.cimp.vo.to.cadre.CadreVO;
import com.sinosoft.ops.cimp.vo.to.user.rolePermissionPageSql.RPPageSqlViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

@BusinessApiGroup
@Api(description = "干部列表接口")
@RestController
@RequestMapping(value = "/cadre")
public class CadreController extends BaseController {

    private final RolePermissionPageSqlService rolePermissionPageSqlService;
    private final JdbcTemplate jdbcTemplate;
    private final CadreService cadreService;

    @Autowired
    public CadreController(RolePermissionPageSqlService rolePermissionPageSqlService, JdbcTemplate jdbcTemplate, CadreService cadreService) {
        this.rolePermissionPageSqlService = rolePermissionPageSqlService;
        this.jdbcTemplate = jdbcTemplate;
        this.cadreService = cadreService;
    }

    @ApiOperation(value = "查询干部列表")
    @PostMapping(value = "/list")
    @RequiresAuthentication
    public ResponseEntity listCadre(@RequestBody CadreSearchModel searchModel) throws BusinessException {
        return ok(cadreService.listCadre(searchModel));
    }

    @ApiOperation(value = "查询干部基本信息")
    @GetMapping(value = "/basicInfo")
    @RequiresAuthentication
    public ResponseEntity getCadreBasicInfo(@RequestParam("EMP_ID") String empId) throws BusinessException {
        return ok(cadreService.getCadreBasicInfo(empId));
    }

    @ApiOperation(value = "获取干部图片")
    @GetMapping(value = "/photo")
    public void getPhoto(@RequestParam("empId") String empId, HttpServletResponse response) {
        try {
            byte[] photo = cadreService.getPhoto(empId);

            List<Map<String, Object>> maps = jdbcTemplate.queryForList("select A01001 as \"name\" from EMP_A001 where EMP_ID = '" + empId + "'");
            Map<String, Object> map = Maps.newHashMap();
            if (maps.size() > 0) {
                map = maps.get(0);
            }
            String name = map.get("name").toString();
            String fileName = name + ".png";
            fileName = new String(fileName.getBytes("utf-8"), "utf-8");
            fileName = URLEncoder.encode(fileName, "UTF-8");

            //设置向浏览器端传送的文件格式
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

            //写入流
            if (photo != null) {
                try (InputStream in = new ByteArrayInputStream(photo);
                     OutputStream os = response.getOutputStream()) {
                    byte[] b = new byte[1024 * 10];
                    int i = 0;
                    while ((i = in.read(b)) > 0) {
                        os.write(b, 0, i);
                    }
                    os.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "获取干部图片")
    @GetMapping(value = "/photo/download")
    public void downloadPhoto(@RequestParam("empId") String empId, HttpServletResponse response) {
        try {
            byte[] photo = cadreService.getPhoto(empId);

            Map<String, Object> map = jdbcTemplate.queryForMap("select A01001 as \"name\" from EMP_A001 where EMP_ID = '" + empId + "'");
            String name = map.get("name").toString();

            String fileName = name + ".png";
            fileName = new String(fileName.getBytes("utf-8"), "utf-8");
            fileName = URLEncoder.encode(fileName, "UTF-8");

            //设置向浏览器端传送的文件格式
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

            //写入流
            if (photo != null) {
                try (InputStream in = new ByteArrayInputStream(photo);
                     OutputStream os = response.getOutputStream()) {
                    byte[] b = new byte[1024 * 10];
                    int i = 0;
                    while ((i = in.read(b)) > 0) {
                        os.write(b, 0, i);
                    }
                    os.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "上传干部图片")
    @PostMapping(value = "/uploadPhoto")
    public ResponseEntity uploadPhoto(@RequestParam("photo") MultipartFile photo, @RequestParam("empId") String empId) throws BusinessException {
        if (photo.isEmpty()) {
            return fail("上传图片不能为空！");
        }

        return cadreService.uploadPhoto(empId, photo) ? ok("上传成功!") : fail("上传失败！");
    }


    @ApiOperation(value = "获取干部单位内排序")
    @PostMapping("/sortInDep")
    @RequiresAuthentication
    public ResponseEntity getSortInDep(@RequestParam String orgId) throws BusinessException {
        return ok(cadreService.getSortInDep(orgId));
    }

    @ApiOperation(value = "修改干部单位内排序")
    @PostMapping("/sortInDep/modify")
    @RequiresAuthentication
    public ResponseEntity modifySortInDep(@RequestBody CadreSortInDepModifyModel modifyModel) throws BusinessException {
        boolean b = cadreService.modifySortInDep(modifyModel);
        BaseResult baseResult = new BaseResult();
        if (b) {
            baseResult.setCode(200);
            baseResult.setData("修改成功");
        } else {
            baseResult.setCode(400);
            baseResult.setData("移动的两个干部不属于同一个单位无法移动");
            baseResult.setMessage("移动的两个干部不属于同一个单位无法移动");
        }
        return ResponseEntity.ok(baseResult);
    }

    @ApiOperation(value = "修改干部单位内排序")
    @PostMapping("/sortInDep/modify1")
    @RequiresAuthentication
    public ResponseEntity modifySortInDep(@RequestBody List<CadreSortInDepModifyModel> modifyModels) throws BusinessException {
        return cadreService.modifySortOrder(modifyModels) ? ok("修改成功!") : fail("修改失败！");
    }

    @ApiOperation(value = "修改干部状态")
    @PostMapping("/status/modify")
    @RequiresAuthentication
    public ResponseEntity modifyStatus(@RequestBody CadreStatusModifyModel modifyModel) throws BusinessException {
        return cadreService.modifyStatus(modifyModel) ? ok("修改成功!") : fail("修改失败！");
    }

    @ApiOperation(value = "修改干部所属单位")
    @PostMapping("/org/modify")
    @RequiresAuthentication
    public ResponseEntity modifyOrganization(@RequestBody CadreOrgModifyModel modifyModel) throws BusinessException {
        return cadreService.modifyOrganization(modifyModel) ? ok("修改成功!") : fail("修改失败！");
    }

    @ApiOperation(value = "查询干部身份证号是否重复")
    @PostMapping("/cardId")
    @RequiresAuthentication
    public ResponseEntity cadreCardIdExist(@RequestParam("cardId") String cardId) throws BusinessException {
        return ok(cadreService.cadreCardIdExist(cardId));
    }

    @GetMapping(value = "/downloadTableData")
    @ApiOperation(value = "下载人员某些信息项的数据")
    public ResponseEntity downloadTableData(@RequestParam("empIds") String empIds, @RequestParam("tableNameEn") String tableNameEn) throws BusinessException {
        String id = SecurityUtils.getSubject().getCurrentUser().getId();
        String[] cadreIds = empIds.split(",");

        for (int i = 0; i < cadreIds.length; i++) {

        }
        return ok("");
    }
}
