package com.sinosoft.ops.cimp.service.impl.user;

import com.google.common.collect.Lists;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import com.sinosoft.ops.cimp.entity.sys.user.*;
import com.sinosoft.ops.cimp.mapper.user.MenuMapper;
import com.sinosoft.ops.cimp.repository.user.*;
import com.sinosoft.ops.cimp.service.user.PermissionService;
import com.sinosoft.ops.cimp.vo.to.user.AddMenuToGroupViewModel;
import com.sinosoft.ops.cimp.vo.to.user.SaveMenuGroupSortViewModel;
import com.sinosoft.ops.cimp.vo.user.PermissionModel;
import com.sinosoft.ops.cimp.vo.user.RoleEnum;
import com.sinosoft.ops.cimp.vo.user.RoleViewModel;
import com.sinosoft.ops.cimp.vo.user.role.MenuChildViewModel;
import com.sinosoft.ops.cimp.vo.user.role.MenuParentViewModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private MenuGroupPerRelRepository menuGroupPerRelRepository;
    @Autowired
    private RolePermissionRepository rolePermissionRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
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
    public Iterable<Permission> findData(PermissionModel permissionModel) {
        BooleanExpression condition = QPermission.permission.id.isNotNull();
        if (!StringUtils.isEmpty(permissionModel.getName())) {
            condition = condition.and(QPermission.permission.name.like("%" + permissionModel.getName() + "%"));
        }
        if (!StringUtils.isEmpty(permissionModel.getDescription())) {
            condition = condition.and(QPermission.permission.description.like("%" + permissionModel.getDescription() + "%"));
        }
        Iterable<Permission> rightLst = permissionRepository.findAll(condition, new Sort(Sort.Direction.ASC, QPermission.permission.createTime.getMetadata().getName()));
        return rightLst;
    }

    @Override
    public Permission getById(String permissionId) {
        Optional<Permission> options = permissionRepository.findById(permissionId);
        if (options.isPresent()) {
            Permission permission = options.get();
            return permission;
        }
        return null;
    }

    @Override
    @Transactional
    public Permission save(PermissionModel permissionModel) {
        Permission permission = new Permission();
        BeanUtils.copyProperties(permissionModel, permission);
        permission.setCreateId("111"); //获得当前登录ID
        permission.setCreateTime(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        return permissionRepository.save(permission);
    }

    @Override
    @Transactional
    public Permission update(PermissionModel permissionModel) {
        String permissionId = permissionModel.getId();
        Optional<Permission> options = permissionRepository.findById(permissionId);
        if (options.isPresent()) {
            Permission permission = options.get();
            BeanUtils.copyProperties(permissionModel, permission);
            permission.setModifyId("111"); //获得当前登录ID
            permission.setModifyTime(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            return permission;
        }
        return null;
    }

    @Override
    public void deleteById(String rightId) {

    }


    @Override
    public boolean saveMenuParent(MenuParentViewModel menuParentViewModel) {
        MenuGroup menuGroup = MenuMapper.INSTANCE.menuParentViewModelToEntity(menuParentViewModel);
        menuGroupRepository.save(menuGroup);
        return true;
    }

    @Override
    public boolean deleteMenuParentById(String id) {
        //查询是否存在子集
        long count = menuGroupPerRelRepository.count(QPermissionMenuGroupRel.permissionMenuGroupRel.menuGroupId.eq(id));
        if (count > 0) {
            return false;
        }
        menuGroupRepository.deleteById(id);
        return true;

    }

    @Override
    public List<MenuChildViewModel> menuListByParentId(String parentId) {
        JPAQuery<Permission> where = queryFactory.select(QPermission.permission).from(QPermissionMenuGroupRel.permissionMenuGroupRel)
                .leftJoin(QPermission.permission).on(QPermission.permission.id.eq(QPermissionMenuGroupRel.permissionMenuGroupRel.permissionId))
                .where(QPermissionMenuGroupRel.permissionMenuGroupRel.menuGroupId.eq(parentId));
        List<Permission> fetch = Lists.newArrayList(where.orderBy(QPermissionMenuGroupRel.permissionMenuGroupRel.sortNumber.asc()).fetch());
        List<MenuChildViewModel> collect = fetch.stream().map(x -> MenuMapper.INSTANCE.entityToMenuChildViewModel(x)).collect(Collectors.toList());
        if (collect != null) {
            collect.stream().forEach(x -> x.setParentId(parentId));
        }
        return collect;
    }

    @Override
    public boolean deleteMenuByParentIdAndMenuId(String parentId, String menuId) {
        long count = rolePermissionRepository.count(QRolePermission.rolePermission.permissionId.eq(menuId).and(QRolePermission.rolePermission.roleMenuGroupId.eq(parentId)));
        if (count > 0) {
            return false;
        }
        JPAQuery<PermissionMenuGroupRel> where = queryFactory.select(QPermissionMenuGroupRel.permissionMenuGroupRel).from(QPermissionMenuGroupRel.permissionMenuGroupRel)
                .leftJoin(QPermission.permission).on(QPermission.permission.id.eq(QPermissionMenuGroupRel.permissionMenuGroupRel.permissionId))
                .where(QPermissionMenuGroupRel.permissionMenuGroupRel.permissionId.eq(menuId).and(QPermissionMenuGroupRel.permissionMenuGroupRel.menuGroupId.eq(parentId)));
        ArrayList<PermissionMenuGroupRel> permissionMenuGroupRels = Lists.newArrayList(where.fetch());
        menuGroupPerRelRepository.deleteAll(permissionMenuGroupRels);
        return true;
    }

    @Override
    public boolean deleteMenuByParentIdAndMenuId(AddMenuToGroupViewModel viewModel) {
        return false;
    }

    @Override
    public List<MenuChildViewModel> findNotHaveMenuListByParentId(String parentId) {
        return null;
    }


    @Override
    public boolean addMenuByParentIdAndMenuId(String parentId, String menuId) {
        JPAQuery<PermissionMenuGroupRel> where = queryFactory.select(QPermissionMenuGroupRel.permissionMenuGroupRel).from(QPermissionMenuGroupRel.permissionMenuGroupRel)
                .leftJoin(QPermission.permission).on(QPermission.permission.id.eq(QPermissionMenuGroupRel.permissionMenuGroupRel.permissionId))
                .where(QPermissionMenuGroupRel.permissionMenuGroupRel.menuGroupId.eq(parentId).and(QPermissionMenuGroupRel.permissionMenuGroupRel.permissionId.eq(menuId)));
        long count = where.fetchCount();
        if (count > 0) {
            return false;
        }
        PermissionMenuGroupRel permissionMenuGroupRel = new PermissionMenuGroupRel();
        permissionMenuGroupRel.setMenuGroupId(parentId);
        permissionMenuGroupRel.setPermissionId(menuId);
        menuGroupPerRelRepository.save(permissionMenuGroupRel);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)

    public boolean addMenuByParentIdAndMenuId(AddMenuToGroupViewModel viewModel) {
        //拿到该组下最大的排序
        int sort = 0;
        List<PermissionMenuGroupRel> sortNumber = (List) menuGroupPerRelRepository.findAll(QPermissionMenuGroupRel.permissionMenuGroupRel.menuGroupId.eq(viewModel.getParentId()), Sort.by(Sort.Order.desc("sortNumber")));
        if (sortNumber != null && sortNumber.size() > 0) {
            if (sortNumber.get(0).getSortNumber() != null) {
                sort = sortNumber.get(0).getSortNumber();
                sort++;
            }
        }
        for (String menuId : viewModel.getMenuIds()) {
            JPAQuery<PermissionMenuGroupRel> where = queryFactory.select(QPermissionMenuGroupRel.permissionMenuGroupRel).from(QPermissionMenuGroupRel.permissionMenuGroupRel)
                    .leftJoin(QPermission.permission).on(QPermission.permission.id.eq(QPermissionMenuGroupRel.permissionMenuGroupRel.permissionId))
                    .where(QPermissionMenuGroupRel.permissionMenuGroupRel.menuGroupId.eq(viewModel.getParentId()).and(QPermissionMenuGroupRel.permissionMenuGroupRel.permissionId.eq(menuId)));
            long count = where.fetchCount();
            if (count > 0) {
                continue;
            }
            sort++;
            PermissionMenuGroupRel permissionMenuGroupRel = new PermissionMenuGroupRel();
            permissionMenuGroupRel.setMenuGroupId(viewModel.getParentId());
            permissionMenuGroupRel.setPermissionId(menuId);
            permissionMenuGroupRel.setSortNumber(sort);
            menuGroupPerRelRepository.save(permissionMenuGroupRel);
        }
        return true;
    }


    public List<MenuChildViewModel> findAllMenuList() {
        List<Permission> all = Lists.newArrayList(permissionRepository.findAll(Sort.by(Sort.Order.asc("sortNumber"))));
        List<MenuChildViewModel> collect = all.stream().map(x -> MenuMapper.INSTANCE.entityToMenuChildViewModel(x)).collect(Collectors.toList());
        return collect;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteMenuById(String menuId) {
        Iterable<PermissionMenuGroupRel> rels = menuGroupPerRelRepository.findAll(QPermissionMenuGroupRel.permissionMenuGroupRel.permissionId.eq(menuId));
        Iterable<RolePermission> permissions = rolePermissionRepository.findAll(QRolePermission.rolePermission.permissionId.eq(menuId));
        if (rels != null) {
            menuGroupPerRelRepository.deleteAll(rels);
        }
        if (permissions != null) {
            rolePermissionRepository.deleteAll(permissions);
        }
        permissionRepository.deleteById(menuId);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)

    public boolean saveMenu(MenuChildViewModel menuChildViewModel) {
        Permission permission = MenuMapper.INSTANCE.permissionViewModelToEntity(menuChildViewModel);
        List<Permission> sortNumber = permissionRepository.findAll(Sort.by(Sort.Order.desc("sortNumber")));
        if (sortNumber.get(0).getSortNumber() != null) {
            Integer sn = sortNumber.get(0).getSortNumber();
            sn++;
            permission.setSortNumber(sn);
        }
        Permission save = permissionRepository.save(permission);
        if (!StringUtils.isEmpty(menuChildViewModel.getGroupId())) {
            ArrayList<PermissionMenuGroupRel> permissionMenuGroupRels = Lists.newArrayList(menuGroupPerRelRepository.findAll(QPermissionMenuGroupRel.permissionMenuGroupRel.permissionId.eq(save.getId()).and(QPermissionMenuGroupRel.permissionMenuGroupRel.menuGroupId.eq(menuChildViewModel.getGroupId()))));
            if (permissionMenuGroupRels.size() == 0) {
                //新增
                PermissionMenuGroupRel permissionMenuGroupRel = new PermissionMenuGroupRel();
                permissionMenuGroupRel.setPermissionId(save.getId());
                permissionMenuGroupRel.setMenuGroupId(menuChildViewModel.getGroupId());
                menuGroupPerRelRepository.save(permissionMenuGroupRel);
            }
            ArrayList<PermissionMenuGroupRel> permissionMenuGroupRelsDelete = Lists.newArrayList(menuGroupPerRelRepository.findAll(QPermissionMenuGroupRel.permissionMenuGroupRel.permissionId.eq(save.getId()).and(QPermissionMenuGroupRel.permissionMenuGroupRel.menuGroupId.ne(menuChildViewModel.getGroupId()))));
            if (permissionMenuGroupRelsDelete.size() > 0) {
                menuGroupPerRelRepository.deleteAll(permissionMenuGroupRelsDelete);
            }
        } else {
            ArrayList<PermissionMenuGroupRel> permissionMenuGroupRelsDelete = Lists.newArrayList(menuGroupPerRelRepository.findAll(QPermissionMenuGroupRel.permissionMenuGroupRel.permissionId.eq(save.getId())));
            if (permissionMenuGroupRelsDelete.size() > 0) {
                menuGroupPerRelRepository.deleteAll(permissionMenuGroupRelsDelete);
            }
        }
        return true;
    }

    @Override
    public String saveRole(RoleViewModel viewModel) {
        Role role;
//        if (!StringUtils.isEmpty(viewModel.getId())) {
//            long count = roleRepository.countByCodeAndSystemTypeAndIdIsNot(viewModel.getCode(), viewModel.getId());
//            if (count > 0) return "1";
//            //编辑
//            role = roleRepository.findById(viewModel.getId()).get();
//        } else {
//            long count = roleRepository.countByCodeAndSystemType(viewModel.getCode());
//            if (count > 0) return "1";
            role = new Role();


        role.setName(viewModel.getName());
        role.setCode(viewModel.getCode());
        role.setDescription(viewModel.getDescription());
        if (StringUtils.isEmpty(viewModel.getPageType())) {
            viewModel.setPageType(RoleEnum.ROLE_pageType.defaultPage.code);
        }
        role.setPageType(viewModel.getPageType());
        role.setDetailUrl(viewModel.getDetailUrl());
        roleRepository.save(role);
        return "0";
    }

    @Override
    public boolean deleteRoleById(String id) {
        //查看该角色是否已经被人使用
        long count = userRoleRepository.count(QUserRole.userRole.roleId.eq(id));
        if (count > 0) {
            return false;
        }
        //删除角色关系
        Iterable<RolePermission> all = rolePermissionRepository.findAll(QRolePermission.rolePermission.roleId.eq(id));
        if (all != null) {
            rolePermissionRepository.deleteAll(all);
        }
        //删除对应统计配置项
        Iterable<RoleHomePageCount> all2 = roleHomePageCountRepository.findAll(QRoleHomePageCount.roleHomePageCount.roleId.eq(id));
        roleHomePageCountRepository.deleteAll(all2);

        roleRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean saveMenuGroupSort(SaveMenuGroupSortViewModel viewModel) {
        return false;
    }

    @Override
    public boolean saveMenuRoleSort(SaveMenuGroupSortViewModel viewModel) {
        return false;
    }


//    public boolean saveMenuGroupSort(SaveMenuGroupSortViewModel viewModel) {
//        List<PermissionMenuGroupRel> list = new ArrayList<>();
//        if (viewModel.getMenuChildViewModelList() != null) {
//            viewModel.getMenuChildViewModelList().stream().forEach(x -> {
//                List<PermissionMenuGroupRel> all = (List) menuGroupPerRelRepository.findAll(QPermissionMenuGroupRel.permissionMenuGroupRel.permissionId.eq(x.getId()).and(QPermissionMenuGroupRel.permissionMenuGroupRel.menuGroupId.eq(viewModel.getParentId())));
//                if (all != null && all.size() > 0) {
//                    PermissionMenuGroupRel permissionMenuGroupRel = all.get(0);
//                    permissionMenuGroupRel.setSortNumber(x.getSortNumber());
//                    list.add(permissionMenuGroupRel);
//                }
//            });
//            menuGroupPerRelRepository.saveAll(list);
//        }
//        return true;
//    }


//    public boolean saveMenuRoleSort(SaveMenuGroupSortViewModel viewModel) {
//        List<RolePermission> list = new ArrayList<>();
//        if (viewModel.getMenuChildViewModelList() != null) {
//            viewModel.getMenuChildViewModelList().stream().forEach(x -> {
//                List<RolePermission> all = (List) rolePermissionRepository.findAll(QRolePermission.rolePermission.roleId.eq(viewModel.getParentId()).and(QRolePermission.rolePermission.permissionId.eq(x.getId())).and(QRolePermission.rolePermission.roleMenuGroupId.eq(x.getParentId())));
//                if (all != null && all.size() > 0) {
//                    RolePermission rolePermission = all.get(0);
//                    rolePermission.setSortNumber(x.getSortNumber());
//                    list.add(rolePermission);
//                }
//            });
//            rolePermissionRepository.saveAll(list);
//        }
//        return true;
//    }

    @Override
    public boolean doTest() {
        List<RolePermission> list = new ArrayList<>();
        List<RolePermission> all = (List) rolePermissionRepository.findAll(QRolePermission.rolePermission.roleMenuGroupId.isNull());
        all.stream().forEach(rolePermission -> {
            JPAQuery<String> where = queryFactory.select(QPermissionMenuGroupRel.permissionMenuGroupRel.menuGroupId).from(QPermissionMenuGroupRel.permissionMenuGroupRel)
                    .where(QPermissionMenuGroupRel.permissionMenuGroupRel.permissionId.eq(rolePermission.getPermissionId())).distinct();
            List<String> fetch = where.fetch();
            if (fetch != null) {
                fetch.stream().forEach(groupId -> {
                    RolePermission rolePermissionNew = new RolePermission();
                    rolePermissionNew.setRoleMenuGroupId(groupId);
                    rolePermissionNew.setPermissionId(rolePermission.getPermissionId());
                    rolePermissionNew.setRoleId(rolePermission.getRoleId());
                    list.add(rolePermissionNew);
                });
            }
        });
        rolePermissionRepository.saveAll(list);
        return false;
    }
}
