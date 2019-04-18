package com.sinosoft.ops.cimp.service.user;

import com.sinosoft.ops.cimp.entity.user.Role;
import com.sinosoft.ops.cimp.vo.from.sys.role.RoleModel;


public interface RoleService {

   // Iterable<Role> findData(RoleModel roleModel);
    Iterable<Role> findData();

    Role getById(String roleId);

    Role save(RoleModel roleModel);

    Role update(RoleModel roleModel);

    void deleteById(String roleId);

    void addRelation(String roleId, String permissionId);

    void deleteRelation(String roleId, String permissionId);
}
