package com.sinosoft.ops.cimp.repository.sys.tag;

import com.sinosoft.ops.cimp.entity.sys.tag.SysTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SysTagRepository extends JpaRepository<SysTag, String>, QuerydslPredicateExecutor<SysTag> {
}
