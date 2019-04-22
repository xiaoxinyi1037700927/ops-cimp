package com.sinosoft.ops.cimp.controller.user;

import com.sinosoft.ops.cimp.annotation.RequiresAuthentication;
import com.sinosoft.ops.cimp.annotation.SystemUserApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.user.RolePermissionTable;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.user.RolePermissionTableService;
import com.sinosoft.ops.cimp.vo.from.user.rolePermissionTable.RPTableAddModel;
import com.sinosoft.ops.cimp.vo.from.user.rolePermissionTable.RPTableDeleteModel;
import com.sinosoft.ops.cimp.vo.from.user.rolePermissionTable.RPTableSearchModel;
import com.sinosoft.ops.cimp.vo.to.user.rolePermissionTable.RPTableViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SystemUserApiGroup
@Api(description = "角色关联table 管理接口")
@RestController
@RequestMapping("/user/rolePermissionTableController")
public class RolePermissionTableController extends BaseController {
    @Autowired
    private RolePermissionTableService rolePermissionTableService;

    @ApiOperation(value = "查询角色关联table分页列表")
    @PostMapping("/findRPTablePageList")
    @RequiresAuthentication
    public ResponseEntity<PaginationViewModel<RPTableViewModel>> findRPTablePageList(@RequestBody RPTableSearchModel searchModel) throws BusinessException {
        PaginationViewModel<RPTableViewModel> rpTablePageList = rolePermissionTableService.findRPTablePageList(searchModel);
        return ok(rpTablePageList);
    }

//    @ApiOperation(value = "根据RoleId查询角色关联table列表")
//    @PostMapping("/findRPTableListByRoleId")
//    @RequiresAuthentication
//    public ResponseEntity<List<RPTableViewModel>> findRPTableListByRoleId(@RequestParam String roleId) throws BusinessException {
//        List<RPTableViewModel> rpTableListByRoleId = rolePermissionTableService.findRPTableListByRoleId(roleId);
//        return ok(rpTableListByRoleId);
//    }

    @ApiOperation(value = "新增查询角色关联table")
    @PostMapping("/saveRPTable")
    @RequiresAuthentication
    public ResponseEntity<Boolean> saveRPTable(@RequestBody RPTableAddModel addModel) throws BusinessException {
        Boolean result = rolePermissionTableService.saveRPTable(addModel);
        if (result) {
            return ok(result);
        } else {
            return fail("新增失败！");
        }
    }

    @ApiOperation(value = "修改查询角色关联table")
    @PostMapping("/modifyRPTable")
    @RequiresAuthentication
    public ResponseEntity<Boolean> modifyRPTable(@RequestBody RPTableViewModel modifyModel) throws BusinessException {
        Boolean result = rolePermissionTableService.modifyRPTable(modifyModel);
        if (result) {
            return ok(result);
        } else {
            return fail("修改失败！");
        }
    }

    @ApiOperation(value = "删除查询角色关联table")
    @PostMapping("/deleteRPTable")
    @RequiresAuthentication
    public ResponseEntity<Boolean> deleteRPTable(@RequestBody RPTableDeleteModel deleteModel) throws BusinessException {
        if (deleteModel.getIds() == null || deleteModel.getIds().size() == 0) {
            return fail("id不能为空！");
        }
        Boolean result = rolePermissionTableService.deleteRPTable(deleteModel.getIds());
        if (result) {
            return ok(result);
        } else {
            return fail("删除失败！");
        }
    }

    @ApiOperation(value = "修改查询角色关联table排序")
    @PostMapping("/changeRPTableSort")
    @RequiresAuthentication
    public ResponseEntity<Boolean> changeRPTableSort(@RequestBody RPTableDeleteModel deleteModel) throws BusinessException {
        if (deleteModel.getIds() == null || deleteModel.getIds().size() != 2) {
            return fail("参数有误！");
        }
        Boolean result = rolePermissionTableService.changeRPTableSort(deleteModel.getIds());
        if (result) {
            return ok(result);
        } else {
            return fail("换位失败！");
        }
    }

}
