package com.sinosoft.ops.cimp.controller.user;

import com.sinosoft.ops.cimp.annotation.RequiresAuthentication;
import com.sinosoft.ops.cimp.annotation.SystemUserApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.user.RolePermissionPageSqlService;
import com.sinosoft.ops.cimp.vo.from.user.rolePermissionPageSql.RPPageSqlAddModel;
import com.sinosoft.ops.cimp.vo.from.user.rolePermissionPageSql.RPPageSqlDeleteModel;
import com.sinosoft.ops.cimp.vo.from.user.rolePermissionPageSql.RPPageSqlSearchModel;
import com.sinosoft.ops.cimp.vo.to.user.rolePermissionPageSql.RPPageSqlViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SystemUserApiGroup
@Api(description = "角色权限页面SQL管理接口")
@RestController
@RequestMapping("/user/rolePermissionPageSql")
public class RolePermissionPageSqlController extends BaseController {
    @Autowired
    private RolePermissionPageSqlService rolePermissionPageSqlService;

    @ApiOperation(value = "查询角色权限页面SQL列表")
    @PostMapping("/findRPPageSqlPageList")
    @RequiresAuthentication
    public ResponseEntity<PaginationViewModel<RPPageSqlViewModel>> findRPPageSqlPageList(@RequestBody RPPageSqlSearchModel searchModel) throws BusinessException {
        PaginationViewModel<RPPageSqlViewModel> rpPageSqlPageList = rolePermissionPageSqlService.findRPPageSqlPageList(searchModel);
        return ok(rpPageSqlPageList);
    }

    @ApiOperation(value = "根据roleId查询角色权限页面SQL")
    @PostMapping("/findRPPageSqlByRoleId")
    @RequiresAuthentication
    public ResponseEntity<RPPageSqlViewModel> findRPPageSqlByRoleId(@RequestParam String roleId) throws BusinessException {
        RPPageSqlViewModel rpPageSqlByRoleId = rolePermissionPageSqlService.findRPPageSqlByRoleId(roleId);
        return ok(rpPageSqlByRoleId);
    }

    @ApiOperation(value = "根据roleIds查询角色权限页面SQL列表")
    @PostMapping("/findRPPageSqlListByRoleIds")
    @RequiresAuthentication
    public ResponseEntity<List<RPPageSqlViewModel>> findRPPageSqlListByRoleIds(@RequestBody RPPageSqlSearchModel searchModel) throws BusinessException {
        List<RPPageSqlViewModel> rpPageSqlListByRoleIds = rolePermissionPageSqlService.findRPPageSqlListByRoleIds(searchModel);
        return ok(rpPageSqlListByRoleIds);
    }

    @ApiOperation(value = "新增角色权限页面SQL")
    @PostMapping("/saveRPPageSql")
    @RequiresAuthentication
    public ResponseEntity<Boolean> saveRPPageSql(@RequestBody RPPageSqlAddModel addModel) throws BusinessException {
        boolean saveMenuParent = rolePermissionPageSqlService.saveRPPageSql(addModel);
        if (saveMenuParent) {
            return ok(saveMenuParent);
        } else {
            return fail("保存失败");
        }
    }

    @ApiOperation(value = "修改角色权限页面SQL")
    @PostMapping("/modifyRPPageSql")
    @RequiresAuthentication
    public ResponseEntity<Boolean> modifyRPPageSql(@RequestBody RPPageSqlViewModel modifyModel) throws BusinessException {
        boolean saveMenuParent = rolePermissionPageSqlService.modifyRPPageSql(modifyModel);
        if (saveMenuParent) {
            return ok(saveMenuParent);
        } else {
            return fail("修改失败");
        }
    }

    @ApiOperation(value = "删除角色权限页面SQL")
    @PostMapping("/deleteRPPageSql")
    @RequiresAuthentication
    public ResponseEntity<Boolean> deleteRPPageSql(@RequestBody RPPageSqlDeleteModel deleteModel) throws BusinessException {
        if (deleteModel.getIds() == null || deleteModel.getIds().size() == 0) {
            return fail("ids不能为空!");
        }
        boolean saveMenuParent = rolePermissionPageSqlService.deleteRPPageSql(deleteModel.getIds());
        if (saveMenuParent) {
            return ok(saveMenuParent);
        } else {
            return fail("删除失败");
        }
    }

}
