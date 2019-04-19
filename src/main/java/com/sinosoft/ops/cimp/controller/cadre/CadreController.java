package com.sinosoft.ops.cimp.controller.cadre;

import com.sinosoft.ops.cimp.annotation.BusinessApiGroup;
import com.sinosoft.ops.cimp.annotation.RequiresAuthentication;
import com.sinosoft.ops.cimp.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@BusinessApiGroup
@Api(description = "干部列表接口")
@RestController
@RequestMapping(value = "/cadre")
public class CadreController extends BaseController {

    @ApiOperation(value = "查询干部列表")
    @GetMapping(value = "/list")
    @RequiresAuthentication
    public ResponseEntity listCadre() {

        return null;
    }

}
