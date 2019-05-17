package com.sinosoft.ops.cimp.service.sys.oplog;

import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.sys.oplog.SysOperationLog;
import com.sinosoft.ops.cimp.vo.from.sys.oplog.SysOperationLogSearchVO;

public interface SysOperationLogService {

    /**
     * 保存一条操作日志
     */
    void saveLog(SysOperationLog sysOperationLog);

    /**
     * 分页查询操作日志
     */
    PaginationViewModel<SysOperationLog> findByPage(int pageIndex, int pageSize);

    /**
     * 搜索操作日志
     */
    PaginationViewModel<SysOperationLog> search(SysOperationLogSearchVO sysOperationLogSearchVO);
}
