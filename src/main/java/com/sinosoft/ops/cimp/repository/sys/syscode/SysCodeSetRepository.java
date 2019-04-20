package com.sinosoft.ops.cimp.repository.sys.syscode;


import com.sinosoft.ops.cimp.entity.sys.syscode.SysCodeSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SysCodeSetRepository extends JpaRepository<SysCodeSet, Integer> , QuerydslPredicateExecutor<SysCodeSet> {

    SysCodeSet getByName(String name);
}
