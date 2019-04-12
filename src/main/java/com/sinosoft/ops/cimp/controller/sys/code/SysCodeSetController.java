package com.sinosoft.ops.cimp.controller.sys.code;

import com.sinosoft.ops.cimp.config.swagger2.SystemApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.code.SysCodeSetService;
import com.sinosoft.ops.cimp.vo.from.code.SysCodeItemModifyModel;
import com.sinosoft.ops.cimp.vo.from.code.SysCodeSetAddModel;
import com.sinosoft.ops.cimp.vo.from.code.SysCodeSetModifyModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@SystemApiGroup
@Api(description = "代码集操作")
@RestController
@RequestMapping(value = "/sys/code/set")
public class SysCodeSetController extends BaseController {

    private final SysCodeSetService sysCodeSetService;

    @Autowired
    public SysCodeSetController(SysCodeSetService sysCodeSetService) {
        this.sysCodeSetService = sysCodeSetService;
    }


    @ApiOperation(value = "获取全部代码集")
    @RequestMapping(value = "/getAllSysCodeSet", method = RequestMethod.POST)
    public ResponseEntity getAllSysCodeSet() throws BusinessException {
        List<SysCodeSetModifyModel> sysCodeSetModifyModels = sysCodeSetService.findAllSysCodeSets();
        return ok(sysCodeSetModifyModels);
    }


    @ApiOperation(value = "删除代码集")
    @RequestMapping(value = "/delSysCodeSet", method = RequestMethod.POST)
    public ResponseEntity delSysCodeSet(@RequestParam("id") Integer id) throws BusinessException {
        boolean isok = sysCodeSetService.delSysCodeSetById(id);
        if (isok) {
            return ok("删除成功");
        }
        return fail("删除失败");
    }

    @ApiOperation("代码集修改")
    @RequestMapping(value = "/upSysCodeSet", method = RequestMethod.POST)
    public ResponseEntity upSysCodeSet(
            @Valid @RequestBody SysCodeSetModifyModel sysCodeSetModifyModel) throws BusinessException {
        boolean isok = sysCodeSetService.upSysCodeSet(sysCodeSetModifyModel);
        if (isok) {
            return ok("修改成功");
        }
        return fail("修改失败");
    }

    @ApiOperation("添加代码集")
    @RequestMapping(value = "/saveSysCodeSet", method = RequestMethod.POST)
    public ResponseEntity saveSysCodeSet(
            @Valid @RequestBody SysCodeSetAddModel sysCodeSetAddModel) throws BusinessException {
        boolean isok = sysCodeSetService.saveSysCodeSet(sysCodeSetAddModel);
        if (isok) {
            return ok("添加成功");
        }
        return fail("添加失败");
    }

    @ApiOperation(value = "根据代码项编号获取对应代码项")
    @RequestMapping(value = "/getSysCodeSetById", method = RequestMethod.POST)
    public ResponseEntity getSysCodeSetById(@RequestParam("id") Integer id) throws BusinessException {
        SysCodeSetModifyModel sysCodeSetModifyModel = sysCodeSetService.getSysCodeById(id);
        return ok(sysCodeSetModifyModel);
    }


}
