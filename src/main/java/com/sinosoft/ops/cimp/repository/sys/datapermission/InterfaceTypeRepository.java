package com.sinosoft.ops.cimp.repository.sys.datapermission;

import com.sinosoft.ops.cimp.entity.sys.datapermission.InterfaceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface InterfaceTypeRepository extends JpaRepository<InterfaceType, String>, QuerydslPredicateExecutor<InterfaceType> {

}
