package com.sinosoft.ops.cimp.repository.user;

import com.sinosoft.ops.cimp.entity.user.RolePermissionTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface RolePermissionTableRepository extends JpaRepository<RolePermissionTable,String>,
        QuerydslPredicateExecutor<RolePermissionTable> {

    /**
     * 查询排序字段
     * @param roleId
     * @return
     */
    @Query("select max(sortNumber) from RolePermissionTable where roleId = ?1 ")
    Integer getSortNumberByRoleId(String roleId);

}
