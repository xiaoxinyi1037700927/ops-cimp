package com.sinosoft.ops.cimp.repository.sys.check;

import com.sinosoft.ops.cimp.entity.sys.check.SysCheckCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SysCheckConditionRepository extends JpaRepository<SysCheckCondition, String>, QuerydslPredicateExecutor<SysCheckCondition> {
}
