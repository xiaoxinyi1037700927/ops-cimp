package com.sinosoft.ops.cimp.repository.sys.sysapp;

import com.sinosoft.ops.cimp.entity.sys.sysapp.SysApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SysAppRepository extends JpaRepository<SysApp, String>, QuerydslPredicateExecutor<SysApp> {
}
