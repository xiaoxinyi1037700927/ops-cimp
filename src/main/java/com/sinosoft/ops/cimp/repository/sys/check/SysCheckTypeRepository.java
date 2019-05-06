package com.sinosoft.ops.cimp.repository.sys.check;

import com.sinosoft.ops.cimp.entity.sys.check.SysCheckType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SysCheckTypeRepository extends JpaRepository<SysCheckType, String>, QuerydslPredicateExecutor<SysCheckType> {
}
