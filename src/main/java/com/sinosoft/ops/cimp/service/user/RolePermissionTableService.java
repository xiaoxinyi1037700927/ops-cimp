package com.sinosoft.ops.cimp.service.user;

import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.vo.from.user.rolePermissionTable.RPTableAddModel;
import com.sinosoft.ops.cimp.vo.from.user.rolePermissionTable.RPTableSearchModel;
import com.sinosoft.ops.cimp.vo.to.user.rolePermissionTable.RPTableViewModel;

import java.util.List;

public interface RolePermissionTableService {

    /**
     * 查询角色关联table分页列表
     * @param searchModel
     * @return
     */
    PaginationViewModel<RPTableViewModel> findRPTablePageList(RPTableSearchModel searchModel);

    /**
     * 根据roleId 查询角色关联table列表
     * @param roleId
     * @return
     */
    List<RPTableViewModel> findRPTableListByRoleId(String roleId);

    /**
     * 新增 角色关联table
     * @param addModel
     * @return
     */
    Boolean saveRPTable(RPTableAddModel addModel);

    /**
     * 修改 角色关联table
     * @param modifyModel
     * @return
     */
    Boolean modifyRPTable(RPTableViewModel modifyModel);

    /**
     * 删除 角色关联table
     * @param ids
     * @return
     */
    Boolean deleteRPTable(List<String> ids);

    /**
     * 上移 或者 下移
     * @param ids
     * @return
     */
    Boolean changeRPTableSort(List<String> ids);




}
