package com.sinosoft.ops.cimp.controller.sys.datapermission;


import com.sinosoft.ops.cimp.annotation.RequiresAuthentication;
import com.sinosoft.ops.cimp.annotation.SystemLimitsApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.sys.datapermission.InterfacesService;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.InterfacesAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.InterfacesDeleteModel;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.InterfacesModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.InterfacesSearchModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@SystemLimitsApiGroup
@Api(description = "接口配置")
@RestController
@RequestMapping("/sys/interfaces")
public class InterfacesController extends BaseController {

    private final InterfacesService interfacesService;

    public InterfacesController(InterfacesService interfacesService) {
        this.interfacesService = interfacesService;
    }


    @ApiOperation(value = "接口列表")
    @PostMapping("/list")
    @RequiresAuthentication
    public ResponseEntity listInterfaces(@RequestBody InterfacesSearchModel searchModel) throws BusinessException {
        return ok(interfacesService.listInterfaces(searchModel));
    }


    @ApiOperation(value = "添加接口")
    @PostMapping("/add")
    @RequiresAuthentication
    public ResponseEntity addInterfaces(@RequestBody InterfacesAddModel addModel) throws BusinessException {
        interfacesService.addInterfaces(addModel);
        return ok("添加成功！");
    }

    @ApiOperation(value = "修改接口")
    @PostMapping("/modify")
    @RequiresAuthentication
    public ResponseEntity modifyInterfaces(@RequestBody InterfacesModifyModel modifyModel) throws BusinessException {
        return ok(interfacesService.modifyInterfaces(modifyModel) ? "修改成功！" : "修改失败！");
    }

    @ApiOperation(value = "删除接口")
    @PostMapping("/delete")
    @RequiresAuthentication
    public ResponseEntity deleteInterfaces(@RequestBody InterfacesDeleteModel deleteModel) throws BusinessException {
        interfacesService.deleteInterfaces(deleteModel);
        return ok("删除成功！");
    }

    @ApiOperation(value = "配置类型列表")
    @PostMapping("/configType")
    public ResponseEntity getConfigType() throws BusinessException {
        return ok(interfacesService.getConfigType());
    }
}
