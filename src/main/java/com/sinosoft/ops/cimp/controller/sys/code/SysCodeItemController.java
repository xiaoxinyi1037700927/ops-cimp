package com.sinosoft.ops.cimp.controller.sys.code;

import com.sinosoft.ops.cimp.config.annotation.SystemApiGroup;
import com.sinosoft.ops.cimp.constant.SysCodeSetTypeEnum;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.code.SysCodeItemService;
import com.sinosoft.ops.cimp.vo.from.code.SysCodeItemAddModel;
import com.sinosoft.ops.cimp.vo.from.code.SysCodeItemModifyModel;
import com.sinosoft.ops.cimp.vo.from.code.SysCodeItemPageModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@SystemApiGroup
@Api(description = "代码项操作")
@RestController
@RequestMapping(value = "/sys/code/item")
public class SysCodeItemController extends BaseController {

    private final SysCodeItemService sysCodeItemService;

    @Autowired
    public SysCodeItemController(SysCodeItemService sysCodeItemService) {
        this.sysCodeItemService = sysCodeItemService;
    }


    @ApiOperation(value = "删除代码项")
    @RequestMapping(value = "/delSysCodeItem", method = RequestMethod.POST)
    public ResponseEntity delSysCodeSet(@RequestParam("id") Integer id) throws BusinessException {
        boolean isok = sysCodeItemService.delSysCodeItemById(id);
        if (isok) {
            return ok("删除成功");
        }
        return fail("删除失败");
    }

    @ApiOperation("代码项修改")
    @RequestMapping(value = "/upSysCodeItem", method = RequestMethod.POST)
    public ResponseEntity upSysCodeSet(
            @Valid @RequestBody SysCodeItemModifyModel sysCodeItemModifyModel) throws BusinessException {
        boolean isok = sysCodeItemService.upSysCodeItem(sysCodeItemModifyModel);
        if (isok) {
            return ok("修改成功");
        }
        return fail("修改失败");
    }

    @ApiOperation("添加代码项")
    @RequestMapping(value = "/saveSysCodeItem", method = RequestMethod.POST)
    public ResponseEntity saveSysCodeSet(
            @Valid @RequestBody SysCodeItemAddModel sysCodeItemAddModel) throws BusinessException {
        boolean isok = sysCodeItemService.saveSysCodeItem(sysCodeItemAddModel);
        if (isok) {
            return ok("添加成功");
        }
        return fail("添加失败");
    }

    @ApiOperation(value = "根据代码集编号分页获取对应代码项")
    @RequestMapping(value = "/getSysCodeItemPageById", method = RequestMethod.POST)
    public ResponseEntity getSysCodeItemPageById(
            @Valid @RequestBody SysCodeItemPageModel sysCodeItemPageModel) throws BusinessException {
        PaginationViewModel<SysCodeItemModifyModel> sysCodeItemModifyModel = sysCodeItemService.getSysCodeItemBySearchModel(sysCodeItemPageModel);
        return ok(sysCodeItemModifyModel);
    }


    @ApiOperation(value = "根据代码项编号获取对应代码项")
    @RequestMapping(value = "/getSysCodeItemById", method = RequestMethod.POST)
    public ResponseEntity getSysCodeItemById(@RequestParam("id") Integer id) throws BusinessException {
        SysCodeItemModifyModel sysCodeItemModifyModel = sysCodeItemService.getSysCodeItemById(id);
        return ok(sysCodeItemModifyModel);
    }

    @ApiOperation(value = "获取代码集类型")
    @RequestMapping(value = "/getSysCodeItemType", method = RequestMethod.POST)
    public ResponseEntity getSysCodeItemType() throws BusinessException {
        Map<Integer, String> dataMap = new HashMap<>();
        dataMap.put(SysCodeSetTypeEnum.GeneralType.getType(), SysCodeSetTypeEnum.GeneralType.getTypeName());
        dataMap.put(SysCodeSetTypeEnum.TreeType.getType(), SysCodeSetTypeEnum.TreeType.getTypeName());
        return ok(dataMap);
    }

}
