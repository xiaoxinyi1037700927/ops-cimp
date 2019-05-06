package com.sinosoft.ops.cimp.controller.sys.check;

import com.sinosoft.ops.cimp.annotation.SystemApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.schedule.tasks.DataStatisticsTask;
import com.sinosoft.ops.cimp.service.sys.check.SysCheckService;
import com.sinosoft.ops.cimp.vo.from.sys.check.*;
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
    private final SysCheckService sysCheckService;
    private final DataStatisticsTask task;

    @Autowired
    public SysCheckController(SysCheckService sysCheckService, DataStatisticsTask task) {
        this.sysCheckService = sysCheckService;
        this.task = task;
    }


    /**
     * 系统查错类型列表
     */
    @ApiOperation(value = "系统查错类型列表")
    @PostMapping("/type/list")
    public ResponseEntity listSysCheckType() throws BusinessException {
        return ok(sysCheckService.listSysCheckType());
    }


    /**
     * 系统查错条件列表
     */
    @ApiOperation(value = "系统查错条件列表")
    @PostMapping("/condition/list")
    public ResponseEntity listSysCheckCondition(@RequestBody SysCheckConditionSearchModel searchModel) throws BusinessException {
        return ok(sysCheckService.listSysCheckCondition(searchModel));
    }

    /**
     * 添加查错条件
     */
    @ApiOperation(value = "添加查错条件")
    @PostMapping("/condition/add")
    public ResponseEntity addSysCheckCondition(@RequestBody SysCheckConditionAddModel addModel) throws BusinessException {
        sysCheckService.addSysCheckCondition(addModel);
        return ok("新增成功！");
    }

    /**
     * 删除查错条件
     */
    @ApiOperation(value = "删除查错条件")
    @PostMapping("/condition/delete")
    public ResponseEntity deleteSysCheckCondition(@RequestBody SysCheckConditionDeleteModel deleteModel) throws BusinessException {
        sysCheckService.deleteSysCheckCondition(deleteModel.getIds());
        return ok("删除成功！");
    }

    /**
     * 修改查错条件
     */
    @ApiOperation(value = "修改查错条件")
    @PostMapping("/condition/modify")
    public ResponseEntity modifySysCheckCondition(@RequestBody SysCheckConditionModifyModel modifyModel) throws BusinessException {
        return sysCheckService.modifySysCheckCondition(modifyModel) ? ok("修改成功！") : fail("修改失败！");
    }


    /**
     * 获取指定查错条件的分组统计数据
     */
    @ApiOperation(value = "获取指定查错条件的分组统计数据")
    @PostMapping("/query")
    public ResponseEntity queryData(@RequestBody SysCheckQueryDataModel queryModel) throws BusinessException {
        return ok(sysCheckService.queryData(queryModel));
    }

    /**
     * 重新统计数据
     */
    @ApiOperation(value = "重新统计数据")
    @PostMapping("/refresh")
    public ResponseEntity refresh() throws BusinessException {
        return ok(task.exec() ? "执行成功" : "执行失败");
    }

}
