package com.sinosoft.ops.cimp.repository.sys.app;

import com.sinosoft.ops.cimp.entity.sys.app.SysAppTableFieldSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SysAppTableFieldSetRepository extends JpaRepository<SysAppTableFieldSet, String>, QuerydslPredicateExecutor<SysAppTableFieldSet> {
}
