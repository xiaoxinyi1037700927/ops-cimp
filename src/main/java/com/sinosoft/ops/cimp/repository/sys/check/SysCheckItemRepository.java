package com.sinosoft.ops.cimp.repository.sys.check;

import com.sinosoft.ops.cimp.entity.sys.check.SysCheckItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface SysCheckItemRepository extends JpaRepository<SysCheckItem, String>, QuerydslPredicateExecutor<SysCheckItem> {
    List<SysCheckItem> findBySysCheckConditionId(String conditionId);
}
