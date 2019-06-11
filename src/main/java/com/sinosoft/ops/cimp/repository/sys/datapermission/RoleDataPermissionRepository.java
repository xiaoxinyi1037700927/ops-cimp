package com.sinosoft.ops.cimp.repository.sys.datapermission;

import com.sinosoft.ops.cimp.entity.sys.datapermission.RoleDataPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface RoleDataPermissionRepository extends JpaRepository<RoleDataPermission, String>, QuerydslPredicateExecutor<RoleDataPermission> {

}
