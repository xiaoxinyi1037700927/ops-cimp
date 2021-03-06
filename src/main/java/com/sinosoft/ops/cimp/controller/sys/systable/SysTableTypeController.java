package com.sinosoft.ops.cimp.controller.sys.systable;


import com.sinosoft.ops.cimp.annotation.SystemApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.sys.systable.SysTableTypeService;
import com.sinosoft.ops.cimp.util.CachePackage.SysTableModelInfoManager;
import com.sinosoft.ops.cimp.vo.from.sys.systable.SysTableTypeAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.systable.SysTableTypeModifyModel;
import com.sinosoft.ops.cimp.vo.to.sys.systable.SysTableTypeModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@SystemApiGroup
@Api(description = "系统表类别操纵")
@RestController
@RequestMapping(value = "/sys/systable/type")
public class SysTableTypeController extends BaseController {

    private final SysTableTypeService sysTableTypeService;

    @Autowired
    public SysTableTypeController(SysTableTypeService sysTableTypeService) {
        this.sysTableTypeService = sysTableTypeService;
    }

    @ApiOperation(value = "新增系统表类别")
    @RequestMapping(value = "/addSysTableType", method = RequestMethod.POST)
    public ResponseEntity saveSysEntityGroupDef(
            @RequestBody SysTableTypeAddModel sysTableTypeAddModel) throws BusinessException {
        SysTableModelInfoManager.removeAllCache();
        boolean isok = sysTableTypeService.addSysTableType(sysTableTypeAddModel);
        if (isok) {
            return ok("操作成功");
        }
        return fail("操作失败");
    }

    @ApiOperation(value = "根据表类别编号删除表")
    @RequestMapping(value = "/delSysTableType", method = RequestMethod.POST)
    public ResponseEntity deleteSysEntityGroupDef(
            @RequestParam String id) throws BusinessException {
        SysTableModelInfoManager.removeAllCache();
        boolean isok = sysTableTypeService.delSysTableType(id);
        if (isok) {
            return ok("删除成功");
        }
        return fail("删除异常");
    }

    @ApiOperation(value = "修改表类别信息")
    @RequestMapping(value = "/updateSysTableType", method = RequestMethod.POST)
    public ResponseEntity updateSysEntityAttrDef(
            @RequestBody SysTableTypeModifyModel sysTableTypeModifyModel) throws BusinessException {
        SysTableModelInfoManager.removeAllCache();
        boolean isok = sysTableTypeService.upSysTableType(sysTableTypeModifyModel);
        if (isok) {
            return ok("操作成功");
        }
        return fail("操作失败");
    }

    @ApiOperation("显示所有表类别数据")
    @RequestMapping(value = "/findAllSysTableType", method = RequestMethod.POST)
    public ResponseEntity findSysEntityDefList() throws BusinessException {
        List<SysTableTypeModel> sysTableTypeModels = sysTableTypeService.getAllSysTableType();
        return ok(sysTableTypeModels);
    }


}
