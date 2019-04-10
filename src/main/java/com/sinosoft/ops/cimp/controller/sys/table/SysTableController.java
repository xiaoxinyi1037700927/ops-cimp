package com.sinosoft.ops.cimp.controller.sys.table;

import com.sinosoft.ops.cimp.config.swagger2.SystemApiGroup;
import com.sinosoft.ops.cimp.controller.sys.BaseController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SystemApiGroup
@Api(description = "系统表操纵-->对应-->对应干部信息，单位类")
@RestController
@RequestMapping(value = "/sys/table")
public class SysTableController extends BaseController {

}
