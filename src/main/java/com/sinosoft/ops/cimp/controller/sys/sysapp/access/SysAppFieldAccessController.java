package com.sinosoft.ops.cimp.controller.sys.sysapp.access;

import com.sinosoft.ops.cimp.annotation.SystemApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.sys.sysapp.acess.SysAppFieldAccessService;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.access.SysAppFieldAccessAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.access.SysAppFieldAccessDeleteModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.access.SysAppFieldAccessModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.access.SysAppFieldAccessSearchModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SystemApiGroup
@Api(description = "app中角色对表字段的访问权限")
@RestController
@RequestMapping(value = "/sys/sysapp/fieldAccess")
public class SysAppFieldAccessController extends BaseController {

    private final SysAppFieldAccessService fieldAccessService;

    @Autowired
    public SysAppFieldAccessController(SysAppFieldAccessService fieldAccessService) {
        this.fieldAccessService = fieldAccessService;
    }

    /**
     * 查询对表字段的访问权限列表
     */
    @ApiOperation(value = "查询对表字段的访问权限列表")
    @PostMapping("/list")
    public ResponseEntity listFieldAccess(@RequestBody SysAppFieldAccessSearchModel searchModel) throws BusinessException {
        return ok(fieldAccessService.listFieldAccess(searchModel));
    }

    /**
     * 新增对表字段的访问权限
     */
    @ApiOperation(value = "新增对表字段的访问权限")
    @PostMapping("/add")
    public ResponseEntity addFieldAccess(@RequestBody SysAppFieldAccessAddModel addModel) throws BusinessException {
        fieldAccessService.addFieldAccess(addModel);
        return ok("新增成功！");
    }

    /**
     * 删除对表字段的访问权限
     */
    @ApiOperation(value = "删除对表字段的访问权限")
    @PostMapping("/delete")
    public ResponseEntity deleteFieldAccess(@RequestBody SysAppFieldAccessDeleteModel deleteModel) throws BusinessException {
        fieldAccessService.deleteFieldAccess(deleteModel.getIds());
        return ok("删除成功！");
    }

    /**
     * 修改对表字段的访问权限
     */
    @ApiOperation(value = "修改对表字段的访问权限")
    @PostMapping("/modify")
    public ResponseEntity modifyFieldAccess(@RequestBody SysAppFieldAccessModifyModel modifyModel) throws BusinessException {
        return fieldAccessService.modifyFieldAccess(modifyModel) ? ok("修改成功！") : fail("修改失败！");
    }

}
