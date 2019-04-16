package com.sinosoft.ops.cimp.repository.user;


import com.sinosoft.ops.cimp.entity.sys.user.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface PermissionRepository extends JpaRepository<Permission,String>, QuerydslPredicateExecutor<Permission> {
}
