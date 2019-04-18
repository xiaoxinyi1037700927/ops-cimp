package com.sinosoft.ops.cimp.service.sys.permission;

import com.sinosoft.ops.cimp.entity.sys.permission.Permission;
import com.sinosoft.ops.cimp.vo.to.sys.permission.AddMenuToGroupViewModel;
import com.sinosoft.ops.cimp.vo.to.sys.permission.SaveMenuGroupSortViewModel;
import com.sinosoft.ops.cimp.vo.from.sys.permission.PermissionModel;
import com.sinosoft.ops.cimp.vo.to.sys.role.RoleViewModel;
import com.sinosoft.ops.cimp.vo.to.sys.role.MenuChildViewModel;
import com.sinosoft.ops.cimp.vo.to.sys.role.MenuParentViewModel;

import java.util.List;

public interface PermissionService {

    Iterable<Permission> findData(PermissionModel permissionModel);

    Permission getById(String permissionId);

    Permission save(PermissionModel rightModel);

    Permission update(PermissionModel rightModel);

    void deleteById(String rightId);

    boolean saveMenuParent(MenuParentViewModel menuParentViewModel);

    boolean deleteMenuParentById(String id);

    List<MenuChildViewModel> menuListByParentId(String parentId);

    boolean deleteMenuByParentIdAndMenuId(String parentId, String menuId);

    boolean deleteMenuByParentIdAndMenuId(AddMenuToGroupViewModel viewModel);

    List<MenuChildViewModel> findNotHaveMenuListByParentId(String parentId);

    boolean addMenuByParentIdAndMenuId(String parentId, String menuId);

    boolean addMenuByParentIdAndMenuId(AddMenuToGroupViewModel viewModel);

    List<MenuChildViewModel> findAllMenuList();

    boolean deleteMenuById(String menuId);

    boolean saveMenu(MenuChildViewModel menuChildViewModel);

    String saveRole(RoleViewModel viewModel);

    boolean deleteRoleById(String id);

    boolean saveMenuGroupSort(SaveMenuGroupSortViewModel viewModel);

    boolean saveMenuRoleSort(SaveMenuGroupSortViewModel viewModel);

    boolean doTest();






}
