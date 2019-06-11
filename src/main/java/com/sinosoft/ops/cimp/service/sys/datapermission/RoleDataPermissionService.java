package com.sinosoft.ops.cimp.service.sys.datapermission;


import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.RoleDataPerModifyModel;

public interface RoleDataPermissionService {

    /**
     * 修改角色数据权限
     *
     * @param modifyModel
     */
    void modifyRoleDataPermission(RoleDataPerModifyModel modifyModel);

}
