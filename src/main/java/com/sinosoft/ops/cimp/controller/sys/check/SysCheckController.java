package com.sinosoft.ops.cimp.controller.sys.check;

import com.sinosoft.ops.cimp.annotation.SystemApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.sys.check.SysCheckService;
import com.sinosoft.ops.cimp.vo.from.sys.check.SysCheckQueryDataModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SystemApiGroup
@Api(description = "系统查错")
@RestController
@RequestMapping(value = "/sys/check")
public class SysCheckController extends BaseController {


    @Autowired
    private SysCheckService sysCheckService;

    /**
     * 系统查错条件列表
     */
    @ApiOperation(value = "系统查错条件列表")
    @PostMapping("/condition/list")
    public ResponseEntity listSysCheckCondition() throws BusinessException {
        return ok(sysCheckService.listSysCheckCondition());
    }


    /**
     * 获取指定查错条件的分组统计数据
     */
    @ApiOperation(value = "获取指定查错条件的分组统计数据")
    @PostMapping("/query")
    public ResponseEntity queryData(@RequestBody SysCheckQueryDataModel queryModel) throws BusinessException {
        return ok(sysCheckService.queryData(queryModel));
    }

}