package com.sinosoft.ops.cimp.controller.cadre;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sinosoft.ops.cimp.annotation.BusinessApiGroup;
import com.sinosoft.ops.cimp.annotation.RequiresAuthentication;
import com.sinosoft.ops.cimp.constant.OpsErrorMessage;
import com.sinosoft.ops.cimp.constant.RolePermissionPageSqlEnum;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.entity.user.User;
import com.sinosoft.ops.cimp.entity.user.UserRole;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.cadre.CadreService;
import com.sinosoft.ops.cimp.service.user.RolePermissionPageSqlService;
import com.sinosoft.ops.cimp.util.JsonUtil;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.vo.from.cadre.CadreOrgModifyModel;
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
    @GetMapping(value = "/list")
    @RequiresAuthentication
    public ResponseEntity listCadre(
            @RequestParam("deptId") String deptId,
            @RequestParam("includeSubNode") String includeSubNode,
            @RequestParam("pageIndex") String pageIndex,
            @RequestParam("pageSize") String pageSize) throws BusinessException {

        if (StringUtils.isEmpty(pageIndex) || StringUtils.equals(pageIndex, "0")) {
            pageIndex = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        String startIndex = String.valueOf((Integer.parseInt(pageIndex) - 1) * Integer.parseInt(pageSize));
        String endIndex = String.valueOf(Integer.parseInt(pageIndex) * Integer.parseInt(pageSize));

        User currentUser = SecurityUtils.getSubject().getCurrentUser();
        if (currentUser == null) {
            throw new BusinessException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE, "请登陆之后访问接口");
        }
        List<UserRole> currentUserRole = SecurityUtils.getSubject().getCurrentUserRole();
        List<String> roleIds = currentUserRole.stream().map(UserRole::getRoleId).collect(Collectors.toList());

        RPPageSqlSearchModel rpPageSqlSearchModel = new RPPageSqlSearchModel();
        rpPageSqlSearchModel.setRoleIds(roleIds);

        List<RPPageSqlViewModel> pageSqlByRoleList = rolePermissionPageSqlService.findRPPageSqlListByRoleIds(rpPageSqlSearchModel);
        Optional<RPPageSqlViewModel> sqlViewModel = pageSqlByRoleList.stream().filter(s -> StringUtils.equals(s.getSqlNameEn(), RolePermissionPageSqlEnum.NAME_EN.干部集合.value)).filter(s -> StringUtils.equals(s.getIncludeSubNode(), includeSubNode)).findFirst();
        if (sqlViewModel.isPresent()) {
            RPPageSqlViewModel rpPageSqlViewModel = sqlViewModel.get();
            String execListSql = rpPageSqlViewModel.getExecListSql();
            String execCountSql = rpPageSqlViewModel.getExecCountSql();
            String selectCountFieldEn = rpPageSqlViewModel.getSelectCountFieldEn();
            String selectListFieldsEn = rpPageSqlViewModel.getSelectListFieldsEn();

            String execCadreListSql = execListSql.replaceAll("\\$\\{deptId\\}", deptId)
                    .replaceAll("\\$\\{startIndex\\}", startIndex)
                    .replaceAll("\\$\\{endIndex\\}", endIndex);

            String execCadreCountSql = execCountSql.replaceAll("\\$\\{deptId\\}", deptId);

            List<Map<String, Object>> mapList = jdbcTemplate.queryForList(execCadreListSql);
            Map<String, Object> countMap = jdbcTemplate.queryForMap(execCadreCountSql);

            CadreDataVO cadreDataVO = new CadreDataVO();
            cadreDataVO.setPageIndex(Integer.parseInt(pageIndex));
            cadreDataVO.setPageSize(Integer.parseInt(pageSize));
            Object cadreCount = countMap.get(selectCountFieldEn);
            if (cadreCount == null) {
                cadreDataVO.setDataCount(0L);
            } else {
                cadreDataVO.setDataCount(Long.parseLong(String.valueOf(cadreCount)));
            }
            List<CadreVO> cadreVOS = Lists.newArrayList();
            Map selectFields = JsonUtil.parseStringToObject(selectListFieldsEn, LinkedHashMap.class);

            for (Map<String, Object> map : mapList) {
                CadreVO cadreVO = new CadreVO();
                List<CadreFieldVO> fieldVOS = Lists.newArrayList();
                selectFields.forEach((k, v) -> {
                    CadreFieldVO fieldVO = new CadreFieldVO();
                    Object o = map.get(k);
                    fieldVO.setFieldNameEn(k);
                    fieldVO.setFieldNameCn(v);
                    fieldVO.setFieldValue(o);
                    fieldVOS.add(fieldVO);
                });
                cadreVO.setFields(fieldVOS);
                cadreVOS.add(cadreVO);
            }
            cadreDataVO.setCadres(cadreVOS);
            cadreDataVO.setTableFields(selectFields);
            return ok(cadreDataVO);
        } else {
            throw new BusinessException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE, "请检查角色配置的干部信息数据权限");
        }
    }

    @ApiOperation(value = "查询干部列表")
    @PostMapping(value = "/search")
    @RequiresAuthentication
    public ResponseEntity searchCadre(
            @RequestParam("deptId") String deptId,
            @RequestParam("pageIndex") String pageIndex,
            @RequestParam("pageSize") String pageSize,
            @RequestParam("cadreTagIds") String cadreTagIds,
            @RequestParam("tableConditions") String tableConditions,
            @RequestParam("appCode") String appCode
    ) throws BusinessException {
        List<String> cadreTagIdList = Lists.newArrayList();
        if (StringUtils.isNotEmpty(cadreTagIds)) {
            cadreTagIdList = Arrays.asList(cadreTagIds.split(","));
        }
        if (StringUtils.isEmpty(deptId)) {
            deptId = SecurityUtils.getSubject().getCurrentUser().getOrganizationId();
        }

        if (StringUtils.isEmpty(pageIndex) || StringUtils.equals(pageIndex, "0")) {
            pageIndex = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        String startIndex = String.valueOf((Integer.parseInt(pageIndex) - 1) * Integer.parseInt(pageSize));
        String endIndex = String.valueOf(Integer.parseInt(pageIndex) * Integer.parseInt(pageSize));

        HashMap<String, Object> searchMap;
        if (StringUtils.isNotEmpty(tableConditions)) {
            searchMap = JsonUtil.parseStringToObject(tableConditions, HashMap.class);
        } else {
            searchMap = Maps.newHashMap();
        }
        CadreSearchVO searchVO = new CadreSearchVO();
        searchVO.setAppCode(appCode);
        searchVO.setCadreTagIds(cadreTagIdList);
        searchVO.setDeptId(deptId);
        searchVO.setEndIndex(endIndex);
        searchVO.setStartIndex(startIndex);
        searchVO.setTableConditions(searchMap);

        Map<String, Object> map = cadreService.searchCadres(searchVO);
        Object cadreList = map.get("cadreList");
        Object cadreCount = map.get("cadreCount");

        CadreDataVO cadreDataVO = new CadreDataVO();
        cadreDataVO.setDataCount(Long.valueOf(cadreCount.toString()));
        cadreDataVO.setPageSize(Integer.parseInt(pageSize));
        cadreDataVO.setPageIndex(Integer.parseInt(pageIndex));

        List<UserRole> currentUserRole = SecurityUtils.getSubject().getCurrentUserRole();
        List<String> roleIds = currentUserRole.stream().map(UserRole::getRoleId).collect(Collectors.toList());

        RPPageSqlSearchModel rpPageSqlSearchModel = new RPPageSqlSearchModel();
        rpPageSqlSearchModel.setRoleIds(roleIds);

        List<RPPageSqlViewModel> pageSqlByRoleList = rolePermissionPageSqlService.findRPPageSqlListByRoleIds(rpPageSqlSearchModel);
        Optional<RPPageSqlViewModel> sqlViewModel = pageSqlByRoleList.stream().filter(s -> StringUtils.equals(s.getSqlNameEn(), RolePermissionPageSqlEnum.NAME_EN.干部集合.value)).filter(s -> StringUtils.equals(s.getIncludeSubNode(), "1")).findFirst();

        if (sqlViewModel.isPresent()) {
            RPPageSqlViewModel viewModel = sqlViewModel.get();
            String selectListFieldsEn = viewModel.getSelectListFieldsEn();
            Map selectFields = JsonUtil.parseStringToObject(selectListFieldsEn, LinkedHashMap.class);
            cadreDataVO.setTableFields(selectFields);
        }
        if (cadreList != null) {
            ArrayList cadreList1 = (ArrayList) cadreList;

            if (sqlViewModel.isPresent()) {
                RPPageSqlViewModel viewModel = sqlViewModel.get();
                String selectListFieldsEn = viewModel.getSelectListFieldsEn();
                Map selectFields = JsonUtil.parseStringToObject(selectListFieldsEn, LinkedHashMap.class);
                List<CadreVO> cadreVOS = Lists.newArrayList();

                for (Object map1 : cadreList1) {
                    CadreVO cadreVO = new CadreVO();
                    List<CadreFieldVO> fieldVOS = Lists.newArrayList();

                    selectFields.forEach((k, v) -> {
                        CadreFieldVO fieldVO = new CadreFieldVO();
                        Object o = ((Map) map1).get(k);
                        fieldVO.setFieldNameEn(k);
                        fieldVO.setFieldNameCn(v);
                        fieldVO.setFieldValue(o);
                        fieldVOS.add(fieldVO);
                    });
                    cadreVO.setFields(fieldVOS);
                    cadreVOS.add(cadreVO);
                }
                cadreDataVO.setCadres(cadreVOS);
            }
        }
        return ok(cadreDataVO);
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

            Map<String, Object> map = jdbcTemplate.queryForMap("select A01001 as \"name\" from EMP_A001 where EMP_ID = '" + empId + "'");
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
    public ResponseEntity modifySortInDep(@RequestBody List<CadreSortInDepModifyModel> modifyModels) throws BusinessException {
        return cadreService.modifySortInDep(modifyModels) ? ok("修改成功!") : fail("修改失败！");
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
}
