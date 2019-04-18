package com.sinosoft.ops.cimp.service.user;

import com.sinosoft.ops.cimp.entity.user.Role;
import com.sinosoft.ops.cimp.entity.user.User;
import com.sinosoft.ops.cimp.vo.to.sys.permission.MenuByRoleIdAndMenuId;
import com.sinosoft.ops.cimp.vo.to.sys.role.RoleGroupViewModel;
import com.sinosoft.ops.cimp.vo.to.sys.role.RoleViewModel;
import com.sinosoft.ops.cimp.vo.to.sys.role.MenuChildViewModel;
import com.sinosoft.ops.cimp.vo.to.sys.role.MenuParentViewModel;

import java.util.List;

public interface UserRoleService {
    /**
     * 根据用户ID查询角色列表
     * @param userId
     * @return
     */
    List<Role> getRolesByUserId(String userId);

    /**
     * 查询角色所拥有的父级菜单
     * */
    List<MenuParentViewModel> findMenuParentViewModelByRoleId(String roleId);

    /**
     * 查询角色所拥有的父级菜单
     * */
    List<MenuParentViewModel> findMenuParentViewModelByRoleId(List<String> roleIds);

    /**
     * 查询父级菜单下所拥有的子集菜单
     * */
    List<MenuChildViewModel> findMenuChildViewModelByRoleIdAndMenuGroupId(String roleId, String menuGroupId);

    /**
     * 查询父级菜单下所拥有的子集菜单
     * */
    List<MenuChildViewModel> findMenuChildViewModelByRoleIdAndMenuGroupId(List<String> roleIds, String menuGroupId);

    /**
     * 查询父级菜单下未拥有的子集菜单
     * */
    List<MenuChildViewModel> findNotMenuChildViewModelByRoleIdAndMenuGroupId(String roleId, String menuGroupId);

    /**
     * 查询父级菜单下未拥有的子集菜单
     * */
    boolean addMenuByRoleIdAndMenuId(String roleId, String menuId, String parentId);

    /**
     * 查询父级菜单下未拥有的子集菜单 多加
     * */
    boolean addMenuByRoleIdAndMenuId(MenuByRoleIdAndMenuId viewModel);

    /**
     * 删除父级菜单下未拥有的子集菜单 删除
     * */
    boolean deleteMenuByRoleIdAndMenuIds(MenuByRoleIdAndMenuId viewModel);

    /**
     * 保存角色父级菜单
     * */
    boolean saveRoleMenu(List<RoleGroupViewModel> roleGroupViewModel);

    /**
     * 通过ID删除角色父级菜单
     * */
    boolean deleteRoleGroupById(String id);

    List<MenuParentViewModel> findRoleMenuList( String type);

    List<RoleViewModel> findAllRole();

    List<User> findReceivesByOrganizationId(String organizationId, String roleCode);




}
