package com.sinosoft.ops.cimp.repository.sys.oplog;

import com.sinosoft.ops.cimp.entity.sys.oplog.SysOperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SysOperationLogRepository extends JpaRepository<SysOperationLog, String>, QuerydslPredicateExecutor<SysOperationLog> {
}
