package com.sinosoft.ops.cimp.controller.organization;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sinosoft.ops.cimp.annotation.OrganizationApiGroup;
import com.sinosoft.ops.cimp.annotation.RequiresAuthentication;
import com.sinosoft.ops.cimp.constant.OpsErrorMessage;
import com.sinosoft.ops.cimp.constant.RolePermissionPageSqlEnum;
import com.sinosoft.ops.cimp.context.ExecuteContext;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.entity.oraganization.Organization;
import com.sinosoft.ops.cimp.entity.user.User;
import com.sinosoft.ops.cimp.entity.user.UserRole;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.oraganization.OrganizationService;
import com.sinosoft.ops.cimp.service.user.RolePermissionPageSqlService;
import com.sinosoft.ops.cimp.util.CachePackage.OrganizationCacheManager;
import com.sinosoft.ops.cimp.util.JsonUtil;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.vo.from.user.rolePermissionPageSql.RPPageSqlSearchModel;
import com.sinosoft.ops.cimp.vo.to.organization.*;
import com.sinosoft.ops.cimp.vo.to.user.rolePermissionPageSql.RPPageSqlViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@OrganizationApiGroup
@Api(description = "单位接口")
@RestController
@RequestMapping(value = "/organization")
@SuppressWarnings("unchecked")
public class OrganizationController extends BaseController {

    private final OrganizationService organizationService;
    private final RolePermissionPageSqlService rolePermissionPageSqlService;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OrganizationController(RolePermissionPageSqlService rolePermissionPageSqlService, OrganizationService organizationService, JdbcTemplate jdbcTemplate) {
        this.rolePermissionPageSqlService = rolePermissionPageSqlService;
        this.organizationService = organizationService;
        this.jdbcTemplate = jdbcTemplate;
    }


    @ApiOperation(value = "查询机构树")
    @PostMapping("/lstTreeNode")
    @RequiresAuthentication
    public ResponseEntity lstTreeNode(@RequestBody OrganizationSearchViewModel searchViewModel) throws BusinessException {
        OrganizationViewModel viewModel = organizationService.lstTreeNode(searchViewModel);
        Object organizationViewModelList = ExecuteContext.getVariable("organizationViewModelList");
        if (organizationViewModelList != null) {
            return ok(organizationViewModelList);
        }
        return ok(viewModel);
    }

    @ApiOperation(value = "根据父节点查询下级组织")
    @PostMapping("/findOrganizationByParentId")
    @RequiresAuthentication
    public ResponseEntity<List<OrganizationViewModel>> findOrganizationByParentId(String parentId) throws BusinessException {
        List<OrganizationViewModel> organViewModelList = organizationService.findOrganizationByParentId(parentId);
        return ok(organViewModelList);
    }

    @ApiOperation(value = "根据机构ID查询机构信息")
    @PostMapping("/findOrganizationById")
    @RequiresAuthentication
    public ResponseEntity<Organization> findOrganizationById(@RequestParam String organizationId) throws BusinessException {
        Organization organization = OrganizationCacheManager.getSubject().getOrganizationById(organizationId);
        if (organization != null) {
            return ok(organization);
        }
        return null;
    }


    @ApiOperation(value = "根据名字查询组织")
    @PostMapping("/findOrganizationByName")
    @RequiresAuthentication
    public ResponseEntity<List<OrganizationViewModel>> findOrganizationByName(@RequestParam("name") String name
            , @RequestParam(required = false) String permission) throws BusinessException {
        if (StringUtils.isEmpty(name)) return fail("名字不能为空");
        //permission: 1 带权限;0 不带权限
        //默认带权限
        if (StringUtils.isEmpty(permission)) {
            permission = "1";
        }
        List<OrganizationViewModel> organizationByName = organizationService.findOrganizationByName(name, permission);
        return ok(organizationByName);
    }

