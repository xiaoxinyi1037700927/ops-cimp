package com.sinosoft.ops.cimp.controller.sys.table;

import com.sinosoft.ops.cimp.config.swagger2.SystemApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.table.SysTableService;
import com.sinosoft.ops.cimp.vo.from.table.SysTableAddModel;
import com.sinosoft.ops.cimp.vo.to.table.SysTableModel;
import com.sinosoft.ops.cimp.vo.from.table.SysTableModifyModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@SystemApiGroup
@Api(description = "系统表操纵-->对应-->对应干部信息，单位类")
@RestController
@RequestMapping(value = "/sys/table")
public class SysTableController extends BaseController {

    private final SysTableService sysTableService;

    @Autowired
    public SysTableController(SysTableService sysTableService) {
        this.sysTableService = sysTableService;
    }

    @ApiOperation("新增实体")
    @RequestMapping(value = "/addSysEntityDef", method = RequestMethod.POST)
    public ResponseEntity saveSysEntityDef(
            @Valid @RequestBody SysTableAddModel sysTableAddModel) throws BusinessException {
        boolean isok = sysTableService.addSysTable(sysTableAddModel);
        if (isok) return ok("操作成功");
        return fail("操作失败");
    }

    @ApiOperation("根据Id删除实体")
    @RequestMapping(value = "/delById", method = RequestMethod.POST)
    public ResponseEntity deleteSysEntityDef(
            @RequestParam("id") String id) throws BusinessException {
        boolean isok = sysTableService.delSysTable(id);
        if (isok) {
            return ok("删除成功");
        }
        return fail("删除异常");
    }

    @ApiOperation("修改实体")
    @RequestMapping(value = "/updateSysEntityDef", method = RequestMethod.POST)
    public ResponseEntity updateSysEntityDef(
            @RequestBody SysTableModifyModel sysTableModifyModel) throws BusinessException {
        boolean isok = sysTableService.upSysTable(sysTableModifyModel);
        if (isok) {
            return ok("操作成功");
        }
        return fail("操作失败");
    }

    @ApiOperation("显示所有数据")
    @RequestMapping(value = "/findAllEntity", method = RequestMethod.POST)
    public ResponseEntity findSysEntityDefList() throws BusinessException {
        List<SysTableModel> sysEntityDefs = sysTableService.findSysTableModels();
        return ok(sysEntityDefs);
    }

}
