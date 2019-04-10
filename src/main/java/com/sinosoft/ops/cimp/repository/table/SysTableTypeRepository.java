package com.sinosoft.ops.cimp.repository.table;

import com.sinosoft.ops.cimp.entity.sys.table.SysTableType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;


public interface SysTableTypeRepository extends JpaRepository<SysTableType, String>, QuerydslPredicateExecutor<SysTableType> {


}
