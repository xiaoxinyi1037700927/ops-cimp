package com.sinosoft.ops.cimp.controller.sys.sysapp.access;

import com.sinosoft.ops.cimp.annotation.SystemApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.sys.sysapp.acess.SysAppTableAccessService;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.access.SysAppTableAccessAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.access.SysAppTableAccessDeleteModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.access.SysAppTableAccessModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.access.SysAppTableAccessSearchModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SystemApiGroup
@Api(description = "app中角色对表的访问权限")
@RestController
@RequestMapping(value = "/sys/sysapp/tableAccess")
public class SysAppTableAccessController extends BaseController {

    private final SysAppTableAccessService tableAccessService;

    @Autowired
    public SysAppTableAccessController(SysAppTableAccessService tableAccessService) {
        this.tableAccessService = tableAccessService;
    }

    /**
     * 查询对表的访问权限列表
     */
    @ApiOperation(value = "查询对表的访问权限列表")
    @PostMapping("/list")
    public ResponseEntity listTableAccess(@RequestBody SysAppTableAccessSearchModel searchModel) throws BusinessException {
        return ok(tableAccessService.listTableAccess(searchModel));
    }

    /**
     * 新增对表的访问权限
     */
    @ApiOperation(value = "新增对表的访问权限")
    @PostMapping("/add")
    public ResponseEntity addTableAccess(@RequestBody SysAppTableAccessAddModel addModel) throws BusinessException {
        tableAccessService.addTableAccess(addModel);
        return ok("新增成功！");
    }

    /**
     * 删除对表的访问权限
     */
    @ApiOperation(value = "删除对表的访问权限")
    @PostMapping("/delete")
    public ResponseEntity deleteTableAccess(@RequestBody SysAppTableAccessDeleteModel deleteModel) throws BusinessException {
        tableAccessService.deleteTableAccess(deleteModel.getIds());
        return ok("删除成功！");
    }

    /**
     * 修改对表的访问权限
     */
    @ApiOperation(value = "修改对表的访问权限")
    @PostMapping("/modify")
    public ResponseEntity modifyTableAccess(@RequestBody SysAppTableAccessModifyModel modifyModel) throws BusinessException {
        return tableAccessService.modifyTableAccess(modifyModel) ? ok("修改成功！") : fail("修改失败！");
    }

}
