package com.sinosoft.ops.cimp.service.sys.datapermission;


import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.RoleDataPerAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.RoleDataPerDeleteModel;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.RoleDataPerModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.RoleDataPerSearchModel;
import com.sinosoft.ops.cimp.vo.to.sys.datapermission.RoleDataPerModel;
import com.sinosoft.ops.cimp.vo.to.sys.datapermission.SqlTypeModel;

import java.util.List;

public interface RoleDataPermissionService {

    /**
     * 角色数据权限列表
     *
     * @param searchModel
     * @return
     */
    PaginationViewModel<RoleDataPerModel> listRoleDataPermission(RoleDataPerSearchModel searchModel);

    /**
     * 添加角色数据权限
     *
     * @param addModel
     */
    void addRoleDataPermission(RoleDataPerAddModel addModel);

    /**
     * 修改角色数据权限
     *
     * @param modifyModel
     */
    boolean modifyRoleDataPermission(RoleDataPerModifyModel modifyModel);

    /**
     * 删除角色数据权限
     *
     * @param deleteModel
     */
    void deleteRoleDataPermission(RoleDataPerDeleteModel deleteModel);

    /**
     * 获取sql类型
     *
     * @return
     */
    List<SqlTypeModel> getSqlTypes();

    /**
     * 获取角色的数据权限sql
     *
     * @param roleIds
     * @param type
     * @return
     */
    List<String> getSqls(List<String> roleIds, String type);
}
