package com.sinosoft.ops.cimp.controller.sys.permission;

import com.google.common.collect.Lists;
import com.sinosoft.ops.cimp.annotation.RequiresAuthentication;
import com.sinosoft.ops.cimp.annotation.SystemLimitsApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.entity.sys.permission.PermissionPage;
import com.sinosoft.ops.cimp.entity.sys.permission.PermissionPageOperation;
import com.sinosoft.ops.cimp.entity.sys.permission.QPermissionPageOperation;
import com.sinosoft.ops.cimp.entity.user.QRolePermissionPage;
import com.sinosoft.ops.cimp.entity.user.RolePermissionPage;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.repository.sys.permission.PermissionPageOperationRepository;
import com.sinosoft.ops.cimp.repository.sys.permission.PermissionPageRepository;
import com.sinosoft.ops.cimp.repository.user.RolePermissionPageRepository;
import com.sinosoft.ops.cimp.service.sys.permission.PermissionPageService;
import com.sinosoft.ops.cimp.vo.from.sys.permission.PermissionPageSearchVO;
import com.sinosoft.ops.cimp.vo.to.sys.permission.PermissionBatchVO;
import com.sinosoft.ops.cimp.vo.to.sys.permission.PermissionPageOperationVO;
import com.sinosoft.ops.cimp.vo.to.sys.permission.PermissionPageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SystemLimitsApiGroup
@Api(description = "功能权限细化到操作")
@RestController
@RequestMapping("/sys/permissionPage")
public class PermissionPageController extends BaseController {
    @Autowired
    private PermissionPageService permissionPageService;
    @Autowired
    private PermissionPageRepository permissionPageRepository;
    @Autowired
    private PermissionPageOperationRepository permissionPageOperationRepository;
    @Autowired
    private RolePermissionPageRepository rolePermissionPageRepository;

    @ApiOperation(value = "查询页面所拥禁用操作")
    @PostMapping("/findProhibitOperation")
    @RequiresAuthentication
    public ResponseEntity<List<PermissionPageOperationVO>> findProhibitOperation(@RequestBody PermissionPageSearchVO searchVO) throws BusinessException {
        List<PermissionPageOperationVO> prohibitOperation = permissionPageService.findProhibitOperation(searchVO);
        return ok(prohibitOperation);
    }

    @ApiOperation(value = "增加页面")
    @PostMapping("/addPermissionPage")
    @RequiresAuthentication
    public ResponseEntity<Boolean> addPermissionPage(@RequestBody PermissionPageVO permissionPageVO) throws BusinessException {
        Boolean aBoolean = permissionPageService.addPermissionPage(permissionPageVO);
        return ok(aBoolean);
    }

    @ApiOperation(value = "查询页面")
    @PostMapping("/findPermissionPage")
    @RequiresAuthentication
    public ResponseEntity<List<PermissionPageVO>> findPermissionPage(@RequestBody PermissionPageSearchVO searchVO) throws BusinessException {
        List<PermissionPageVO> permissionPage = permissionPageService.findPermissionPage(searchVO);
        return ok(permissionPage);
    }

    @ApiOperation(value = "通过ID删除页面")
    @PostMapping("/deletePermissionPageById")
    @RequiresAuthentication
    public ResponseEntity<Boolean> deletePermissionPageById(@RequestParam String permissionPageId) throws BusinessException {
        //查询页面下的操作
        ArrayList<PermissionPageOperation> permissionPageOperations = Lists.newArrayList(permissionPageOperationRepository.findAll(QPermissionPageOperation.permissionPageOperation.permissionPageId.eq(permissionPageId)));
        List<String> ids = permissionPageOperations.stream().map(x -> x.getId()).collect(Collectors.toList());
        Iterable<RolePermissionPage> all = rolePermissionPageRepository.findAll(QRolePermissionPage.rolePermissionPage.permissionPageOperationId.in(ids));
        //删除被角色使用的操作
        if(all != null){
            rolePermissionPageRepository.deleteAll(all);
        }
        //删除操作
        if(permissionPageOperations.size() >0){
            permissionPageOperationRepository.deleteAll(permissionPageOperations);
        }

        PermissionPage permissionPage = permissionPageRepository.findById(permissionPageId).get();
        String permissionId = permissionPage.getPermissionId();

        //删除页面
        permissionPageRepository.deleteById(permissionPageId);

        permissionPageService.pageCount(permissionId);
        return ok(true);
    }


    @ApiOperation(value = "增加操作")
    @PostMapping("/addPermissionPageOperation")
    @RequiresAuthentication
    public ResponseEntity<Boolean> addPermissionPageOperation(@RequestBody PermissionPageOperationVO voList) throws BusinessException {
        Boolean aBoolean = permissionPageService.addPermissionPageOperation(voList);
        return ok(aBoolean);
    }

    @ApiOperation(value = "查询页面操作")
    @PostMapping("/findPermissionPageOperation")
    @RequiresAuthentication
    public ResponseEntity<List<PermissionPageOperationVO>> findPermissionPageOperation(@RequestParam String permissionPageId) throws BusinessException {
        List<PermissionPageOperationVO> permissionPageOperation = permissionPageService.findPermissionPageOperation(permissionPageId);
        return ok(permissionPageOperation);
    }

    @ApiOperation(value = "查询页面操作(角色)")
    @PostMapping("/findPermissionPageOperationForRole")
    @RequiresAuthentication
    public ResponseEntity<List<PermissionPageOperationVO>> findPermissionPageOperationForRole(@RequestParam String permissionPageId, @RequestParam String roleId) throws BusinessException {
        List<PermissionPageOperationVO> permissionPageOperation = permissionPageService.findPermissionPageOperationForRole(permissionPageId, roleId);
        return ok(permissionPageOperation);
    }

    @ApiOperation(value = "通过ID删除操作")
    @PostMapping("/deletePermissionPageOperationById")
    @RequiresAuthentication
    public ResponseEntity<Boolean> deletePermissionPageOperationById(@RequestParam String permissionPageOperationId) throws BusinessException {
        //删除被使用的操作
        Iterable<RolePermissionPage> all = rolePermissionPageRepository.findAll(QRolePermissionPage.rolePermissionPage.permissionPageOperationId.eq(permissionPageOperationId));
        if(all != null){
            rolePermissionPageRepository.deleteAll(all);
        }
        permissionPageOperationRepository.deleteById(permissionPageOperationId);
        return ok(true);
    }

    @ApiOperation(value = "禁用/启用 操作权限")
    @PostMapping("/switchPermissionPageOperation")
    @RequiresAuthentication
    public ResponseEntity<Boolean> switchPermissionPageOperation(@RequestParam String permissionPageOperationId,
                                                         @RequestParam String roleId) throws BusinessException {
        Boolean aBoolean = permissionPageService.switchPermissionPageOperation(permissionPageOperationId, roleId);
        return ok(aBoolean);
    }

    @ApiOperation(value = "禁用/启用 操作权限 批量操作")
    @PostMapping("/switchPermissionPageOperationList")
    @RequiresAuthentication
    public ResponseEntity<Boolean> switchPermissionPageOperationList(@RequestBody PermissionBatchVO permissionBatchVO) throws BusinessException {
        Boolean aBoolean = permissionPageService.switchPermissionPageOperation(permissionBatchVO.getPermissionPageOperationIds(), permissionBatchVO.getRoleId(),permissionBatchVO.getType());
        return ok(aBoolean);
    }


}
