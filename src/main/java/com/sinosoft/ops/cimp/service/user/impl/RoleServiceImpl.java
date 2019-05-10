package com.sinosoft.ops.cimp.service.user.impl;


import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sinosoft.ops.cimp.entity.user.QRole;
import com.sinosoft.ops.cimp.entity.user.QRolePermissionPageSql;
import com.sinosoft.ops.cimp.entity.user.Role;
import com.sinosoft.ops.cimp.entity.user.subSelects.*;
import com.sinosoft.ops.cimp.repository.user.RoleRepository;
import com.sinosoft.ops.cimp.service.user.RoleService;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.vo.from.sys.role.RoleModel;
import com.sinosoft.ops.cimp.vo.to.sys.role.RoleViewModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @PersistenceContext
    private EntityManager entityManager;
    private JPAQueryFactory queryFactory;

    @PostConstruct
    public void getFactory() {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<RoleViewModel> findData() {
        QRole qRole = QRole.role;
        QRMGroupCount qrmGroupCount = QRMGroupCount.rMGroupCount;
        QRHPCountCount qrhpCountCount = QRHPCountCount.rHPCountCount;
        QRPTableCount qrpTableCount = QRPTableCount.rPTableCount;
        QRPPSqlCount rPPSqlCount = QRPPSqlCount.rPPSqlCount;

        QueryResults<RoleViewModel> queryResult = queryFactory.select(
                Projections.bean(
                        RoleViewModel.class,
                        qRole.id,
                        qRole.name,
                        qRole.code,
                        qRole.parentId,
                        qRole.description,
                        qrmGroupCount.tableCount.as("menuCount"),
                        qrhpCountCount.tableCount.as("homePageCountNumber"),
                        qrpTableCount.tableCount.as("rPTableCount"),
                        rPPSqlCount.tableCount.as("roleSqlCount")
                )).from(qRole)
                .leftJoin(qrmGroupCount).on(qRole.id.eq(qrmGroupCount.roleId))
                .leftJoin(qrhpCountCount).on(qRole.id.eq(qrhpCountCount.roleId))
                .leftJoin(qrpTableCount).on(qRole.id.eq(qrpTableCount.roleId))
                .leftJoin(rPPSqlCount).on(qRole.id.eq(rPPSqlCount.roleId))
                .orderBy(qRole.createTime.desc())
                .fetchResults();

        List<RoleViewModel> results = queryResult.getResults();
//        BooleanExpression condition = qRole.id.isNotNull().and(qRole.systemType.eq(roleModel.getSystemType()));
//        if (!StringUtils.isEmpty(roleModel.getName())) {
//            condition = condition.and(qRole.name.like("%" + roleModel.getName() + "%"));
//        }
//        if (!StringUtils.isEmpty(roleModel.getDescription())) {
//            condition = condition.and(qRole.description.like("%" + roleModel.getDescription() + "%"));
//        }
//        Iterable<Role> roleLst = roleRepository.findAll();
        return results;
    }



    @Override
    public Role getById(String roleId) {
        Optional<Role> options = roleRepository.findById(roleId);
        if (options.isPresent()) {
            Role role = options.get();
            return role;
        }
        return null;
    }

    @Override
    @Transactional
    public Role save(RoleModel roleModel) {
        Role role = new Role();
        BeanUtils.copyProperties(roleModel, role);
        role.setCreateId(SecurityUtils.getSubject().getCurrentUser().getId());  //获得当前登录用户ID
        role.setCreateTime(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public Role update(RoleModel roleModel) {
        String roleId = roleModel.getId();
        Optional<Role> options = roleRepository.findById(roleId);
        if (options.isPresent()) {
            Role role = options.get();
            BeanUtils.copyProperties(roleModel, role);
            role.setModifyId(SecurityUtils.getSubject().getCurrentUser().getId()); //获取当前登录用户ID
            role.setModifyTime(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            return role;
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteById(String roleId) {
        roleRepository.deleteById(roleId);
    }

    @Override
    @Transactional
    public void addRelation(String roleId, String permissionId) {

    }

    @Override
    @Transactional
    public void deleteRelation(String roleId, String permissionId) {

    }

}
