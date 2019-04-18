package com.sinosoft.ops.cimp.controller.sys.user.permissionPage;

import com.google.common.collect.Lists;
import com.sinosoft.ops.cimp.config.annotation.swaggergroup.ApiPermissionInfo;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.entity.sys.user.MenuGroup;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.mapper.user.MenuMapper;
import com.sinosoft.ops.cimp.repository.user.MenuGroupRepository;
import com.sinosoft.ops.cimp.service.user.PermissionService;
import com.sinosoft.ops.cimp.service.user.RoleService;
import com.sinosoft.ops.cimp.service.user.permissionPage.UserRoleService;
import com.sinosoft.ops.cimp.swaggwegroup.RequiresAuthentication;
import com.sinosoft.ops.cimp.vo.to.user.AddMenuToGroupViewModel;
import com.sinosoft.ops.cimp.vo.to.user.MenuByRoleIdAndMenuId;
import com.sinosoft.ops.cimp.vo.to.user.SaveMenuGroupSortViewModel;
import com.sinosoft.ops.cimp.vo.user.RoleGroupViewModel;
import com.sinosoft.ops.cimp.vo.user.RoleViewModel;
import com.sinosoft.ops.cimp.vo.user.role.MenuChildViewModel;
import com.sinosoft.ops.cimp.vo.user.role.MenuParentViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApiPermissionInfo
@Api(description = "权限管理接口")
@RestController
@RequestMapping("/permission")
public class PermissionController extends BaseController {
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;



    @ApiOperation(value = "查询菜单分组")
    @PostMapping("/menuParentList")
    @RequiresAuthentication
    public ResponseEntity<List<MenuParentViewModel>> menuParentList() throws BusinessException {
        ArrayList<MenuGroup> sortNumber = Lists.newArrayList(menuGroupRepository.findAll(Sort.by(Sort.Order.asc("sortNumber"))));
        List<MenuParentViewModel> collect = sortNumber.stream().map(x -> MenuMapper.INSTANCE.entityToMenuParentViewModel(x)).collect(Collectors.toList());
        return ok(collect);
    }

    @ApiOperation(value = "保存菜单分组")
    @PostMapping("/saveMenuParent")
    @RequiresAuthentication
    public ResponseEntity<Boolean> saveMenuParent(@RequestBody MenuParentViewModel menuParentViewModel) throws BusinessException {
        boolean saveMenuParent = permissionService.saveMenuParent(menuParentViewModel);
        if (saveMenuParent) {
            return ok(saveMenuParent);
        } else {
            return fail("保存失败");
        }
    }

    @ApiOperation(value = "通过ID删除父级菜单")
    @PostMapping("/deleteMenuParentById")
    @RequiresAuthentication
    public ResponseEntity<Boolean> deleteMenuParentById(@RequestParam String id) throws BusinessException {
        boolean menuParentById = permissionService.deleteMenuParentById(id);
        if (menuParentById) {
            return ok(menuParentById);
        } else {
            return fail("该菜单下存在子集菜单不能删除！");
        }
    }

    @ApiOperation(value = "通过父级ID查询已拥有菜单")
    @PostMapping("/findHaveMenuListByParentId")
    @RequiresAuthentication
    public ResponseEntity<List<MenuChildViewModel>> menuListByParentId(@RequestParam String parentId
                                                               ) throws BusinessException {
        List<MenuChildViewModel> menuChildViewModels = permissionService.menuListByParentId(parentId);
        return ok(menuChildViewModels);
    }

    @ApiOperation(value = "通过ID删除分组下的菜单")
    @PostMapping("/deleteMenuByParentIdAndMenuId")
    @RequiresAuthentication
    public ResponseEntity<Boolean> deleteMenuByParentIdAndMenuId(@RequestParam String parentId,
                                                         @RequestParam String menuId
                                                         ) throws BusinessException {
        boolean deleteMenuByParentIdAndMenuId = permissionService.deleteMenuByParentIdAndMenuId(parentId, menuId);
        if (deleteMenuByParentIdAndMenuId) {
            return ok(deleteMenuByParentIdAndMenuId);
        } else {
            return fail("该菜单权限已被其他角色使用，不能移除！");
        }
    }


    @ApiOperation(value = "通过ID删除分组下的菜单 多删")
    @PostMapping("/deleteMenuByParentIdAndMenuIds")
    @RequiresAuthentication
    public ResponseEntity<Boolean> deleteMenuByParentIdAndMenuIds(@RequestBody AddMenuToGroupViewModel viewModel) throws BusinessException {
        boolean deleteMenuByParentIdAndMenuId = permissionService.deleteMenuByParentIdAndMenuId(viewModel);
        if (deleteMenuByParentIdAndMenuId) {
            return ok(deleteMenuByParentIdAndMenuId);
        } else {
            return fail("该菜单权限已被其他角色使用，不能移除！");
        }
    }

