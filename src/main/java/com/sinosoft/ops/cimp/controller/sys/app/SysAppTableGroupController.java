package com.sinosoft.ops.cimp.controller.sys.app;

import com.sinosoft.ops.cimp.config.swagger2.SystemApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.sys.app.SysAppTableGroupService;
import com.sinosoft.ops.cimp.vo.from.sys.app.sysAppTableFieldSet.SysAppTableFieldSetSortModel;
import com.sinosoft.ops.cimp.vo.from.sys.app.sysAppTableGroup.*;
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
@Api(description = "系统应用表分组操作")
@RestController
@RequestMapping(value = "/sys/app/tableGroup")
public class SysAppTableGroupController extends BaseController {

    @Autowired
    private SysAppTableGroupService tableGroupService;

    /**
     * 查询系统应用表分组列表
     */
    @ApiOperation(value = "查询系统应用表分组列表")
    @PostMapping("/list")
    public ResponseEntity listTableGroup(@RequestBody SysAppTableGroupSearchModel searchModel) throws BusinessException {
        return ok(tableGroupService.listTableGroup(searchModel));
    }

    /**
     * 新增系统应用表分组
     */
    @ApiOperation(value = "新增系统应用表分组")
    @PostMapping("/add")
    public ResponseEntity addTableGroup(@RequestBody SysAppTableGroupAddModel addModel) throws BusinessException {
        tableGroupService.addTableGroup(addModel);
        return ok("新增成功！");
    }

    /**
     * 删除系统应用表分组
     */
    @ApiOperation(value = "删除系统应用表分组")
    @PostMapping("/delete")
    public ResponseEntity deleteTableGroup(@RequestBody SysAppTableGroupDeleteModel deleteModel) throws BusinessException {
        tableGroupService.deleteTableGroup(deleteModel.getIds());
        return ok("删除成功！");
    }

    /**
     * 修改系统应用表分组
     */
    @ApiOperation(value = "修改系统应用表分组")
    @PostMapping("/modify")
    public ResponseEntity modifyTableGroup(@RequestBody SysAppTableGroupModifyModel modifyModel) throws BusinessException {
        return tableGroupService.modifyTableGroup(modifyModel) ? ok("修改成功！") : fail("修改失败！");
    }

    /**
     * 交换排序
     */
    @ApiOperation(value = "交换排序")
    @PostMapping("/sort/swap")
    public ResponseEntity swapSort(@RequestBody SysAppTableGroupSortModel sortModel) throws BusinessException {
        return tableGroupService.swapSort(sortModel.getIds()) ? ok("修改成功！") : fail("修改失败！");
    }
}
