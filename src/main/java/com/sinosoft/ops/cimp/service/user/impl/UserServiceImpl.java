package com.sinosoft.ops.cimp.service.user.impl;

import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.oraganization.Organization;
import com.sinosoft.ops.cimp.entity.oraganization.QOrganization;
import com.sinosoft.ops.cimp.entity.user.*;
import com.sinosoft.ops.cimp.repository.user.RoleRepository;
import com.sinosoft.ops.cimp.repository.user.UserRepository;
import com.sinosoft.ops.cimp.repository.user.UserRoleRepository;
import com.sinosoft.ops.cimp.service.user.UserService;
import com.sinosoft.ops.cimp.util.CachePackage.OrganizationCacheManager;
import com.sinosoft.ops.cimp.util.PasswordEncoderHelper;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.vo.from.user.UserModifyContactVO;
import com.sinosoft.ops.cimp.vo.from.user.UserModifyPasswordVO;
import com.sinosoft.ops.cimp.vo.from.user.UserSearchViewModel;
import com.sinosoft.ops.cimp.vo.to.user.UserViewModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @PersistenceContext
    private final EntityManager entityManager;
    private JPAQueryFactory queryFactory;

    @Autowired
    public UserServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @PostConstruct
    public void init() {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public PaginationViewModel<UserViewModel> findByPageData(UserSearchViewModel userSearchViewModel) {
        int pageIndex = userSearchViewModel.getPageIndex();
        int pageSize = userSearchViewModel.getPageSize();
        if (pageIndex <= 0) pageIndex = 1;
        if (pageSize <= 0) pageSize = 10;
        QUser qUser = QUser.user;
        QUserRole qUserRole = QUserRole.userRole;
        QRole qRole = QRole.role;
        QOrganization qOrganization = QOrganization.organization;

        BooleanBuilder builder = new BooleanBuilder();

        //关联机构树查询，如果机构code变了，用户存的code也应该变
        if (StringUtils.isNotBlank(userSearchViewModel.getOrganizationCode())) {
            if ("1".equals(userSearchViewModel.getContain())) {
                //builder = builder.and(qUser.organizationCode.startsWith(userSearchViewModel.getOrganizationCode()));
                builder = builder.and(qOrganization.code.startsWith(userSearchViewModel.getOrganizationCode()));
            } else {
                //builder = builder.and(qUser.organizationCode.eq(userSearchViewModel.getOrganizationCode()));
                builder = builder.and(qOrganization.code.eq(userSearchViewModel.getOrganizationCode()));
            }
        }
        QueryResults<UserViewModel> queryResults = queryFactory.select(Projections.bean(
                UserViewModel.class,
                qUser.id,
                qUser.name,
                qUser.loginName,
                qUser.organizationId,
                qOrganization.code.as("organizationCode"),
                qUser.dataOrganizationId,
                qRole.id.as("roleId")
        ))
                .from(qUser)
                .leftJoin(qOrganization).on(qOrganization.id.eq(qUser.organizationId))
                .leftJoin(qUserRole).on(qUser.id.eq(qUserRole.userId))
                .leftJoin(qRole).on(qUserRole.roleId.eq(qRole.id))
                .where(builder)
                .orderBy(qUser.dataOrganizationId.asc())
                .offset((pageIndex - 1) * pageSize)
                .limit(pageSize).fetchResults();
        List<UserViewModel> userViewModelList = queryResults.getResults();
        userViewModelList.forEach(userViewModel -> {
            String organizationId = userViewModel.getOrganizationId();
            Organization organization = OrganizationCacheManager.getSubject().getOrganizationById(organizationId);
            if (organization != null) {
                userViewModel.setOrganizationName(organization.getName());
            }
            String dataOrganizationId = userViewModel.getDataOrganizationId();
            Organization dataOrganization = OrganizationCacheManager.getSubject().getOrganizationById(dataOrganizationId);
            if (dataOrganization != null) {
                userViewModel.setDataOrganizationName(dataOrganization.getName());
            }

            String ownerOrganizationId = userViewModel.getOwnerOrganizationId();
            Organization ownerOrganization = OrganizationCacheManager.getSubject().getOrganizationById(ownerOrganizationId);
            if (ownerOrganization != null) {
                userViewModel.setOwnerOrganizationCode(ownerOrganization.getCode());
                userViewModel.setOwnerOrganizationName(ownerOrganization.getName());
            }
            String userId = userViewModel.getId();
            List<Role> roleList = this.findUserRole(userId);
            userViewModel.setRoleList(roleList);
        });

        PaginationViewModel<UserViewModel> page = new PaginationViewModel<UserViewModel>();
        page.setData(userViewModelList);
        page.setPageIndex(pageIndex);
        page.setPageSize(pageSize);
        page.setTotalCount(queryResults.getTotal());
        return page;
    }


    private List<Role> findUserRole(String userId) {
        QRole qRole = QRole.role;
        QUserRole qUserRole = QUserRole.userRole;
        List<Role> roleList = queryFactory.select(qRole).from(qRole)
                .leftJoin(qUserRole).on(qRole.id.eq(qUserRole.roleId))
                .where(qUserRole.userId.eq(userId))
                .fetch();
        return roleList;
    }

//    private List<UserTaskViewModel> userTaskViewModelList(String userId) {
//        QUserTask qUserTask = QUserTask.userTask;
//        QInvestigateTask qInvestigateTask = QInvestigateTask.investigateTask;
//        List<UserTaskViewModel> userTaskViewModelList = queryFactory.select(Projections.bean(
//                UserTaskViewModel.class,
//                qUserTask.userId,
//                qUserTask.taskId,
//                qInvestigateTask.name.as("taskName")
//        ))
//                .from(qUserTask)
//                .leftJoin(qInvestigateTask).on(qUserTask.taskId.eq(qInvestigateTask.id))
//                .where(qUserTask.userId.eq(userId)).fetch();
//        return userTaskViewModelList;
//    }


    @Override
    @Transactional
    public boolean modifyUser(User user, List<String>... roleList) {
        QUser qUser = QUser.user;
        List<User> userList = (List<User>) userRepository.findAll(qUser.loginName.equalsIgnoreCase(user.getLoginName()).and(qUser.id.ne(user.getId())));
        if (userList.size() > 0) return false;


        user.setLocked(false);
        userRepository.save(user);
        if (roleList != null && roleList.length > 0) {
            List<String> roleCodeList = roleList[0];
            if (roleCodeList != null && roleCodeList.size() > 0) {
                //先删除原来的角色
                userRoleRepository.deleteByUserId(user.getId());
                //在增加新角色
                List<Role> roleList2 = roleRepository.findByIdIn(roleCodeList);
                addUserRole(user, roleList2);
            }
        }
        return true;
    }

    @Override
    public UserModifyContactVO modifyUserContact(UserModifyContactVO contactVO) {
        User user = userRepository.findById(contactVO.getId()).get();
        user.setName(contactVO.getName());
        user.setMobilePhone(contactVO.getMobilePhone());
        user.setTelePhone(contactVO.getTelePhone());

        User currentUser = SecurityUtils.getSubject().getCurrentUser();
        user.setModifyId(currentUser.getId());
        user.setModifyTime(new Date());

        User save = userRepository.save(user);

        //返回
        UserModifyContactVO returnVo = new UserModifyContactVO();
        BeanUtils.copyProperties(save, returnVo);
        return returnVo;
    }

    @Override
    @Transactional
    public String modifyUserPassword(UserModifyPasswordVO passwordVO) {
        //0修改成功；1未找到当前用户；2原始密码不正确
        Optional<User> userOptional = userRepository.findById(passwordVO.getId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String loginPassword = user.getLoginPassword();
            String nowPassword = PasswordEncoderHelper.decryptByDES(loginPassword);
            if (passwordVO.getOldPassword().equals(nowPassword)) {
                String newPassword = PasswordEncoderHelper.encode(passwordVO.getNewPassword());
                user.setLoginPassword(newPassword);
                userRepository.save(user);
            } else {
                return "2";
            }
        } else {
            return "1";
        }
        return "0";
    }

    @Override
    @Transactional
    public boolean addUser(User user, List<String> roleCodeList) {
        User addUser = userRepository.save(user);
        addUser.setLocked(false);
        List<Role> roleList = Lists.newArrayList(roleRepository.findAll(QRole.role.id.in(roleCodeList)));
        addUserRole(user, roleList);
        return true;
    }


    @Override
    public boolean checkIsExist(String loginName) {
        QUser qUser = QUser.user;
        List<User> userList = (List<User>) userRepository.findAll(qUser.loginName.equalsIgnoreCase(loginName));
        if (userList.size() > 0) return false;
        return true;
    }

    private void addUserRole(User user, List<Role> roleList) {
        List<UserRole> userRoleList = new ArrayList<>();
        for (Role role : roleList) {
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(role.getId());
            userRole.setRoleCode(role.getCode());
            userRole.setCreateId(SecurityUtils.getSubject().getCurrentUser().getId());
            userRole.setCreateTime(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            userRoleList.add(userRole);
        }
        userRoleRepository.saveAll(userRoleList);
    }

    @Override
    public User getById(String userId) {
        Optional<User> optionals = userRepository.findById(userId);
        User user = null;
        if (optionals.isPresent()) {
            user = optionals.get();
        }
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public User findByLoginName(String loginName) {
        Objects.requireNonNull(loginName);
        Iterator<User> alluser = userRepository.findAll(QUser.user.loginName.equalsIgnoreCase(loginName)).iterator();
        if (alluser.hasNext()) {
            return alluser.next();
        }
        return null;
    }

    @Override
    @Transactional
    public boolean deleteById(String userId) {
        userRoleRepository.deleteByUserId(userId);
        userRepository.deleteById(userId);
        return true;
    }

    @Override
    public List<String> findSameOrgId() {
        User currentUser = SecurityUtils.getSubject().getCurrentUser();
        ArrayList<User> users = Lists.newArrayList(userRepository.findAll(QUser.user.organizationId.eq(currentUser.getOrganizationId())));
        List<String> collect = users.stream().map(x -> x.getId()).collect(Collectors.toList());
        return collect;
    }

    @Override
    public boolean isHaveChildrenOrg(String orgId) {
        if (StringUtils.isEmpty(orgId)) {
            return false;
        }
        List<Organization> allList = OrganizationCacheManager.getSubject().getAllList();
        long count = allList.stream().filter(x -> orgId.equals(x.getParentId())).count();
        if (count > 0) {
            return true;
        }
        return false;
    }



    @Override
    @Transactional
    public boolean resetPassword(String userId, String password) {
        Optional<User> options = userRepository.findById(userId);
        if (options.isPresent()) {
            User user = options.get();
            String encode = PasswordEncoderHelper.encode(password);
            user.setLoginPassword(encode);
        }
        return true;
    }
}
