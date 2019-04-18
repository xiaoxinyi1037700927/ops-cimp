package com.sinosoft.ops.cimp.repository.sys.sysapp;

import com.sinosoft.ops.cimp.entity.sys.sysapp.SysAppTableGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SysAppTableGroupRepository extends JpaRepository<SysAppTableGroup, String>, QuerydslPredicateExecutor<SysAppTableGroup> {
}
