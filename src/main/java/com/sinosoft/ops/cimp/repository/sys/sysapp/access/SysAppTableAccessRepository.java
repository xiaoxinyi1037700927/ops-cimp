package com.sinosoft.ops.cimp.repository.sys.sysapp.access;

import com.sinosoft.ops.cimp.entity.sys.sysapp.fieldAccess.SysAppRoleTableAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysAppTableAccessRepository extends JpaRepository<SysAppRoleTableAccess, String>, QuerydslPredicateExecutor<SysAppRoleTableAccess> {

    List<SysAppRoleTableAccess> findByRoleId(String roleId);

    void deleteBySysAppTableSetIdIn(List<String> tableSetIds);

    List<SysAppRoleTableAccess> findBySysAppTableSetId(String tableSetId);
}
