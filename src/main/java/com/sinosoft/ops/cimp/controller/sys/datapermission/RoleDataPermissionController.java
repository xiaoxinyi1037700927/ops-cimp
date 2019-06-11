package com.sinosoft.ops.cimp.controller.sys.datapermission;


import com.sinosoft.ops.cimp.annotation.RequiresAuthentication;
import com.sinosoft.ops.cimp.annotation.SystemLimitsApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.sys.datapermission.RoleDataPermissionService;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.RoleDataPerModifyModel;
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


    @ApiOperation(value = "修改数据权限")
    @PostMapping("/modify")
    @RequiresAuthentication
    public ResponseEntity modifyRoleDataPermission(@RequestBody RoleDataPerModifyModel modifyModel) throws BusinessException {
        roleDataPermissionService.modifyRoleDataPermission(modifyModel);
        return ok("修改成功！");
    }

}
