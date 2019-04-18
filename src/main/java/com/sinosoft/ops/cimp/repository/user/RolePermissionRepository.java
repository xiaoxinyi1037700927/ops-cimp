package com.sinosoft.ops.cimp.repository.user;


import com.sinosoft.ops.cimp.entity.user.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface RolePermissionRepository extends JpaRepository<RolePermission,String>
        , QuerydslPredicateExecutor<RolePermission> {
}
