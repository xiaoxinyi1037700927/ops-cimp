package com.sinosoft.ops.cimp.service.user.impl;


import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sinosoft.ops.cimp.entity.oraganization.QOrganization;
import com.sinosoft.ops.cimp.entity.user.QRole;
import com.sinosoft.ops.cimp.entity.user.Role;
import com.sinosoft.ops.cimp.entity.user.subSelects.QRHPCountCount;
import com.sinosoft.ops.cimp.entity.user.subSelects.QRMGroupCount;
import com.sinosoft.ops.cimp.entity.user.subSelects.QRPPSqlCount;
import com.sinosoft.ops.cimp.entity.user.subSelects.QRPTableCount;
import com.sinosoft.ops.cimp.repository.user.RoleRepository;
import com.sinosoft.ops.cimp.service.user.RoleService;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.vo.from.sys.role.RoleModel;
import com.sinosoft.ops.cimp.vo.to.sys.role.RoleViewModel;
import org.apache.commons.lang3.StringUtils;
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
import java.util.stream.Collectors;

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
    public List<RoleViewModel> findData(String orgId) {
        QRole qRole = QRole.role;
        QRMGroupCount qrmGroupCount = QRMGroupCount.rMGroupCount;
        QRHPCountCount qrhpCountCount = QRHPCountCount.rHPCountCount;
        QRPTableCount qrpTableCount = QRPTableCount.rPTableCount;
        QRPPSqlCount rPPSqlCount = QRPPSqlCount.rPPSqlCount;
        QOrganization qOrganization = QOrganization.organization;

        List<RoleViewModel> results = queryFactory.select(
                Projections.bean(
                        RoleViewModel.class,
                        qRole.id,
                        qRole.name,
                        qRole.code,
                        qRole.parentId,
                        qRole.description,
                        qRole.pageType,
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
                .fetch();

        if (StringUtils.isNotEmpty(orgId)) {
            //账号配置中，如果单位是毕节市区县，排除"管理员"
            String code = queryFactory.select(qOrganization.code).from(qOrganization).where(qOrganization.id.eq(orgId)).fetchOne();
            if (code != null && code.startsWith("001.019")) {
                results = results.stream().filter(model -> !"管理员".equals(model.getName())).collect(Collectors.toList());
            }
        }


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
