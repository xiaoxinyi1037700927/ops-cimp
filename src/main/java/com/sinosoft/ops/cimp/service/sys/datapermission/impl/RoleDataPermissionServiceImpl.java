package com.sinosoft.ops.cimp.service.sys.datapermission.impl;

import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.sys.datapermission.QRoleDataPermission;
import com.sinosoft.ops.cimp.entity.sys.datapermission.RoleDataPermission;
import com.sinosoft.ops.cimp.mapper.sys.datapermission.RoleDataPermissionMapper;
import com.sinosoft.ops.cimp.repository.sys.datapermission.RoleDataPermissionRepository;
import com.sinosoft.ops.cimp.service.sys.datapermission.RoleDataPermissionService;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.RoleDataPerAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.RoleDataPerDeleteModel;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.RoleDataPerModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.RoleDataPerSearchModel;
import com.sinosoft.ops.cimp.vo.to.sys.datapermission.RoleDataPerModel;
import com.sinosoft.ops.cimp.vo.to.sys.datapermission.RoleDataPerSqlType;
import com.sinosoft.ops.cimp.vo.to.sys.datapermission.SqlTypeModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleDataPermissionServiceImpl implements RoleDataPermissionService {

    private final RoleDataPermissionRepository roleDataPermissionRepository;

    public RoleDataPermissionServiceImpl(RoleDataPermissionRepository roleDataPermissionRepository) {
        this.roleDataPermissionRepository = roleDataPermissionRepository;
    }

    /**
     * 角色数据权限列表
     *
     * @param searchModel
     * @return
     */
    @Override
    public PaginationViewModel<RoleDataPerModel> listRoleDataPermission(RoleDataPerSearchModel searchModel) {
        int pageSize = searchModel.getPageSize();
        int pageIndex = searchModel.getPageIndex();
        //是否分页
        boolean isPaging = pageSize > 0 && pageIndex > 0;

        QRoleDataPermission qRoleDataPermission = QRoleDataPermission.roleDataPermission;
        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.isNotEmpty(searchModel.getName())) {
            builder = builder.and(qRoleDataPermission.name.contains(searchModel.getName()));
        }

        List<RoleDataPermission> perList = null;
        long total = 0;
        if (isPaging) {
            PageRequest pageRequest = PageRequest.of(pageIndex - 1, pageSize);
            Page<RoleDataPermission> page = roleDataPermissionRepository.findAll(builder, pageRequest);
            perList = page.getContent();
            total = page.getTotalElements();
        } else {
            Iterable<RoleDataPermission> iterable = roleDataPermissionRepository.findAll(builder);
            perList = Lists.newArrayList(iterable);
            total = perList.size();
        }

        return new PaginationViewModel
                .Builder<RoleDataPerModel>()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .totalCount(total)
                .data(perList.stream().map(per -> {
                    RoleDataPerModel model = RoleDataPermissionMapper.INSTANCE.roleDataPermissionToModel(per);
                    model.setTypeName(RoleDataPerSqlType.getName(model.getType()));
                    return model;
                }).collect(Collectors.toList()))
                .build();
    }

    /**
     * 添加角色数据权限
     *
     * @param addModel
     */
    @Override
    public void addRoleDataPermission(RoleDataPerAddModel addModel) {
        roleDataPermissionRepository.save(RoleDataPermissionMapper.INSTANCE.addModelToRoleDataPermission(addModel));
    }

    /**
     * 修改角色数据权限
     *
     * @param modifyModel
     */
    @Override
    public boolean modifyRoleDataPermission(RoleDataPerModifyModel modifyModel) {
        Optional<RoleDataPermission> optional = roleDataPermissionRepository.findById(modifyModel.getId());
        if (!optional.isPresent()) {
            return false;
        }

        RoleDataPermission roleDataPermission = optional.get();
        RoleDataPermissionMapper.INSTANCE.modifyModelToRoleDataPermission(modifyModel, roleDataPermission);
        roleDataPermissionRepository.save(roleDataPermission);

        return true;
    }

    /**
     * 删除角色数据权限
     *
     * @param deleteModel
     */
    @Override
    public void deleteRoleDataPermission(RoleDataPerDeleteModel deleteModel) {
        List<String> ids = deleteModel.getIds();
        if (ids == null || ids.size() == 0) {
            return;
        }

        for (String id : ids) {
            roleDataPermissionRepository.deleteById(id);
        }
    }

    /**
     * 获取sql类型
     *
     * @return
     */
    @Override
    public List<SqlTypeModel> getSqlTypes() {
        return Arrays.stream(RoleDataPerSqlType.values()).map(sqlType -> {
            SqlTypeModel model = new SqlTypeModel();
            model.setId(sqlType.getType());
            model.setName(sqlType.getName());
            return model;
        }).collect(Collectors.toList());
    }

    /**
     * 获取角色的数据权限sql
     *
     * @param roleIds
     * @param type
     * @return
     */
    @Override
    public List<String> getSqls(List<String> roleIds, String type) {
        QRoleDataPermission qRoleDataPermission = QRoleDataPermission.roleDataPermission;
        Iterable<RoleDataPermission> iterable = roleDataPermissionRepository.findAll(qRoleDataPermission.type.eq(type).and(qRoleDataPermission.roleId.in(roleIds)));

        return Lists.newArrayList(iterable).stream().map(RoleDataPermission::getSql).collect(Collectors.toList());
    }
}
