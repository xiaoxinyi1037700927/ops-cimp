package com.sinosoft.ops.cimp.controller.sys.table;

import com.sinosoft.ops.cimp.config.swagger2.SystemApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.table.SysTableFieldService;
import com.sinosoft.ops.cimp.vo.from.table.SysTableFieldAddModel;
import com.sinosoft.ops.cimp.vo.from.table.SysTableFieldModifyModel;
import com.sinosoft.ops.cimp.vo.from.table.SysTableFieldSearchModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.ok;

@SystemApiGroup
@Api(description = "系统表下字段操纵-->对应-->对应干部信息，单位类")
@RestController
@RequestMapping(value = "/sys/table/field")
public class SysTableFieldController extends BaseController {

    private final SysTableFieldService sysTableFieldService;

    @Autowired
    public SysTableFieldController(SysTableFieldService sysTableFieldService) {
        this.sysTableFieldService = sysTableFieldService;
    }

    @ApiOperation(value = "创建字段信息")
    @RequestMapping(value = "/addSysEntityAttrDef", method = RequestMethod.POST)
    public ResponseEntity saveSysEntityAttrDef(
            @RequestBody SysTableFieldAddModel sysTableFieldAddModel) throws BusinessException {
        boolean isok = sysTableFieldService.addSysTableField(sysTableFieldAddModel);
        if (isok) {
            return ok("操作成功");
        }
        return null ;//fail("操作失败");
    }

    @ApiOperation(value = "根据属性编号删除")
    @RequestMapping(value = "/delSysEntityAttrDefById", method = RequestMethod.POST)
    public ResponseEntity delById(
            @RequestParam(value = "id") String id) throws BusinessException {
        boolean isok = sysTableFieldService.delSysTableField(id);
        if (isok) {
            return ok("删除成功");
        }
          return null;///fail("删除异常");
    }

    @ApiOperation(value = "修改属性信息")
    @RequestMapping(value = "/updateSysEntityAttrDef", method = RequestMethod.POST)
    public ResponseEntity updateSysEntityAttrDef(
            @RequestBody SysTableFieldModifyModel sysTableFieldModifyModel) throws BusinessException {
        boolean isok = sysTableFieldService.upSysTableField(sysTableFieldModifyModel);
        return ok(isok);
    }

    @ApiOperation("分页查询获取属性")
    @RequestMapping(value = "/findByGroupIdOrNameCn", method = RequestMethod.POST)
    public ResponseEntity findSysEntityAttrDef(
            @Valid @RequestBody SysTableFieldSearchModel sysTableFieldSearchModel) throws BusinessException {
        PaginationViewModel<SysTableFieldModifyModel> sysEntityAttrDefModels = sysTableFieldService.findBySysTableFieldByPageOrName(sysTableFieldSearchModel);
        return ok(sysEntityAttrDefModels);
    }

    @ApiOperation("查询单个属性信息")
    @RequestMapping(value = "/findAttrDefById", method = RequestMethod.POST)
    public ResponseEntity<SysTableFieldModifyModel> findAttrDefById(@RequestParam("id") String id) throws BusinessException {
        SysTableFieldModifyModel sysEntityAttrDefModel = sysTableFieldService.findById(id);
        if (sysEntityAttrDefModel == null) {
          //  return fail("查询失败");
        }
        return ok(sysEntityAttrDefModel);
    }


}
