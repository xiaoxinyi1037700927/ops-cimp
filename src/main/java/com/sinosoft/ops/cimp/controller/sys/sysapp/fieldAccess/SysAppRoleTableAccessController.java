package com.sinosoft.ops.cimp.controller.sys.sysapp.fieldAccess;

import com.sinosoft.ops.cimp.annotation.SystemApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.sys.sysapp.SysAppService;
import com.sinosoft.ops.cimp.util.CachePackage.SysTableModelInfoManager;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysApp.SysAppAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysApp.SysAppDeleteModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysApp.SysAppModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysApp.SysAppSearchModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@SystemApiGroup
@Api(description = "角色对表的访问权限")
@RestController
@RequestMapping(value = "/sys/sysapp/tableAccess")
public class SysAppRoleTableAccessController extends BaseController {

    @Autowired
    private SysAppService sysAppService;

    /**
     * 查询系统应用列表
     */
    @ApiOperation(value = "查询系统应用列表")
    @PostMapping("/list")
    public ResponseEntity listSysApp(@RequestBody SysAppSearchModel searchModel) throws BusinessException {
        return ok(sysAppService.listSysApp(searchModel));
    }

    /**
     * 新增系统应用
     */
    @ApiOperation(value = "新增系统应用")
    @PostMapping("/add")
    public ResponseEntity addSysApp(@Valid @RequestBody SysAppAddModel addModel) throws BusinessException {
        SysTableModelInfoManager.removeAllCache();
        SysTableModelInfoManager.removeAllCache();
        sysAppService.addSysApp(addModel);
        return ok("新增成功！");
    }

    /**
     * 删除系统应用
     */
    @ApiOperation(value = "删除系统应用")
    @PostMapping("/delete")
    public ResponseEntity deleteSysApp(@RequestBody SysAppDeleteModel deleteModel) throws BusinessException {
        SysTableModelInfoManager.removeAllCache();
        sysAppService.deleteSysApp(deleteModel.getIds());
        return ok("删除成功！");
    }

    /**
     * 修改系统应用
     */
    @ApiOperation(value = "修改系统应用")
    @PostMapping("/modify")
    public ResponseEntity modifySysApp(@RequestBody SysAppModifyModel modifyModel) throws BusinessException {
        SysTableModelInfoManager.removeAllCache();
        return sysAppService.modifySysApp(modifyModel) ? ok("修改成功！") : fail("修改失败！");
    }

}
