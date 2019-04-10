package com.sinosoft.ops.cimp.controller.sys.table;


import com.sinosoft.ops.cimp.config.swagger2.SystemApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.table.SysTableTypeService;
import com.sinosoft.ops.cimp.vo.from.table.SysTableTypeAddModel;
import com.sinosoft.ops.cimp.vo.from.table.SysTableTypeModifyModel;
import com.sinosoft.ops.cimp.vo.from.table.SysTableTypeSearchModel;
import com.sinosoft.ops.cimp.vo.to.table.SysTableModel;
import com.sinosoft.ops.cimp.vo.to.table.SysTableTypeModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@SystemApiGroup
@Api(description = "系统表类别操纵-->对应-->对应干部信息，单位类")
@RestController
@RequestMapping(value = "/sys/table/type")
public class SysTableTypeController extends BaseController{

    private final SysTableTypeService sysTableTypeService;

    @Autowired
    public SysTableTypeController(SysTableTypeService sysTableTypeService) {
        this.sysTableTypeService = sysTableTypeService;
    }

    @ApiOperation(value = "新增实体中组信息")
    @RequestMapping(value = "/addSysEntityGroupDef", method = RequestMethod.POST)
    public ResponseEntity saveSysEntityGroupDef(
            @RequestBody SysTableTypeAddModel sysTableTypeAddModel) throws BusinessException {
        boolean isok = sysTableTypeService.addSysTableType(sysTableTypeAddModel);
        if (isok) {
            return ok("操作成功");
        }
        return fail("操作失败");
    }

    @ApiOperation(value = "根据组编号删除表")
    @RequestMapping(value = "/delSysEntityGroupDef", method = RequestMethod.POST)
    public ResponseEntity deleteSysEntityGroupDef(
            @RequestParam String id) throws BusinessException {
        boolean isok = sysTableTypeService.delSysTableType(id);
        if (isok) {
            return ok("删除成功");
        }
        return fail("删除异常");
    }

    @ApiOperation(value = "修改组信息")
    @RequestMapping(value = "/updateSysEntityGroupDef", method = RequestMethod.POST)
    public ResponseEntity updateSysEntityAttrDef(
            @RequestBody SysTableTypeModifyModel sysTableTypeModifyModel) throws BusinessException {
        boolean isok = sysTableTypeService.upSysTableType(sysTableTypeModifyModel);
        if (isok) {
            return ok("操作成功");
        }
        return fail("操作失败");
    }

    @ApiOperation("显示所有数据")
    @RequestMapping(value = "/findAllEntity", method = RequestMethod.POST)
    public ResponseEntity findSysEntityDefList() throws BusinessException {
        List<SysTableTypeModel> sysTableTypeModels = sysTableTypeService.getAllSysTableType();
        return ok(sysTableTypeModels);
    }




}
