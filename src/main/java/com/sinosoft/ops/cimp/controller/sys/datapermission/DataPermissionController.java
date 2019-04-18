package com.sinosoft.ops.cimp.controller.sys.datapermission;


import com.sinosoft.ops.cimp.config.annotation.RequiresAuthentication;
import com.sinosoft.ops.cimp.config.annotation.SystemLimitsApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.repository.sys.datapermission.PageInterfaceRepository;
import com.sinosoft.ops.cimp.service.sys.datapermission.DataPermissionService;
import com.sinosoft.ops.cimp.vo.to.sys.datapermission.PageInterfaceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Jay on 2019/2/22.
 *
 * @author Jay
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinoSoft All Rights Received
 */
@SystemLimitsApiGroup
@Api(description = "功能数据权限")
@RestController
@RequestMapping("/dataPermission")
public class DataPermissionController extends BaseController {

    @Autowired
    private DataPermissionService dataPermissionService;
    @Autowired
    private PageInterfaceRepository pageInterfaceRepository;

    @ApiOperation(value = "新增接口")
    @PostMapping("/savePageInterface")
    @RequiresAuthentication
    public ResponseEntity<Boolean> savePageInterface(@RequestBody PageInterfaceVO vo) throws BusinessException {
        Boolean aBoolean = dataPermissionService.savePageInterface(vo);
        return ok(aBoolean);
    }

    @ApiOperation(value = "查询接口")
    @PostMapping("/findPageInterfaceVOList")
    @RequiresAuthentication
    public ResponseEntity<List<PageInterfaceVO>> findPageInterfaceVOList(@RequestParam String permissionPageId,
                                                                 @RequestParam(required = false) String roleId) throws BusinessException {
        List<PageInterfaceVO> pageInterfaceVOList = dataPermissionService.findPageInterfaceVOList(permissionPageId, roleId);
        return ok(pageInterfaceVOList);
}

    @ApiOperation(value = "通过ID删除操作")
    @PostMapping("/deletePageInterfaceVOById")
    @RequiresAuthentication
    public ResponseEntity<Boolean> deletePageInterfaceVOById(@RequestParam String id) throws BusinessException {
        pageInterfaceRepository.deleteById(id);
        return ok(true);
    }

    @ApiOperation(value = "给角色下接口填入sql")
    @PostMapping("/fillSqlToRole")
    @RequiresAuthentication
    public ResponseEntity<Boolean> fillSqlToRole(@RequestParam String id,
                                         @RequestParam String roleId,
                                         @RequestParam String roleSql) throws BusinessException {
        Boolean aBoolean = dataPermissionService.fillSqlToRole(id, roleId, roleSql);
        return ok(aBoolean);
}



}
