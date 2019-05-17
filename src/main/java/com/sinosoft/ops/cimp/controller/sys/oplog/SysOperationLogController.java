package com.sinosoft.ops.cimp.controller.sys.oplog;

import com.sinosoft.ops.cimp.annotation.SystemLimitsApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.sys.oplog.SysOperationLog;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.sys.oplog.SysOperationLogService;
import com.sinosoft.ops.cimp.vo.from.sys.oplog.SysOperationLogSearchVO;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SystemLimitsApiGroup
@Api(description = "日志管理接口")
@RestController
@RequestMapping("/sys/oplog")
@SuppressWarnings("unchecked")
public class SysOperationLogController extends BaseController {

    private final SysOperationLogService sysOperationLogService;

    @Autowired
    public SysOperationLogController(SysOperationLogService sysOperationLogService) {
        this.sysOperationLogService = sysOperationLogService;
    }

    @GetMapping("/list")
    public ResponseEntity<PaginationViewModel<SysOperationLog>> list(
            @RequestParam("pageIndex") String pageIndex,
            @RequestParam("pageSize") String pageSize) throws BusinessException {

        int pageIndexInt = 0;
        int pageSizeInt = 0;
        if (StringUtils.isNotEmpty(pageIndex)) {
            try {
                pageIndexInt = Integer.parseInt(pageIndex);
            } catch (Exception e) {
                pageIndexInt = 0;
            }
        }
        if (StringUtils.isNotEmpty(pageSize)) {
            try {
                pageSizeInt = Integer.parseInt(pageSize);
            } catch (Exception e) {
                pageSizeInt = 0;
            }
        }
        PaginationViewModel<SysOperationLog> sysLogPage = sysOperationLogService.findByPage(pageIndexInt, pageSizeInt);

        return ok(sysLogPage);
    }


    public ResponseEntity<PaginationViewModel<SysOperationLog>> search(
            @RequestBody SysOperationLogSearchVO sysOperationLogSearchVO
    ) throws BusinessException {
        PaginationViewModel<SysOperationLog> sysOperationLog = sysOperationLogService.search(sysOperationLogSearchVO);
        return ok(sysOperationLog);
    }
}
