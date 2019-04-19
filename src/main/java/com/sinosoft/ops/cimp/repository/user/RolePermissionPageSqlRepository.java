package com.sinosoft.ops.cimp.repository.user;

import com.sinosoft.ops.cimp.entity.user.RolePermissionPageSql;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface RolePermissionPageSqlRepository extends JpaRepository<RolePermissionPageSql,String>,
        QuerydslPredicateExecutor<RolePermissionPageSql> {

}
