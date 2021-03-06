package com.sinosoft.ops.cimp.controller.sys.systable;

import com.sinosoft.ops.cimp.annotation.SystemApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.sys.systable.SysTableFieldService;
import com.sinosoft.ops.cimp.util.CachePackage.SysTableModelInfoManager;
import com.sinosoft.ops.cimp.vo.from.sys.systable.SysTableFieldAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.systable.SysTableFieldModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.systable.SysTableFieldSearchModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@SystemApiGroup
@Api(description = "系统表下字段操纵")
@RestController
@RequestMapping(value = "/sys/systable/field")
public class SysTableFieldController extends BaseController {

    private final SysTableFieldService sysTableFieldService;

    @Autowired
    public SysTableFieldController(SysTableFieldService sysTableFieldService) {
        this.sysTableFieldService = sysTableFieldService;
    }

    @ApiOperation(value = "创建字段信息")
    @RequestMapping(value = "/addSysTableField", method = RequestMethod.POST)
    public ResponseEntity saveSysEntityAttrDef(
            @RequestBody SysTableFieldAddModel sysTableFieldAddModel) throws BusinessException {
        SysTableModelInfoManager.removeAllCache();
        boolean isok = sysTableFieldService.addSysTableField(sysTableFieldAddModel);
        if (isok) {
            return ok("操作成功");
        }
        return fail("操作失败");
    }

    @ApiOperation(value = "根据字段编号删除")
    @RequestMapping(value = "/delSysTableFieldById", method = RequestMethod.POST)
    public ResponseEntity delById(
            @RequestParam(value = "id") String id) throws BusinessException {
        SysTableModelInfoManager.removeAllCache();
        boolean isok = sysTableFieldService.delSysTableField(id);
        if (isok) {
            return ok("删除成功");
        }
        return fail("删除异常");
    }

    @ApiOperation(value = "修改字段信息")
    @RequestMapping(value = "/updateSysTableField", method = RequestMethod.POST)
    public ResponseEntity updateSysEntityAttrDef(
            @RequestBody SysTableFieldModifyModel sysTableFieldModifyModel) throws BusinessException {
        SysTableModelInfoManager.removeAllCache();
        boolean isok = sysTableFieldService.upSysTableField(sysTableFieldModifyModel);
        return ok(isok);
    }

    @ApiOperation("分页查询获取字段信息")
    @RequestMapping(value = "/findSysTableFieldBySysTableIdOrNameCn", method = RequestMethod.POST)
    public ResponseEntity findSysEntityAttrDef(
            @Valid @RequestBody SysTableFieldSearchModel sysTableFieldSearchModel) throws BusinessException {
        PaginationViewModel<SysTableFieldModifyModel> sysEntityAttrDefModels = sysTableFieldService.findBySysTableFieldByPageOrName(sysTableFieldSearchModel);
        return ok(sysEntityAttrDefModels);
    }

    @ApiOperation("查询单个字段信息")
    @RequestMapping(value = "/findSysTableDefById", method = RequestMethod.POST)
    public ResponseEntity findAttrDefById(@RequestParam("id") String id) throws BusinessException {
        SysTableFieldModifyModel sysEntityAttrDefModel = sysTableFieldService.findById(id);
        if (sysEntityAttrDefModel == null) {
            return fail("查询失败");
        }
        return ok(sysEntityAttrDefModel);
    }


}
