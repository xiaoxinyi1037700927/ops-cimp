package com.sinosoft.ops.cimp.controller.sys.datapermission;


import com.sinosoft.ops.cimp.annotation.RequiresAuthentication;
import com.sinosoft.ops.cimp.annotation.SystemLimitsApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.sys.datapermission.InterfaceTypeService;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@SystemLimitsApiGroup
@Api(description = "接口类型")
@RestController
@RequestMapping("/sys/interface/type")
public class InterfaceTypeController extends BaseController {
    
    private final InterfaceTypeService interfaceTypeService;

    public InterfaceTypeController(InterfaceTypeService interfaceTypeService) {
        this.interfaceTypeService = interfaceTypeService;
    }


    @ApiOperation(value = "接口类型列表")
    @PostMapping("/list")
    @RequiresAuthentication
    public ResponseEntity listInterfaceType() throws BusinessException {
        return ok(interfaceTypeService.listInterfaceType());
    }


    @ApiOperation(value = "添加接口类型")
    @PostMapping("/add")
    @RequiresAuthentication
    public ResponseEntity addInterfaceType(@RequestBody InterfaceTypeAddModel addModel) throws BusinessException {
        interfaceTypeService.addInterfaceType(addModel);
        return ok("添加成功！");
    }

    @ApiOperation(value = "修改接口类型")
    @PostMapping("/modify")
    @RequiresAuthentication
    public ResponseEntity modifyInterfaceType(@RequestBody InterfaceTypeModifyModel modifyModel) throws BusinessException {
        return ok(interfaceTypeService.modifyInterfaceType(modifyModel) ? "修改成功！" : "修改失败！");
    }

    @ApiOperation(value = "删除接口类型")
    @PostMapping("/delete")
    @RequiresAuthentication
    public ResponseEntity deleteInterfaceType(@RequestBody InterfaceTypeDeleteModel deleteModel) throws BusinessException {
        interfaceTypeService.deleteInterfaceType(deleteModel);
        return ok("删除成功！");
    }

}
