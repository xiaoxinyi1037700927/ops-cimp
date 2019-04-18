package com.sinosoft.ops.cimp.controller.sys.sysapp;

import com.sinosoft.ops.cimp.cache.CacheManager;
import com.sinosoft.ops.cimp.annotation.SystemApiGroup;
import com.sinosoft.ops.cimp.constant.Constants;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.sys.sysapp.SysAppTableFieldGroupService;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysAppTableFieldGroup.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SystemApiGroup
@Api(description = "系统应用表字段分组操作")
@RestController
@RequestMapping(value = "/sys/sysapp/fieldGroup")
public class SysAppTableFieldGroupController extends BaseController {
    @Autowired
    private SysAppTableFieldGroupService fieldGroupService;

    /**
     * 查询系统应用表字段分组列表
     */
    @ApiOperation(value = "查询系统应用表字段分组列表")
    @PostMapping("/list")
    public ResponseEntity listFieldGroup(@RequestBody SysAppTableFieldGroupSearchModel searchModel) throws BusinessException {
        return ok(fieldGroupService.listFieldGroup(searchModel));
    }

    /**
     * 新增系统应用表字段分组
     */
    @ApiOperation(value = "新增系统应用表字段分组")
    @PostMapping("/add")
    public ResponseEntity addFieldGroup(@RequestBody SysAppTableFieldGroupAddModel addModel) throws BusinessException {
        CacheManager.getInstance().remove(Constants.SYS_TABLE_MODEL_INFO);
        fieldGroupService.addFieldGroup(addModel);
        return ok("新增成功！");
    }

    /**
     * 删除系统应用表字段分组
     */
    @ApiOperation(value = "删除系统应用表字段分组")
    @PostMapping("/delete")
    public ResponseEntity deleteFieldGroup(@RequestBody SysAppTableFieldGroupDeleteModel deleteModel) throws BusinessException {
        CacheManager.getInstance().remove(Constants.SYS_TABLE_MODEL_INFO);
        fieldGroupService.deleteFieldGroup(deleteModel.getIds());
        return ok("删除成功！");
    }

    /**
     * 修改系统应用表字段分组
     */
    @ApiOperation(value = "修改系统应用表字段分组")
    @PostMapping("/modify")
    public ResponseEntity modifyFieldGroup(@RequestBody SysAppTableFieldGroupModifyModel modifyModel) throws BusinessException {
        CacheManager.getInstance().remove(Constants.SYS_TABLE_MODEL_INFO);
        return fieldGroupService.modifyFieldGroup(modifyModel) ? ok("修改成功！") : fail("修改失败！");
    }

    /**
     * 交换排序
     */
    @ApiOperation(value = "交换排序")
    @PostMapping("/sort/swap")
    public ResponseEntity swapSort(@RequestBody SysAppTableFieldGroupSortModel sortModel) throws BusinessException {
        CacheManager.getInstance().remove(Constants.SYS_TABLE_MODEL_INFO);
        return fieldGroupService.swapSort(sortModel.getIds()) ? ok("修改成功！") : fail("修改失败！");
    }

}
