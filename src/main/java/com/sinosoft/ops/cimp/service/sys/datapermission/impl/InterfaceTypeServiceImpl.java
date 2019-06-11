package com.sinosoft.ops.cimp.service.sys.datapermission.impl;

import com.sinosoft.ops.cimp.entity.sys.datapermission.InterfaceType;
import com.sinosoft.ops.cimp.mapper.sys.datapermission.InterfaceTypeMapper;
import com.sinosoft.ops.cimp.repository.sys.datapermission.InterfaceTypeRepository;
import com.sinosoft.ops.cimp.service.sys.datapermission.InterfaceTypeService;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.InterfaceTypeAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.InterfaceTypeDeleteModel;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.InterfaceTypeModifyModel;
import com.sinosoft.ops.cimp.vo.to.sys.datapermission.InterfaceTypeModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InterfaceTypeServiceImpl implements InterfaceTypeService {

    private final InterfaceTypeRepository interfaceTypeRepository;

    public InterfaceTypeServiceImpl(InterfaceTypeRepository interfaceTypeRepository) {
        this.interfaceTypeRepository = interfaceTypeRepository;
    }


    /**
     * 角色数据权限列表
     *
     * @return
     */
    @Override
    public List<InterfaceTypeModel> listInterfaceType() {
        return interfaceTypeRepository.findAll().stream()
                .map(InterfaceTypeMapper.INSTANCE::interfaceTypeToModel)
                .collect(Collectors.toList());
    }

    /**
     * 添加角色数据权限
     *
     * @param addModel
     */
    @Override
    public void addInterfaceType(InterfaceTypeAddModel addModel) {
        interfaceTypeRepository.save(InterfaceTypeMapper.INSTANCE.addModelToInterfaceType(addModel));
    }

    /**
     * 修改角色数据权限
     *
     * @param modifyModel
     */
    @Override
    public boolean modifyInterfaceType(InterfaceTypeModifyModel modifyModel) {
        Optional<InterfaceType> optional = interfaceTypeRepository.findById(modifyModel.getId());
        if (!optional.isPresent()) {
            return false;
        }

        InterfaceType interfaceType = optional.get();
        InterfaceTypeMapper.INSTANCE.modifyModelToInterfaceType(modifyModel, interfaceType);
        interfaceTypeRepository.save(interfaceType);

        return true;
    }

    /**
     * 删除角色数据权限
     *
     * @param deleteModel
     */
    @Override
    public void deleteInterfaceType(InterfaceTypeDeleteModel deleteModel) {
        List<String> ids = deleteModel.getIds();
        if (ids == null || ids.size() == 0) {
            return;
        }

        for (String id : ids) {
            interfaceTypeRepository.deleteById(id);
        }
    }
}
