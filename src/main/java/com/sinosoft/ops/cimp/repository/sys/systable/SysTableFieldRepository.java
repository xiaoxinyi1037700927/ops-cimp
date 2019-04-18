package com.sinosoft.ops.cimp.repository.sys.systable;


import com.sinosoft.ops.cimp.entity.sys.systable.SysTableField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface SysTableFieldRepository extends JpaRepository<SysTableField, String>, QuerydslPredicateExecutor<SysTableField> {

    List<SysTableField> findBySysTableId(String sysCodeId);

}
