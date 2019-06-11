package com.sinosoft.ops.cimp.service.sys.datapermission.impl;

import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.sys.datapermission.Interfaces;
import com.sinosoft.ops.cimp.entity.sys.datapermission.QInterfaces;
import com.sinosoft.ops.cimp.entity.sys.datapermission.RoleDataPermission;
import com.sinosoft.ops.cimp.mapper.sys.datapermission.InterfacesMapper;
import com.sinosoft.ops.cimp.repository.sys.datapermission.InterfacesRepository;
import com.sinosoft.ops.cimp.repository.sys.datapermission.RoleDataPermissionRepository;
import com.sinosoft.ops.cimp.service.sys.datapermission.InterfacesService;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.InterfacesAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.InterfacesDeleteModel;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.InterfacesModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.InterfacesSearchModel;
import com.sinosoft.ops.cimp.vo.to.sys.datapermission.ConfigType;
import com.sinosoft.ops.cimp.vo.to.sys.datapermission.InterfacesModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InterfacesServiceImpl implements InterfacesService {

    private final InterfacesRepository interfacesRepository;
    private final RoleDataPermissionRepository roleDataPermissionRepository;

    public InterfacesServiceImpl(InterfacesRepository interfacesRepository, RoleDataPermissionRepository roleDataPermissionRepository) {
        this.interfacesRepository = interfacesRepository;
        this.roleDataPermissionRepository = roleDataPermissionRepository;
    }

    /**
     * 接口列表
     *
     * @param searchModel
     * @return
     */
    @Override
    public PaginationViewModel<InterfacesModel> listInterfaces(InterfacesSearchModel searchModel) {
        String roleId = searchModel.getRoleId();
        int pageSize = searchModel.getPageSize();
        int pageIndex = searchModel.getPageIndex();
        //是否分页
        boolean isPaging = pageSize > 0 && pageIndex > 0;

        QInterfaces qInterfaces = QInterfaces.interfaces;
        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.isNotEmpty(searchModel.getInterfaceTypeId())) {
            builder = builder.and(qInterfaces.interfaceTypeId.eq(searchModel.getInterfaceTypeId()));
        }
        if (StringUtils.isNotEmpty(searchModel.getName())) {
            builder = builder.and(qInterfaces.name.contains(searchModel.getName()));
        }

        List<Interfaces> ifaceList = null;
        long total = 0;
        if (isPaging) {
            PageRequest pageRequest = PageRequest.of(pageIndex - 1, pageSize);
            Page<Interfaces> page = interfacesRepository.findAll(builder, pageRequest);
            ifaceList = page.getContent();
            total = page.getTotalElements();
        } else {
            Iterable<Interfaces> iterable = interfacesRepository.findAll(builder);
            ifaceList = Lists.newArrayList(iterable);
            total = ifaceList.size();
        }

        return new PaginationViewModel
                .Builder<InterfacesModel>()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .totalCount(total)
                .data(ifaceList.stream().map(iface -> {
                    InterfacesModel model = InterfacesMapper.INSTANCE.interfacesToModel(iface);

                    if (StringUtils.isNotEmpty(roleId)) {
                        List<RoleDataPermission> pers = roleDataPermissionRepository.findByRoleIdAndInterfaceId(roleId, iface.getId());
                        if (pers.size() > 0) {
                            model.setSql(pers.get(0).getSql());
                        }
                    }

                    return model;
                }).collect(Collectors.toList()))
                .build();
    }

    /**
     * 添加接口
     *
     * @param addModel
     */
    @Override
    public void addInterfaces(InterfacesAddModel addModel) {
        interfacesRepository.save(InterfacesMapper.INSTANCE.addModelToInterfaces(addModel));
    }

    /**
     * 修改接口
     *
     * @param modifyModel
     */
    @Override
    public boolean modifyInterfaces(InterfacesModifyModel modifyModel) {
        Optional<Interfaces> optional = interfacesRepository.findById(modifyModel.getId());
        if (!optional.isPresent()) {
            return false;
        }

        Interfaces interfaces = optional.get();
        InterfacesMapper.INSTANCE.modifyModelToInterfaces(modifyModel, interfaces);
        interfacesRepository.save(interfaces);

        return true;
    }

    /**
     * 删除接口
     *
     * @param deleteModel
     */
    @Override
    public void deleteInterfaces(InterfacesDeleteModel deleteModel) {
        List<String> ids = deleteModel.getIds();
        if (ids == null || ids.size() == 0) {
            return;
        }

        for (String id : ids) {
            interfacesRepository.deleteById(id);
        }
    }

    /**
     * 获取配置类型
     *
     * @return
     */
    @Override
    public List<String> getConfigType() {
        return ConfigType.getConfigTypes();
    }
}
