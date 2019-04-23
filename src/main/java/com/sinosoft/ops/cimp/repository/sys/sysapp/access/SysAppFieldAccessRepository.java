package com.sinosoft.ops.cimp.repository.sys.sysapp.access;

import com.sinosoft.ops.cimp.entity.sys.sysapp.fieldAccess.SysAppRoleFieldAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysAppFieldAccessRepository extends JpaRepository<SysAppRoleFieldAccess, String>, QuerydslPredicateExecutor<SysAppRoleFieldAccess> {

    void deleteBySysAppTableFieldSetIdIn(List<String> ids);

    void deleteBySysAppRoleTableAccessIdIn(List<String> ids);
}