    @ApiOperation(value = "通过父级ID查询未拥有菜单")
    @PostMapping("/findNotHaveMenuListByParentId")
    @RequiresAuthentication
    public ResponseEntity<List<MenuChildViewModel>> findNotHaveMenuListByParentId(@RequestParam String parentId
                                                                          ) throws BusinessException {
        List<MenuChildViewModel> menuChildViewModels = permissionService.findNotHaveMenuListByParentId(parentId);
        return ok(menuChildViewModels);
    }

    @ApiOperation(value = "通过ID给分组增加菜单")
    @PostMapping("/addMenuByParentIdAndMenuId")
    @RequiresAuthentication
    public ResponseEntity<Boolean> addMenuByParentIdAndMenuId(@RequestParam String parentId,
                                                      @RequestParam String menuId
                                                      ) throws BusinessException {
        boolean addMenuByParentIdAndMenuId = permissionService.addMenuByParentIdAndMenuId(parentId, menuId);
        if (addMenuByParentIdAndMenuId) {
            return ok(addMenuByParentIdAndMenuId);
        } else {
            return fail("不能重复添加该菜单！");
        }
    }

    @ApiOperation(value = "通过ID给分组增加菜单 多加")
    @PostMapping("/addMenuByParentIdAndMenuIds")
    @RequiresAuthentication
    public ResponseEntity<Boolean> addMenuByParentIdAndMenuIds(@RequestBody AddMenuToGroupViewModel viewModel) throws BusinessException {
        boolean addMenuByParentIdAndMenuId = permissionService.addMenuByParentIdAndMenuId(viewModel);
        if (addMenuByParentIdAndMenuId) {
            return ok(addMenuByParentIdAndMenuId);
        } else {
            return fail("不能重复添加该菜单！");
        }
    }

    @ApiOperation(value = "保存分组下菜单的排序")
    @PostMapping("/saveMenuGroupSort")
    @RequiresAuthentication
    public ResponseEntity<Boolean> SaveMenuGroupSortViewModel(@RequestBody SaveMenuGroupSortViewModel viewModel) throws BusinessException {
        boolean saveMenuGroupSort = permissionService.saveMenuGroupSort(viewModel);
        if (saveMenuGroupSort) {
            return ok(saveMenuGroupSort);
        } else {
            return fail("操作失败！");
        }
    }


    @ApiOperation(value = "保存角色下菜单的排序")
    @PostMapping("/saveMenuRoleSort")
    @RequiresAuthentication
    public ResponseEntity<Boolean> SaveMenuRoleSortViewModel(@RequestBody SaveMenuGroupSortViewModel viewModel) throws BusinessException {
        boolean saveMenuGroupSort = permissionService.saveMenuRoleSort(viewModel);
        if (saveMenuGroupSort) {
            return ok(saveMenuGroupSort);
        } else {
            return fail("操作失败！");
        }
    }


    @ApiOperation(value = "查询所有菜单")
    @PostMapping("/findAllMenuList")
    @RequiresAuthentication
    public ResponseEntity<List<MenuChildViewModel>> findAllMenuList() throws BusinessException {
        List<MenuChildViewModel> menuChildViewModels = permissionService.findAllMenuList();
        return ok(menuChildViewModels);
    }


    @ApiOperation(value = "通过ID删除菜单")
    @PostMapping("/deleteMenuById")
    @RequiresAuthentication
    public ResponseEntity<Boolean> deleteMenuById(
            @RequestParam String menuId) throws BusinessException {
        boolean deleteMenuById = permissionService.deleteMenuById(menuId);
        if (deleteMenuById) {
            return ok(deleteMenuById);
        } else {
            return fail("不能删除已经在使用的菜单！");
        }
    }

    @ApiOperation(value = "保存菜单")
    @PostMapping("/saveMenu")
    @RequiresAuthentication
    public ResponseEntity<Boolean> saveMenu(@RequestBody MenuChildViewModel menuChildViewModel) throws BusinessException {
        boolean save = permissionService.saveMenu(menuChildViewModel);
        if (save) {
            return ok(save);
        } else {
            return fail("保存失败");
        }
    }

    @ApiOperation(value = "查询所有角色")
    @PostMapping("/findAllRole")
    @RequiresAuthentication
    public ResponseEntity<List<RoleViewModel>> findAllRole() throws BusinessException {
        List<RoleViewModel> allRole = userRoleService.findAllRole();
        return ok(allRole);
    }


    @ApiOperation(value = "保存角色")
    @PostMapping("/saveRole")
    @RequiresAuthentication
    public ResponseEntity<String> saveRole(@Valid @RequestBody RoleViewModel viewModel) throws BusinessException {
        String save = permissionService.saveRole(viewModel);
        if ("1".equals(save)) {
            return fail("该角色已经存在！");
        } else {
            return ok("保存成功！");
        }
    }

    @ApiOperation(value = "通过ID删除角色")
    @PostMapping("/deleteRoleById")
    @RequiresAuthentication
    public ResponseEntity<Boolean> deleteRoleById(@RequestParam String id) throws BusinessException {
        boolean save = permissionService.deleteRoleById(id);
        if (save) {
            return ok(save);
        } else {
            return fail("删除失败");
        }
    }


