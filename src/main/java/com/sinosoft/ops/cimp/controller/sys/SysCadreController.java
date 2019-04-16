package com.sinosoft.ops.cimp.controller.sys;

import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.exception.SystemException;
import com.sinosoft.ops.cimp.service.SysTableModelInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/cadre")
public class SysCadreController extends BaseController {

    private final SysTableModelInfoService sysTableModelInfoService;

    @Autowired
    public SysCadreController(SysTableModelInfoService sysTableModelInfoService) {
        this.sysTableModelInfoService = sysTableModelInfoService;
    }

    @RequestMapping(value = "/list")
    public ResponseEntity getPageCadre() throws BusinessException, SystemException {

//        sysTableModelInfoService.queryData();
        return null;
    }
}
