package com.sinosoft.ops.cimp.repository.sys.systable;

import com.sinosoft.ops.cimp.entity.sys.systable.SysTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;


public interface SysTableRepository extends JpaRepository<SysTable, String>, QuerydslPredicateExecutor<SysTable> {
    List<SysTable> findBySysTableTypeId(String sysTableTypeId);
}
