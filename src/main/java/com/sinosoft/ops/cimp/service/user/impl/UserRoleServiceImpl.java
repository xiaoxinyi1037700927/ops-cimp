package com.sinosoft.ops.cimp.service.user.impl;

import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sinosoft.ops.cimp.entity.sys.permission.*;
import com.sinosoft.ops.cimp.entity.user.*;
import com.sinosoft.ops.cimp.mapper.sys.permission.MenuMapper;
import com.sinosoft.ops.cimp.mapper.user.RoleViewModelMapper;
import com.sinosoft.ops.cimp.repository.sys.permission.MenuGroupRepository;
import com.sinosoft.ops.cimp.repository.user.*;
import com.sinosoft.ops.cimp.service.user.UserRoleService;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.vo.to.sys.permission.MenuByRoleIdAndMenuId;
import com.sinosoft.ops.cimp.vo.to.sys.role.RoleGroupViewModel;
import com.sinosoft.ops.cimp.vo.to.sys.role.RoleViewModel;
import com.sinosoft.ops.cimp.vo.to.sys.role.MenuChildViewModel;
import com.sinosoft.ops.cimp.vo.to.sys.role.MenuParentViewModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RolePermissionRepository rolePermissionRepository;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private RoleGroupRepository roleGroupRepository;
    @Autowired
    private RoleHomePageCountRepository roleHomePageCountRepository;

    @PersistenceContext
    private EntityManager entityManager;
    private JPAQueryFactory queryFactory;

    @PostConstruct
    public void getFactory() {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Role> getRolesByUserId(String userId) {
        List<UserRole> userRoles = userRoleRepository.findByUserId(userId);
        List<String> roleIdList = new ArrayList<>();
        userRoles.forEach(userRole -> roleIdList.add(userRole.getRoleId()));
        List<Role> roleList = roleRepository.findByIdIn(roleIdList);
        return roleList;
    }

    @Override
    public List<MenuParentViewModel> findMenuParentViewModelByRoleId(String roleId) {
        List<RoleMenuGroup> menuGroups = (List) roleGroupRepository.findAll(QRoleMenuGroup.roleMenuGroup.roleId.eq(roleId), Sort.by(Sort.Order.asc("sortNumber")));
        List<MenuParentViewModel> collect = Lists.newArrayList(menuGroups.stream().map(x -> MenuMapper.INSTANCE.entityToRoleMenuViewModel(x)).collect(Collectors.toList()));
        if (collect != null && collect.size() > 0) {
            for (MenuParentViewModel menuParentViewModel : collect) {
                List<MenuChildViewModel> menuChildViewModelByRoleIdAndMenuGroupId = userRoleService.findMenuChildViewModelByRoleIdAndMenuGroupId(roleId, menuParentViewModel.getId());
                if (menuChildViewModelByRoleIdAndMenuGroupId == null || menuChildViewModelByRoleIdAndMenuGroupId.size() == 0) {
                    menuChildViewModelByRoleIdAndMenuGroupId = new ArrayList<>(0);
                }
                menuChildViewModelByRoleIdAndMenuGroupId.stream().forEach(x -> x.setParentId(menuParentViewModel.getId()));
                menuParentViewModel.setChildren(menuChildViewModelByRoleIdAndMenuGroupId);
            }
        } else {
            collect = new ArrayList<>(0);
        }
        return collect;
    }

    @Override
    public List<MenuParentViewModel> findMenuParentViewModelByRoleId(List<String> roleIds) {
        List<RoleMenuGroup> menuGroups = (List) roleGroupRepository.findAll(QRoleMenuGroup.roleMenuGroup.roleId.in(roleIds), Sort.by(Sort.Order.asc("sortNumber")));
        List<MenuParentViewModel> collect = Lists.newArrayList(menuGroups.stream().map(x -> MenuMapper.INSTANCE.entityToRoleMenuViewModel(x)).collect(Collectors.toList()));
        return collect;
    }

    @Override
    public List<MenuChildViewModel> findMenuChildViewModelByRoleIdAndMenuGroupId(String roleId, String menuGroupId) {
        JPAQuery<Permission> where = queryFactory.select(QPermission.permission)
                .from(QRolePermission.rolePermission)
                .leftJoin(QPermission.permission).on(QPermission.permission.id.eq(QRolePermission.rolePermission.permissionId))
                .where(QRolePermission.rolePermission.roleMenuGroupId.eq(menuGroupId).and(QRolePermission.rolePermission.roleId.eq(roleId)));
        List<Permission> fetch = Lists.newArrayList(where.orderBy(QPermission.permission.sortNumber.asc()).fetch());
        List<MenuChildViewModel> menuChildViewModels = Lists.newArrayList(fetch.stream().map(x -> MenuMapper.INSTANCE.entityToMenuChildViewModel(x)).collect(Collectors.toList()));
        if (menuChildViewModels != null) {
            menuChildViewModels.stream().forEach(x -> x.setParentId(menuGroupId));
        }
        return menuChildViewModels;
    }

    public static List removeDuplicate(List list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }

    @Override
    public List<MenuChildViewModel> findMenuChildViewModelByRoleIdAndMenuGroupId(List<String> roleIds, String menuGroupId) {
        JPAQuery<MenuChildViewModel> where = queryFactory.select(
                Projections.bean(
                        MenuChildViewModel.class,
                        QPermission.permission.id,
                        QPermission.permission.url,
                        QPermission.permission.description,
                        QPermission.permission.name,
                        QPermission.permission.color,
                        QPermission.permission.icon,
                        QPermission.permission.introduction,
                        QPermission.permission.parentId,
                        QPermission.permission.sortNumber,
                        QPermission.permission.type,
                        QRolePermission.rolePermission.roleMenuGroupId.as("groupId"),
                        QRoleMenuGroup.roleMenuGroup.name.as("groupName")
                )
        ).from(QRolePermission.rolePermission)
                .leftJoin(QPermission.permission).on(QPermission.permission.id.eq(QRolePermission.rolePermission.permissionId))
                .leftJoin(QRoleMenuGroup.roleMenuGroup).on(QRoleMenuGroup.roleMenuGroup.id.eq(QRolePermission.rolePermission.roleMenuGroupId))
                .where(QRolePermission.rolePermission.roleMenuGroupId.eq(menuGroupId).and(QRolePermission.rolePermission.roleId.in(roleIds)));
        ArrayList<MenuChildViewModel> menuChildViewModels1 = Lists.newArrayList(where.orderBy(QRolePermission.rolePermission.sortNumber.asc()).fetch());
//        if(roleIds.size()>1){
//            fetch=removeDuplicate(fetch);
//        }
//        List<MenuChildViewModel> menuChildViewModels = Lists.newArrayList(fetch.stream().map(x -> MenuMapper.INSTANCE.entityToMenuChildViewModel(x)).collect(Collectors.toList()));

        return menuChildViewModels1;
    }

    @Override
    public List<MenuChildViewModel> findNotMenuChildViewModelByRoleIdAndMenuGroupId(String roleId, String menuGroupId) {
        JPAQuery<String> distinct = queryFactory.select(QPermission.permission.id).from(QRolePermission.rolePermission)
                .innerJoin(QPermission.permission).on(QPermission.permission.id.eq(QRolePermission.rolePermission.permissionId))
                .where(QRolePermission.rolePermission.roleMenuGroupId.eq(menuGroupId).and(QRolePermission.rolePermission.roleId.eq(roleId))).distinct();
        ArrayList<String> strings = Lists.newArrayList(distinct.fetch());

        JPAQuery<Permission> where = queryFactory.select(QPermission.permission).from(QPermissionMenuGroupRel.permissionMenuGroupRel)
                .innerJoin(QPermission.permission).on(QPermission.permission.id.eq(QPermissionMenuGroupRel.permissionMenuGroupRel.permissionId))
                .where(QPermissionMenuGroupRel.permissionMenuGroupRel.permissionId.notIn(strings).and(QPermissionMenuGroupRel.permissionMenuGroupRel.menuGroupId.eq(menuGroupId)));
        ArrayList<Permission> permissions = Lists.newArrayList(where.fetch());
        List<MenuChildViewModel> collect = permissions.stream().map(x -> MenuMapper.INSTANCE.entityToMenuChildViewModel(x)).collect(Collectors.toList());
        if (collect != null) {
            collect.stream().forEach(x -> x.setParentId(menuGroupId));
        }
        return collect;
    }

    @Override
    public boolean addMenuByRoleIdAndMenuId(String roleId, String menuId, String parentId) {
        long count = rolePermissionRepository.count(QRolePermission.rolePermission.permissionId.eq(menuId).and(QRolePermission.rolePermission.roleId.eq(roleId)).and(QRolePermission.rolePermission.roleMenuGroupId.eq(parentId)));
        if (count > 0) {
            return true;
        }
        RolePermission rolePermission = new RolePermission();
        rolePermission.setPermissionId(menuId);
        rolePermission.setRoleMenuGroupId(parentId);
        rolePermission.setRoleId(roleId);
        rolePermissionRepository.save(rolePermission);
        return true;
    }

    @Override
    public boolean addMenuByRoleIdAndMenuId(MenuByRoleIdAndMenuId viewModel) {
        for (String menuId : viewModel.getMenuIds()) {
            long count = rolePermissionRepository.count(QRolePermission.rolePermission.permissionId.eq(menuId).and(QRolePermission.rolePermission.roleId.eq(viewModel.getRoleId())).and(QRolePermission.rolePermission.roleMenuGroupId.eq(viewModel.getParentId())));
            if (count > 0) {
                continue;
            }
            RolePermission rolePermission = new RolePermission();
            rolePermission.setPermissionId(menuId);
            rolePermission.setRoleMenuGroupId(viewModel.getParentId());
            rolePermission.setRoleId(viewModel.getRoleId());
            rolePermissionRepository.save(rolePermission);
        }
        return true;
    }

    @Override
    public boolean deleteMenuByRoleIdAndMenuIds(MenuByRoleIdAndMenuId viewModel) {
        for (String menuId : viewModel.getMenuIds()) {
            Iterable<RolePermission> all = rolePermissionRepository.findAll(QRolePermission.rolePermission.roleId.eq(viewModel.getRoleId()).and(QRolePermission.rolePermission.permissionId.eq(menuId)).and(QRolePermission.rolePermission.roleMenuGroupId.eq(viewModel.getParentId())));
            rolePermissionRepository.deleteAll(all);
        }
        return true;
    }

    @Override
    public boolean saveRoleMenu(List<RoleGroupViewModel> roleGroupViewModel) {
        List<RoleMenuGroup> collect = roleGroupViewModel.stream().map(x -> RoleViewModelMapper.INSTANCE.roleGroup(x)).collect(Collectors.toList());
        roleGroupRepository.saveAll(collect);
        return true;
    }

    @Override
    public boolean deleteRoleGroupById(String id) {
        long count = rolePermissionRepository.count(QRolePermission.rolePermission.roleMenuGroupId.eq(id));
        if (count > 0) {
            return false;
        } else {
            roleGroupRepository.deleteById(id);
        }
        return true;
    }

    @SuppressWarnings("all")
    @Override
    public List<MenuParentViewModel> findRoleMenuList(String type) {
        User currentUser = SecurityUtils.getSubject().getCurrentUser();
        //得到用户拥有的角色
        List<Role> roleList = userRoleService.getRolesByUserId(currentUser.getId());
        List<RoleViewModel> roleViewModelList = roleList.stream().map(role -> RoleViewModelMapper.INSTANCE.roleToRoleViewModel(role)).collect(Collectors.toList());
        List<String> collect = roleViewModelList.stream().map(x -> x.getId()).collect(Collectors.toList());
        List<MenuParentViewModel> menuParentViewModelByRoleId = userRoleService.findMenuParentViewModelByRoleId(String.valueOf(collect));

        Object statisticsViewModel = null;
//        if ("1".equals(type)) {
//            if (PermissionEnum.SYSTEM_TYPE.任免系统.syscode.equals(systemType)) {
//                statisticsViewModel = projectService.findCadreStatistics();
//            } else {
//                statisticsViewModel = panelMessageService.panelMessageViewModel();
//            }
//        }
        if (menuParentViewModelByRoleId != null && menuParentViewModelByRoleId.size() > 0) {
            for (MenuParentViewModel menuParentViewModel : menuParentViewModelByRoleId) {
                List<MenuChildViewModel> menuChildViewModelByRoleIdAndMenuGroupId = userRoleService.findMenuChildViewModelByRoleIdAndMenuGroupId(collect, menuParentViewModel.getId());
                if (menuChildViewModelByRoleIdAndMenuGroupId == null || menuChildViewModelByRoleIdAndMenuGroupId.size() == 0) {
                    menuChildViewModelByRoleIdAndMenuGroupId = new ArrayList<>(0);
                }
                Object finalStatisticsViewModel = statisticsViewModel;
                menuChildViewModelByRoleIdAndMenuGroupId.stream().forEach(x -> {
                    x.setParentId(menuParentViewModel.getId());
                    if (!StringUtils.isEmpty(x.getType()) && "1".equals(type)) {
                        Field field = null;
                        try {
                            field = finalStatisticsViewModel.getClass().getDeclaredField(x.getType());
                            boolean accessible = field.isAccessible();
                            field.setAccessible(true);
                            Object o = field.get(finalStatisticsViewModel);
                            if (o == null) {
                                x.setNoDeal(0);
                            } else {
                                int parseInt = Integer.parseInt(String.valueOf(o));
                                x.setNoDeal(parseInt);
                                menuParentViewModel.setNoDeal(menuParentViewModel.getNoDeal() + parseInt);
                            }
                            field.setAccessible(accessible);
                        } catch (Exception e) {
                            //e.printStackTrace();
                        }
                    }
                });
                menuParentViewModel.setChildren(menuChildViewModelByRoleIdAndMenuGroupId);
            }
        } else {
            menuParentViewModelByRoleId = new ArrayList<>(0);
        }
        return menuParentViewModelByRoleId;
    }

    @Override
    public List<RoleViewModel> findAllRole() {
        List<Role> all = Lists.newArrayList(roleRepository.findAll());
        List<RoleViewModel> collect = all.stream().map(x -> RoleViewModelMapper.INSTANCE.roleToRoleViewModel(x)).collect(Collectors.toList());
        collect.stream().forEach(x -> {
            long count = rolePermissionRepository.count(QRolePermission.rolePermission.roleId.eq(x.getId()));
            x.setMenuCount(count);
            long homePageCountNumber = roleHomePageCountRepository.count(QRoleHomePageCount.roleHomePageCount.roleId.eq(x.getId()));
            x.setHomePageCountNumber(homePageCountNumber);
        });
        return collect;
    }

    @Override
    public List<User> findReceivesByOrganizationId(String organizationId, String roleCode) {
        QUser qUser = QUser.user;
        QUserRole qUserRole = QUserRole.userRole;
        QRole qRole = QRole.role;

        BooleanBuilder builder = new BooleanBuilder();
        builder = builder.and(qUser.dataOrganizationId.eq(organizationId))
                .and(qRole.code.eq(roleCode));
        List<User> userList = queryFactory.select(qUser)
                .from(qUser)
                .join(qUserRole).on(qUser.id.eq(qUserRole.userId))
                .join(qRole).on(qRole.id.eq(qUserRole.roleId))
                .where(builder).fetch();

        return userList;
    }
}
