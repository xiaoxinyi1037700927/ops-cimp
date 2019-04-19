package com.sinosoft.ops.cimp.service.user;

import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.vo.from.user.rolePermissionPageSql.RPPageSqlAddModel;
import com.sinosoft.ops.cimp.vo.from.user.rolePermissionPageSql.RPPageSqlSearchModel;
import com.sinosoft.ops.cimp.vo.to.user.rolePermissionPageSql.RPPageSqlViewModel;

import java.util.List;

public interface RolePermissionPageSqlService {

    /**
     * 分页查询 角色权限页面SQl
     * @param searchModel
     * @return
     */
    PaginationViewModel<RPPageSqlViewModel> findRPPageSqlPageList(RPPageSqlSearchModel searchModel);

    /**
     * 根据角色id查询 角色权限页面SQl
     * @param roleId
     * @return
     */
    RPPageSqlViewModel findRPPageSqlByRoleId(String roleId);

    /**
     * 根据roleIds查询 角色权限页面SQl
     * @param searchModel
     * @return
     */
    List<RPPageSqlViewModel> findRPPageSqlListByRoleIds(RPPageSqlSearchModel searchModel);

    /**
     * 新增 角色权限页面SQl
     * @param addModel
     * @return
     */
    Boolean saveRPPageSql(RPPageSqlAddModel addModel);

    /**
     * 修改 角色权限页面SQl
     * @param modifyModel
     * @return
     */
    Boolean modifyRPPageSql(RPPageSqlViewModel modifyModel);

    /**
     * 删除 角色权限页面SQl
     * @param ids
     * @return
     */
    Boolean deleteRPPageSql(List<String> ids);

}
