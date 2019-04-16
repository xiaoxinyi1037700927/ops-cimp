package com.sinosoft.ops.cimp.repository.sys.app;

import com.sinosoft.ops.cimp.entity.sys.app.SysAppTableFieldGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SysAppTableFieldGroupRepository extends JpaRepository<SysAppTableFieldGroup, String>, QuerydslPredicateExecutor<SysAppTableFieldGroup> {
}
