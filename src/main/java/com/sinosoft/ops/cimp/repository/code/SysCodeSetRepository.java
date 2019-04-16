package com.sinosoft.ops.cimp.repository.code;


import com.sinosoft.ops.cimp.entity.sys.code.SysCodeSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SysCodeSetRepository extends JpaRepository<SysCodeSet, Integer> , QuerydslPredicateExecutor<SysCodeSet> {

}
