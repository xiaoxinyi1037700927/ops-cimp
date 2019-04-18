package com.sinosoft.ops.cimp.controller.sys.user;

import com.sinosoft.ops.cimp.config.annotation.RequiresAuthentication;
import com.sinosoft.ops.cimp.config.annotation.RequiresRoles;
import com.sinosoft.ops.cimp.config.annotation.SystemLimitsApiGroup;
import com.sinosoft.ops.cimp.constant.UserRoleConstants;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.entity.sys.user.Permission;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.mapper.user.PermissionViewModelMapper;
import com.sinosoft.ops.cimp.service.user.PermissionService;
import com.sinosoft.ops.cimp.vo.user.PermissionModel;
import com.sinosoft.ops.cimp.vo.user.PermissionViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@SystemLimitsApiGroup
@Api(description = "权限管理接口")
@RestController
@RequestMapping("/sys/user/userpermission")
public class UserPermissionController extends BaseController {

    @Autowired
    private PermissionService permissionService;

    @ApiOperation(value = "查询权限列表")
    @PostMapping("/findData")
    @RequiresAuthentication
    public ResponseEntity<List<PermissionViewModel>> findData(@RequestBody PermissionModel permissionModel) throws BusinessException {
        Iterable<Permission> rightLst = permissionService.findData(permissionModel);
        List<PermissionViewModel> rightViewModelLst = new ArrayList<>();
        rightLst.iterator().forEachRemaining(right -> {
            rightViewModelLst.add(PermissionViewModelMapper.INSTANCE.permissionToPermissionViewModel(right));
        });
        return ok(rightViewModelLst);
    }


    @ApiOperation(value = "根据id查询权限")
    @ApiImplicitParam(name = "rightId", paramType = "query", value = "权限ID", required = true)
    @PostMapping(value="/getById")
    @RequiresAuthentication
    public ResponseEntity<Object> getById(@RequestParam String rightId) throws BusinessException {
        Permission permission = permissionService.getById(rightId);
        if (permission == null) {
            return fail("该数据不存在!");
        }
        PermissionViewModel permissionViewModel = PermissionViewModelMapper.INSTANCE.permissionToPermissionViewModel(permission);
        return ok(permissionViewModel);
    }


    @ApiOperation(value = "新增权限")
    @ApiParam(value = "新增权限参数", required = true)
    @PostMapping(value = "/save")
    @RequiresAuthentication
    @RequiresRoles(UserRoleConstants.ADMIN_ROLE)
    public ResponseEntity<PermissionViewModel> save(@Valid @RequestBody PermissionModel permissionModel) throws BusinessException {
        Permission permission = permissionService.save(permissionModel);
        PermissionViewModel permissionViewModel = PermissionViewModelMapper.INSTANCE.permissionToPermissionViewModel(permission);
        return ok(permissionViewModel);
    }


    @ApiOperation(value = "修改权限")
    @ApiParam(value = "修改权限参数", required = true)
    @PostMapping(value = "/update")
    @RequiresAuthentication
    @RequiresRoles(UserRoleConstants.ADMIN_ROLE)
    public ResponseEntity<Object> update(@Valid @RequestBody PermissionModel permissionModel) throws BusinessException {
        Permission permission = permissionService.update(permissionModel);
        if (permission == null) {
            return fail("修改权限异常！");
        }
        PermissionViewModel permissionViewModel = PermissionViewModelMapper.INSTANCE.permissionToPermissionViewModel(permission);
        return ok(permissionViewModel);
    }

    @ApiOperation(value = "根据id删除权限")
    @ApiImplicitParam(name = "permissionId", paramType = "query",value = "权限ID", required = true)
    @PostMapping(value = "/deleteById")
    @RequiresAuthentication
    @RequiresRoles(UserRoleConstants.ADMIN_ROLE)
    public ResponseEntity<Object> deleteById(@RequestParam String permissionId) throws BusinessException {
        if (StringUtils.isEmpty(permissionId)) {
            return fail("权限ID不能为空！");
        }
        permissionService.deleteById(permissionId);
        return ok(null);
    }

}
