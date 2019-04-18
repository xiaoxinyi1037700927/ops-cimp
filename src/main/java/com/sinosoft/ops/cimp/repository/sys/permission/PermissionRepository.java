package com.sinosoft.ops.cimp.repository.sys.permission;


import com.sinosoft.ops.cimp.entity.sys.permission.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface PermissionRepository extends JpaRepository<Permission,String>, QuerydslPredicateExecutor<Permission> {
}
