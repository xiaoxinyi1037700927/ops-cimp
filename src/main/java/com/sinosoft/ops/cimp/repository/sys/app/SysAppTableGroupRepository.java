package com.sinosoft.ops.cimp.repository.sys.app;

import com.sinosoft.ops.cimp.entity.sys.app.SysAppTableGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SysAppTableGroupRepository extends JpaRepository<SysAppTableGroup, String>, QuerydslPredicateExecutor<SysAppTableGroup> {
}