    @ApiOperation(value = "查询单位列表")
    @GetMapping(value = "/list")
    @RequiresAuthentication
    public ResponseEntity listDep(
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
        Optional<RPPageSqlViewModel> sqlViewModel = pageSqlByRoleList.stream().filter(s -> StringUtils.equals(s.getSqlNameEn(), RolePermissionPageSqlEnum.NAME_EN.单位集合.value)).filter(s -> StringUtils.equals(s.getIncludeSubNode(), includeSubNode)).findFirst();
        if (sqlViewModel.isPresent()) {
            RPPageSqlViewModel rpPageSqlViewModel = sqlViewModel.get();
            String execListSql = rpPageSqlViewModel.getExecListSql();
            String execCountSql = rpPageSqlViewModel.getExecCountSql();
            String selectCountFieldEn = rpPageSqlViewModel.getSelectCountFieldEn();
            String selectListFieldsEn = rpPageSqlViewModel.getSelectListFieldsEn();

            String execDepListSql = execListSql.replaceAll("\\$\\{deptId\\}", deptId)
                    .replaceAll("\\$\\{startIndex\\}", startIndex)
                    .replaceAll("\\$\\{endIndex\\}", endIndex);

            String execDepCountSql = execCountSql.replaceAll("\\$\\{deptId\\}", deptId);

            List<Map<String, Object>> mapList = jdbcTemplate.queryForList(execDepListSql);
            Map<String, Object> countMap = jdbcTemplate.queryForMap(execDepCountSql);

            DepDataVO depDataVO = new DepDataVO();
            depDataVO.setPageIndex(Integer.parseInt(pageIndex));
            depDataVO.setPageSize(Integer.parseInt(pageSize));
            Object depCount = countMap.get(selectCountFieldEn);
            if (depCount == null) {
                depDataVO.setDataCount(0L);
            } else {
                depDataVO.setDataCount(Long.parseLong(String.valueOf(depCount)));
            }

            List<DepVO> depVOS = Lists.newArrayList();
            Map selectFields = JsonUtil.parseStringToObject(selectListFieldsEn, LinkedHashMap.class);

            for (Map<String, Object> map : mapList) {
                DepVO depVO = new DepVO();
                List<DepFieldVO> fieldVOS = Lists.newArrayList();
                selectFields.forEach((k, v) -> {
                    DepFieldVO fieldVO = new DepFieldVO();
                    Object o = map.get(k);
                    fieldVO.setFieldNameEn(k);
                    fieldVO.setFieldNameCn(v);
                    fieldVO.setFieldValue(o);
                    fieldVOS.add(fieldVO);
                });
                depVO.setFields(fieldVOS);
                depVOS.add(depVO);
            }
            depDataVO.setDeps(depVOS);
            depDataVO.setTableFields(selectFields);
            return ok(depDataVO);
        } else {
            throw new BusinessException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE, "请检查角色配置的权限");
        }
    }

    @ApiOperation(value = "查询职务信息")
    @GetMapping(value = "/getDutyInfo")
    @RequiresAuthentication
    public ResponseEntity getDutyInfo(@RequestParam("deptId") String deptId) throws BusinessException {
        //B01027 任职机构级别
        //B01017 任职机构所在行政区划
        //B01031 任职机构性质类别
        //B01044B 隶属单位代码
        //B01044A 隶属单位名称
        Map<String, String> result = Maps.newHashMap();
        if (StringUtils.isNotEmpty(deptId)) {
            String sql = "SELECT B01027,B01017,B01031,B01044_B,B01044_A FROM DEP_B001 WHERE DEP_ID = '%s'";
            String execSql = String.format(sql, deptId);

            List<Map<String, Object>> maps = jdbcTemplate.queryForList(execSql);
            if (maps.size() > 0) {
                Map<String, Object> map = maps.get(0);
                Object B01027 = map.get("B01027");
                Object B01017 = map.get("B01017");
                Object B01031 = map.get("B01031");
                Object B01044B = map.get("B01044_B");
                Object B01044A = map.get("B01044_A");
                result.put("B01027", String.valueOf(B01027));
                result.put("B01017", String.valueOf(B01017));
                result.put("B01031", String.valueOf(B01031));
                result.put("B01044B", String.valueOf(B01044B));
                result.put("B01044A", String.valueOf(B01044A));
            }
        }
        return ok(result);
    }
}
