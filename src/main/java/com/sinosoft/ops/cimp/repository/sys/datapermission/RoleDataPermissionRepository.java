package com.sinosoft.ops.cimp.repository.sys.datapermission;

import com.sinosoft.ops.cimp.entity.sys.datapermission.RoleDataPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface RoleDataPermissionRepository extends JpaRepository<RoleDataPermission, String>, QuerydslPredicateExecutor<RoleDataPermission> {

    List<RoleDataPermission> findByRoleIdAndInterfaceId(String roleId, String interfaceId);
}
