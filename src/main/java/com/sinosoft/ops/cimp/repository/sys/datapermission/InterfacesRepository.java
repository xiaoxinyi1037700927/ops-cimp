package com.sinosoft.ops.cimp.repository.sys.datapermission;

import com.sinosoft.ops.cimp.entity.sys.datapermission.Interfaces;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface InterfacesRepository extends JpaRepository<Interfaces, String>, QuerydslPredicateExecutor<Interfaces> {

}
