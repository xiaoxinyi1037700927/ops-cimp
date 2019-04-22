package com.sinosoft.ops.cimp.controller.sys.sysapp;

import com.sinosoft.ops.cimp.annotation.SystemApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.sys.sysapp.SysAppTableSetService;
import com.sinosoft.ops.cimp.util.CachePackage.SysTableModelInfoManager;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysAppTableSet.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SystemApiGroup
@Api(description = "系统应用表集合操作")
@RestController
@RequestMapping(value = "/sys/sysapp/tableSet")
public class SysAppTableSetController extends BaseController {
    @Autowired
    private SysAppTableSetService tableSetService;

    /**
     * 查询系统应用表集合列表
     */
    @ApiOperation(value = "查询系统应用表集合列表")
    @PostMapping("/list")
    public ResponseEntity listTableSet(@RequestBody SysAppTableSetSearchModel searchModel) throws BusinessException {
        return ok(tableSetService.listTableSet(searchModel));
    }

    /**
     * 新增系统应用表集合
     */
    @ApiOperation(value = "新增系统应用表集合")
    @PostMapping("/add")
    public ResponseEntity addTableSet(@RequestBody SysAppTableSetAddModel addModel) throws BusinessException {
        SysTableModelInfoManager.removeAllCache();
        tableSetService.addTableSet(addModel);
        return ok("新增成功！");
    }

    /**
     * 删除系统应用表集合
     */
    @ApiOperation(value = "删除系统应用表集合")
    @PostMapping("/delete")
    public ResponseEntity deleteTableSet(@RequestBody SysAppTableSetDeleteModel deleteModel) throws BusinessException {
        SysTableModelInfoManager.removeAllCache();
        tableSetService.deleteTableSet(deleteModel.getIds());
        return ok("删除成功！");
    }

    /**
     * 修改系统应用表集合
     */
    @ApiOperation(value = "修改系统应用表集合")
    @PostMapping("/modify")
    public ResponseEntity modifyTableSet(@RequestBody SysAppTableSetModifyModel modifyModel) throws BusinessException {
        SysTableModelInfoManager.removeAllCache();
        return tableSetService.modifyTableSet(modifyModel) ? ok("修改成功！") : fail("修改失败！");
    }

    /**
     * 系统表分类选择列表
     */
    @ApiOperation(value = "系统表分类选择列表")
    @PostMapping("/sysTableType/list")
    public ResponseEntity listSysTableType() throws BusinessException {
        return ok(tableSetService.listSysTableType());
    }

    /**
     * 系统表选择列表
     */
    @ApiOperation(value = "系统表选择列表")
    @PostMapping("/sysTable/list")
    public ResponseEntity listSysTable(@RequestBody SysAppTableSearchModel searchModel) throws BusinessException {
        return ok(tableSetService.listSysTable(searchModel));
    }

    /**
     * 交换排序
     */
    @ApiOperation(value = "交换排序")
    @PostMapping("/sort/swap")
    public ResponseEntity swapSort(@RequestBody SysAppTableSetSortModel sortModel) throws BusinessException {
        SysTableModelInfoManager.removeAllCache();
        return tableSetService.swapSort(sortModel.getIds()) ? ok("修改成功！") : fail("修改失败！");
    }
}
