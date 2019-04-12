package com.sinosoft.ops.cimp.controller.sys.table;

import com.sinosoft.ops.cimp.config.swagger2.SystemApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.table.SysTableService;
import com.sinosoft.ops.cimp.vo.from.table.SysTableAddModel;
import com.sinosoft.ops.cimp.vo.from.table.SysTableModifyModel;
import com.sinosoft.ops.cimp.vo.from.table.SysTableSearchModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@SystemApiGroup
@Api(description = "系统表操纵")
@RestController
@RequestMapping(value = "/sys/table")
public class SysTableController extends BaseController {

    private final SysTableService sysTableService;

    @Autowired
    public SysTableController(SysTableService sysTableService) {
        this.sysTableService = sysTableService;
    }

    @ApiOperation("新增系统表")
    @RequestMapping(value = "/addSysTable", method = RequestMethod.POST)
    public ResponseEntity saveSysEntityDef(
            @Valid @RequestBody SysTableAddModel sysTableAddModel) throws BusinessException {
        boolean isok = sysTableService.addSysTable(sysTableAddModel);
        if (isok) return ok("操作成功");
        return fail("操作失败");
    }

    @ApiOperation("根据Id删除系统表")
    @RequestMapping(value = "/delSysTableById", method = RequestMethod.POST)
    public ResponseEntity deleteSysEntityDef(
            @RequestParam("id") String id) throws BusinessException {
        boolean isok = sysTableService.delSysTable(id);
        if (isok) {
            return ok("删除成功");
        }
        return fail("删除异常");
    }

    @ApiOperation("修改系统表")
    @RequestMapping(value = "/updateSysTable", method = RequestMethod.POST)
    public ResponseEntity updateSysEntityDef(
            @RequestBody SysTableModifyModel sysTableModifyModel) throws BusinessException {
        boolean isok = sysTableService.upSysTable(sysTableModifyModel);
        if (isok) {
            return ok("操作成功");
        }
        return fail("操作失败");
    }

    @ApiOperation("分页显示系统表数据")
    @RequestMapping(value = "/findSysTableByPages", method = RequestMethod.POST)
    public ResponseEntity findSysEntityDefList(
            @RequestBody SysTableSearchModel sysTableSearchModel) throws BusinessException {
        PaginationViewModel<SysTableModifyModel> sysEntityDefs = sysTableService.getSysTableByPage(sysTableSearchModel);
        return ok(sysEntityDefs);
    }

    @ApiOperation("数据库表操作")
    @RequestMapping(value = "/operatingDbTable", method = RequestMethod.POST)
    public ResponseEntity operatingDbTable(
            @RequestParam("sysTableId") String sysTableId ) throws BusinessException {
        boolean isok = sysTableService.operatingDbTable(sysTableId);
        if (isok) {
            return ok("提交成功");
        }
        return fail("提交失败");
    }


}
