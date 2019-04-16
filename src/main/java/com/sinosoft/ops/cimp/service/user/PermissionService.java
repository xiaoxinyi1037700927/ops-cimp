package com.sinosoft.ops.cimp.service.user;

import com.sinosoft.ops.cimp.entity.sys.user.Permission ;
import com.sinosoft.ops.cimp.vo.to.user.AddMenuToGroupViewModel;
import com.sinosoft.ops.cimp.vo.to.user.SaveMenuGroupSortViewModel;
import com.sinosoft.ops.cimp.vo.user.PermissionModel ;
//import com.sinosoft.ops.appointdismisssupervise.vo.to.common.system.permission.AddMenuToGroupViewModel;
//import com.sinosoft.ops.appointdismisssupervise.vo.to.common.system.permission.SaveMenuGroupSortViewModel;
//import com.sinosoft.ops.appointdismisssupervise.vo.to.common.system.role.MenuChildViewModel;
//import com.sinosoft.ops.appointdismisssupervise.vo.to.common.system.role.MenuParentViewModel;
import com.sinosoft.ops.cimp.vo.user.RoleViewModel ;
import com.sinosoft.ops.cimp.vo.user.role.MenuChildViewModel;
import com.sinosoft.ops.cimp.vo.user.role.MenuParentViewModel;

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
