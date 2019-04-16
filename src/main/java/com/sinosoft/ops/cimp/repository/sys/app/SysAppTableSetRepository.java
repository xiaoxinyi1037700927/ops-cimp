package com.sinosoft.ops.cimp.repository.sys.app;

import com.sinosoft.ops.cimp.entity.sys.app.SysAppTableSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SysAppTableSetRepository extends JpaRepository<SysAppTableSet, String>, QuerydslPredicateExecutor<SysAppTableSet> {
}
