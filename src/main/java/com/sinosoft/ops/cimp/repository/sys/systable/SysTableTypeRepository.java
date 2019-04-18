package com.sinosoft.ops.cimp.repository.sys.systable;

import com.sinosoft.ops.cimp.entity.sys.systable.SysTableType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;


public interface SysTableTypeRepository extends JpaRepository<SysTableType, String>, QuerydslPredicateExecutor<SysTableType> {


}
