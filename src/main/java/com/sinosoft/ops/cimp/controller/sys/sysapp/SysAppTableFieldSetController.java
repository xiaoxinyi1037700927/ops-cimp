package com.sinosoft.ops.cimp.controller.sys.sysapp;

import com.sinosoft.ops.cimp.annotation.SystemApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.sys.sysapp.SysAppTableFieldSetService;
import com.sinosoft.ops.cimp.util.CachePackage.SysTableModelInfoManager;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysAppTableFieldSet.*;
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
@RequestMapping(value = "/sys/sysapp/fieldSet")
public class SysAppTableFieldSetController extends BaseController {
    private final SysAppTableFieldSetService fieldSetService;

    @Autowired
    public SysAppTableFieldSetController(SysAppTableFieldSetService fieldSetService) {
        this.fieldSetService = fieldSetService;
    }

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
        SysTableModelInfoManager.removeAllCache();
        fieldSetService.addFieldSet(addModel);
        return ok("新增成功！");
    }

    /**
     * 删除系统应用表字段集合
     */
    @ApiOperation(value = "删除系统应用表字段集合")
    @PostMapping("/delete")
    public ResponseEntity deleteFieldSet(@RequestBody SysAppTableFieldSetDeleteModel deleteModel) throws BusinessException {
        SysTableModelInfoManager.removeAllCache();
        fieldSetService.deleteFieldSet(deleteModel.getIds());
        return ok("删除成功！");
    }

    /**
     * 修改系统应用表字段集合
     */
    @ApiOperation(value = "修改系统应用表字段集合")
    @PostMapping("/modify")
    public ResponseEntity modifyFieldSet(@RequestBody SysAppTableFieldSetModifyModel modifyModel) throws BusinessException {
        SysTableModelInfoManager.removeAllCache();
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
     * 修改排序
     */
    @ApiOperation(value = "修改排序")
    @PostMapping("/sort/modify")
    public ResponseEntity modifySort(@RequestBody SysAppTableFieldSetSortModel sortModel) throws BusinessException {
        SysTableModelInfoManager.removeAllCache();
        return fieldSetService.modifySort(sortModel) ? ok("修改成功！") : fail("修改失败！");
    }
}
