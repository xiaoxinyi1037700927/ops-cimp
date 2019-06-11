package com.sinosoft.ops.cimp.service.sys.datapermission.impl;

import com.sinosoft.ops.cimp.entity.sys.datapermission.RoleDataPermission;
import com.sinosoft.ops.cimp.repository.sys.datapermission.RoleDataPermissionRepository;
import com.sinosoft.ops.cimp.service.sys.datapermission.RoleDataPermissionService;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.RoleDataPerModifyModel;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RoleDataPermissionServiceImpl implements RoleDataPermissionService {

    private final RoleDataPermissionRepository roleDataPermissionRepository;

    public RoleDataPermissionServiceImpl(RoleDataPermissionRepository roleDataPermissionRepository) {
        this.roleDataPermissionRepository = roleDataPermissionRepository;
    }

    /**
     * 修改角色数据权限
     *
     * @param modifyModel
     */
    @Override
    public void modifyRoleDataPermission(RoleDataPerModifyModel modifyModel) {
        String userId = SecurityUtils.getSubject().getCurrentUser().getId();
        List<RoleDataPermission> pers = roleDataPermissionRepository.findByRoleIdAndInterfaceId(modifyModel.getRoleId(), modifyModel.getInterfaceId());

        RoleDataPermission per = null;
        if (pers.size() > 0) {
            per = pers.get(0);
        } else {
            per = new RoleDataPermission();
            per.setRoleId(modifyModel.getRoleId());
            per.setInterfaceId(modifyModel.getInterfaceId());
            per.setCreateId(userId);
            per.setCreateTime(new Date());
        }

        per.setSql(modifyModel.getSql());
        per.setModifyId(userId);
        per.setModifyTime(new Date());

        roleDataPermissionRepository.save(per);
    }


}
