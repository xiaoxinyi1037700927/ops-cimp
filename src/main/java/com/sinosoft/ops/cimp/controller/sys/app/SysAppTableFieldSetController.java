package com.sinosoft.ops.cimp.controller.sys.app;

import com.sinosoft.ops.cimp.config.swagger2.SystemApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.sys.app.SysAppTableFieldSetService;
import com.sinosoft.ops.cimp.vo.from.sys.app.sysAppTableFieldSet.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SystemApiGroup
@Api(description = "系统应用表字段集合操作")
@RestController
@RequestMapping(value = "/sys/app/fieldSet")
public class SysAppTableFieldSetController extends BaseController {
    @Autowired
    private SysAppTableFieldSetService fieldSetService;

    /**
     * 查询系统应用表字段集合列表
     */
    @ApiOperation(value = "查询系统应用表字段集合列表")
    @PostMapping("/list")
    public ResponseEntity listFieldSet(@RequestBody SysAppTableFieldSetSearchModel searchModel) throws BusinessException {
        return ok(fieldSetService.listFieldSet(searchModel));
    }

    /**
     * 新增系统应用表字段集合
     */
    @ApiOperation(value = "新增系统应用表字段集合")
    @PostMapping("/add")
    public ResponseEntity addFieldSet(@RequestBody SysAppTableFieldSetAddModel addModel) throws BusinessException {
        fieldSetService.addFieldSet(addModel);
        return ok("新增成功！");
    }

    /**
     * 删除系统应用表字段集合
     */
    @ApiOperation(value = "删除系统应用表字段集合")
    @PostMapping("/delete")
    public ResponseEntity deleteFieldSet(@RequestBody SysAppTableFieldSetDeleteModel deleteModel) throws BusinessException {
        fieldSetService.deleteFieldSet(deleteModel.getIds());
        return ok("删除成功！");
    }

    /**
     * 修改系统应用表字段集合
     */
    @ApiOperation(value = "修改系统应用表字段集合")
    @PostMapping("/modify")
    public ResponseEntity modifyFieldSet(@RequestBody SysAppTableFieldSetModifyModel modifyModel) throws BusinessException {
        return fieldSetService.modifyFieldSet(modifyModel) ? ok("修改成功！") : fail("修改失败！");
    }

    /**
     * 系统表字段选择列表
     */
    @ApiOperation(value = "系统表字段选择列表")
    @PostMapping("/sysTableField/list")
    public ResponseEntity listSysTableField(@RequestBody SysAppTableFieldSearchModel searchModel) throws BusinessException {
        return ok(fieldSetService.listSysTableField(searchModel));
    }

    /**
     * 交换排序
     */
    @ApiOperation(value = "交换排序")
    @PostMapping("/sort/swap")
    public ResponseEntity swapSort(@RequestBody SysAppTableFieldSetSortModel sortModel) throws BusinessException {
        return fieldSetService.swapSort(sortModel.getIds()) ? ok("修改成功！") : fail("修改失败！");
    }
}
