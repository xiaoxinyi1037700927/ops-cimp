package com.sinosoft.ops.cimp.repository.sys.check;

import com.sinosoft.ops.cimp.entity.sys.check.SysCheckResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SysCheckResultRepository extends JpaRepository<SysCheckResult, String>, QuerydslPredicateExecutor<SysCheckResult> {

    void deleteByEmpId(String empId);
}
