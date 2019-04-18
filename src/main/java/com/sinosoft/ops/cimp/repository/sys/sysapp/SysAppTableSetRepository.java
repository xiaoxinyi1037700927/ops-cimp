package com.sinosoft.ops.cimp.repository.sys.sysapp;

import com.sinosoft.ops.cimp.entity.sys.sysapp.SysAppTableSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SysAppTableSetRepository extends JpaRepository<SysAppTableSet, String>, QuerydslPredicateExecutor<SysAppTableSet> {
}