    @ApiOperation(value = "查询角色所拥有父级菜单")
    @PostMapping("/findMenuParentListByRoleId")
    @RequiresAuthentication
    public ResponseEntity<List<MenuParentViewModel>> findMenuParentListByRoleId(@RequestParam String roleId
                                                                        ) throws BusinessException {
        List<MenuParentViewModel> menuParentViewModelByRoleId = userRoleService.findMenuParentViewModelByRoleId(roleId);
        return ok(menuParentViewModelByRoleId);
    }

    @ApiOperation(value = "查询角色父级菜单下所拥有的子集菜单")
    @PostMapping("/findHaveMenuListByParentIdAndRoleId")
    @RequiresAuthentication
    public ResponseEntity<List<MenuChildViewModel>> findHaveMenuListByParentIdAndRoleId(@RequestParam String parentId,
                                                                                @RequestParam String roleId) throws BusinessException {
        List<MenuChildViewModel> menuChildViewModelByRoleIdAndMenuGroupId = userRoleService.findMenuChildViewModelByRoleIdAndMenuGroupId(roleId, parentId);
        return ok(menuChildViewModelByRoleIdAndMenuGroupId);
    }

    @ApiOperation(value = "查询角色父级菜单下未拥有的子集菜单")
    @PostMapping("/findNotHaveMenuListByParentIdAndRoleId")
    @RequiresAuthentication
    public ResponseEntity<List<MenuChildViewModel>> findNotHaveMenuListByParentIdAndRoleId(@RequestParam String parentId,
                                                                                   @RequestParam String roleId) throws BusinessException {
        List<MenuChildViewModel> menuChildViewModelByRoleIdAndMenuGroupId = userRoleService.findNotMenuChildViewModelByRoleIdAndMenuGroupId(roleId, parentId);
        return ok(menuChildViewModelByRoleIdAndMenuGroupId);
    }


    @ApiOperation(value = "通过ID增加角色菜单")
    @PostMapping("/addMenuByRoleIdAndMenuId")
    @RequiresAuthentication
    public ResponseEntity<Boolean> addMenuByRoleIdAndMenuId(@RequestParam String roleId,
                                                    @RequestParam String menuId,
                                                    @RequestParam String parentId) throws BusinessException {
        boolean roleIdAndMenuId = userRoleService.addMenuByRoleIdAndMenuId(roleId, menuId, parentId);
        if (roleIdAndMenuId) {
            return ok(roleIdAndMenuId);
        } else {
            return fail("不能重复添加该菜单！");
        }
    }

    @ApiOperation(value = "通过ID增加角色菜单 多加")
    @PostMapping("/addMenuByRoleIdAndMenuIds")
    @RequiresAuthentication
    public ResponseEntity<Boolean> addMenuByRoleIdAndMenuIds(@RequestBody MenuByRoleIdAndMenuId viewModel) throws BusinessException {
        boolean roleIdAndMenuId = userRoleService.addMenuByRoleIdAndMenuId(viewModel);
        if (roleIdAndMenuId) {
            return ok(roleIdAndMenuId);
        } else {
            return fail("不能重复添加该菜单！");
        }
    }

    @ApiOperation(value = "通过ID删除角色菜单 删除")
    @PostMapping("/deleteMenuByRoleIdAndMenuIds")
    @RequiresAuthentication
    public ResponseEntity<Boolean> deleteMenuByRoleIdAndMenuIds(@RequestBody MenuByRoleIdAndMenuId viewModel) throws BusinessException {
        boolean roleIdAndMenuId = userRoleService.deleteMenuByRoleIdAndMenuIds(viewModel);
        if (roleIdAndMenuId) {
            return ok(roleIdAndMenuId);
        } else {
            return fail("不能重复删除该菜单！");
        }
    }


    @ApiOperation(value = "获取角色拥有的菜单列表")
    @PostMapping("/findRoleMenuList")
    @RequiresAuthentication
    public ResponseEntity<List<MenuParentViewModel>> findRoleMenuList(
                                                              @RequestParam(required = false) String type ) throws BusinessException {
        List<MenuParentViewModel> roleMenuList = userRoleService.findRoleMenuList(type);
        return ok(roleMenuList);
    }

    @ApiOperation(value = "保存角色父级菜单")
    @PostMapping("/saveRoleGroup")
    @RequiresAuthentication
    public ResponseEntity<Boolean> saveRoleGroup(@RequestBody List<RoleGroupViewModel> roleGroupViewModel) throws BusinessException {
        boolean saveRoleMenu = userRoleService.saveRoleMenu(roleGroupViewModel);
        return ok(saveRoleMenu);
    }

    @ApiOperation(value = "通过ID删除角色父级菜单")
    @PostMapping("/deleteRoleGroupById")
    @RequiresAuthentication
    public ResponseEntity<Boolean> deleteRoleGroupById(@RequestParam String id) throws BusinessException {
        boolean b = userRoleService.deleteRoleGroupById(id);
        if (b) {
            return ok(b);
        } else {
            return fail("请先移除下子集权限！");
        }
    }

}
