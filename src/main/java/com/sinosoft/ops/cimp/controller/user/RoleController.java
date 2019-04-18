package com.sinosoft.ops.cimp.controller.user;

import com.sinosoft.ops.cimp.config.annotation.RequiresAuthentication;
import com.sinosoft.ops.cimp.config.annotation.RequiresRoles;
import com.sinosoft.ops.cimp.config.annotation.SystemUserApiGroup;
import com.sinosoft.ops.cimp.constant.UserRoleConstants;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.entity.user.Role;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.mapper.user.RoleViewModelMapper;
import com.sinosoft.ops.cimp.service.user.RoleService;
import com.sinosoft.ops.cimp.vo.from.sys.role.RoleModel;
import com.sinosoft.ops.cimp.vo.to.sys.role.RoleViewModel;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@SystemUserApiGroup
@Api(description = "角色管理接口")
@RestController
@RequestMapping("/user/role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "查询角色列表")
    @PostMapping("/findData")
    @RequiresAuthentication
    public ResponseEntity<List<RoleViewModel>> findData() throws BusinessException {
        Iterable<Role> roleLst = roleService.findData();
        List<RoleViewModel> roleViewModelLst = new ArrayList<>();
        roleLst.iterator().forEachRemaining(role->{
            roleViewModelLst.add(RoleViewModelMapper.INSTANCE.roleToRoleViewModel(role));
        });
        return ok(roleViewModelLst);

    }



    @ApiOperation(value ="根据id查询角色")
    @ApiImplicitParam(name = "roleId", paramType = "query", value = "角色ID", required = true)
    @PostMapping("/getById")
    @RequiresAuthentication
    public ResponseEntity<RoleViewModel> getById(@RequestParam String roleId) throws BusinessException {
        Role role = roleService.getById(roleId);
        if (role == null) {
            return fail("该数据不存在!");
        }
        RoleViewModel roleViewModel = RoleViewModelMapper.INSTANCE.roleToRoleViewModel(role);
        return ok(roleViewModel);
    }

    @ApiOperation(value = "新增角色")
    @ApiParam(value = "新增角色参数", required = true)
    @PostMapping("/save")
    @RequiresAuthentication
    @RequiresRoles(UserRoleConstants.ADMIN_ROLE)
    public ResponseEntity<RoleViewModel> save(@Valid @RequestBody RoleModel roleModel) throws BusinessException {
        Role role = roleService.save(roleModel);
        RoleViewModel roleViewModel = RoleViewModelMapper.INSTANCE.roleToRoleViewModel(role);
        return ok(roleViewModel);
    }

    @ApiOperation(value = "修改角色")
    @ApiParam(value = "修改角色参数", required = true)
    @PostMapping("/update")
    @RequiresAuthentication
    @RequiresRoles(UserRoleConstants.ADMIN_ROLE)
    public ResponseEntity<RoleViewModel> update(@Valid @RequestBody RoleModel roleModel) throws BusinessException {
        Role role = roleService.update(roleModel);
        if (role == null) {
            return fail("修改角色异常！");
        }
        RoleViewModel roleViewModel = RoleViewModelMapper.INSTANCE.roleToRoleViewModel(role);
        return ok(roleViewModel);
    }

    @ApiOperation(value = "根据id删除角色")
    @ApiImplicitParam(name = "roleId", paramType = "query", value = "角色ID", required = true)
    @PostMapping("/deleteById")
    @RequiresAuthentication
    @RequiresRoles(UserRoleConstants.ADMIN_ROLE)
    public ResponseEntity<RoleViewModel> deleteById(@RequestParam String roleId) throws BusinessException {
        if (StringUtils.isEmpty(roleId)) {
            return fail("角色ID不能为空！");
        }
        roleService.deleteById(roleId);
        return ok(true);
    }

    @ApiOperation(value = "新增角色权限关系")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", paramType = "query", value = "角色ID", required = true),
            @ApiImplicitParam(name = "permissionId", paramType = "query", value = "权限ID", required = true)
    })
    @RequiresAuthentication
    @RequiresRoles(UserRoleConstants.ADMIN_ROLE)
    public ResponseEntity<RoleViewModel> addRelation(@RequestParam String roleId,
                                             @RequestParam String permissionId) {
        return null;
    }
}
