package com.sinosoft.ops.cimp.controller.sys.datapermission;


import com.sinosoft.ops.cimp.annotation.RequiresAuthentication;
import com.sinosoft.ops.cimp.annotation.SystemLimitsApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.sys.datapermission.RoleDataPermissionService;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.RoleDataPerAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.RoleDataPerDeleteModel;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.RoleDataPerModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.RoleDataPerSearchModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@SystemLimitsApiGroup
@Api(description = "角色数据权限")
@RestController
@RequestMapping("/sys/roleDataPermission")
public class RoleDataPermissionController extends BaseController {

    private final RoleDataPermissionService roleDataPermissionService;

    public RoleDataPermissionController(RoleDataPermissionService roleDataPermissionService) {
        this.roleDataPermissionService = roleDataPermissionService;
    }


    @ApiOperation(value = "数据权限列表")
    @PostMapping("/list")
    @RequiresAuthentication
    public ResponseEntity listRoleDataPermission(@RequestBody RoleDataPerSearchModel searchModel) throws BusinessException {
        return ok(roleDataPermissionService.listRoleDataPermission(searchModel));
    }


    @ApiOperation(value = "数据权限列表")
    @PostMapping("/sqlType")
    public ResponseEntity getSqlTypes() throws BusinessException {
        return ok(roleDataPermissionService.getSqlTypes());
    }


    @ApiOperation(value = "添加数据权限")
    @PostMapping("/add")
    @RequiresAuthentication
    public ResponseEntity addRoleDataPermission(@RequestBody RoleDataPerAddModel addModel) throws BusinessException {
        roleDataPermissionService.addRoleDataPermission(addModel);
        return ok("添加成功！");
    }

    @ApiOperation(value = "修改数据权限")
    @PostMapping("/modify")
    @RequiresAuthentication
    public ResponseEntity modifyRoleDataPermission(@RequestBody RoleDataPerModifyModel modifyModel) throws BusinessException {
        return ok(roleDataPermissionService.modifyRoleDataPermission(modifyModel) ? "修改成功！" : "修改失败！");
    }

    @ApiOperation(value = "删除数据权限")
    @PostMapping("/delete")
    @RequiresAuthentication
    public ResponseEntity deleteRoleDataPermission(@RequestBody RoleDataPerDeleteModel deleteModel) throws BusinessException {
        roleDataPermissionService.deleteRoleDataPermission(deleteModel);
        return ok("删除成功！");
    }

}
