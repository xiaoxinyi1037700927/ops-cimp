package com.sinosoft.ops.cimp.controller.cadre;

import com.google.common.collect.Lists;
import com.sinosoft.ops.cimp.annotation.BusinessApiGroup;
import com.sinosoft.ops.cimp.annotation.RequiresAuthentication;
import com.sinosoft.ops.cimp.constant.OpsErrorMessage;
import com.sinosoft.ops.cimp.constant.RolePermissionPageSqlEnum;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.entity.user.User;
import com.sinosoft.ops.cimp.entity.user.UserRole;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.user.RolePermissionPageSqlService;
import com.sinosoft.ops.cimp.util.JsonUtil;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.vo.from.user.rolePermissionPageSql.RPPageSqlSearchModel;
import com.sinosoft.ops.cimp.vo.to.cadre.CadreDataVO;
import com.sinosoft.ops.cimp.vo.to.cadre.CadreFieldVO;
import com.sinosoft.ops.cimp.vo.to.cadre.CadreVO;
import com.sinosoft.ops.cimp.vo.to.user.rolePermissionPageSql.RPPageSqlViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@BusinessApiGroup
@Api(description = "干部列表接口")
@RestController
@RequestMapping(value = "/cadre")
public class CadreController extends BaseController {

    private final RolePermissionPageSqlService rolePermissionPageSqlService;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CadreController(RolePermissionPageSqlService rolePermissionPageSqlService, JdbcTemplate jdbcTemplate) {
        this.rolePermissionPageSqlService = rolePermissionPageSqlService;
        this.jdbcTemplate = jdbcTemplate;
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
            return ok(cadreDataVO);
        } else {
            throw new BusinessException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE, "请检查角色配置的干部信息数据权限");
        }
    }

}
