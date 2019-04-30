package com.sinosoft.ops.cimp.repository.sys.tag;

import com.sinosoft.ops.cimp.entity.sys.tag.SysTagCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SysTagCategoryRepository extends JpaRepository<SysTagCategory, String>, QuerydslPredicateExecutor<SysTagCategory> {
}
