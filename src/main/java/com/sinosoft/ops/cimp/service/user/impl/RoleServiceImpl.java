package com.sinosoft.ops.cimp.service.user.impl;


import com.sinosoft.ops.cimp.entity.user.QRole;
import com.sinosoft.ops.cimp.entity.user.Role;
import com.sinosoft.ops.cimp.repository.user.RoleRepository;
import com.sinosoft.ops.cimp.service.user.RoleService;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.vo.from.sys.role.RoleModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Iterable<Role> findData() {
        QRole qRole = QRole.role;
//        BooleanExpression condition = qRole.id.isNotNull().and(qRole.systemType.eq(roleModel.getSystemType()));
//        if (!StringUtils.isEmpty(roleModel.getName())) {
//            condition = condition.and(qRole.name.like("%" + roleModel.getName() + "%"));
//        }
//        if (!StringUtils.isEmpty(roleModel.getDescription())) {
//            condition = condition.and(qRole.description.like("%" + roleModel.getDescription() + "%"));
//        }
        Iterable<Role> roleLst = roleRepository.findAll();
        return roleLst;
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
