package com.sinosoft.ops.cimp.mapper.user;

import com.sinosoft.ops.cimp.entity.sys.user.Role;
import com.sinosoft.ops.cimp.entity.sys.user.RoleMenuGroup;
import com.sinosoft.ops.cimp.vo.user.RoleGroupViewModel;
import com.sinosoft.ops.cimp.vo.user.RoleViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleViewModelMapper {

    RoleViewModelMapper INSTANCE = Mappers.getMapper(RoleViewModelMapper.class);

    RoleViewModel roleToRoleViewModel(Role role);

    RoleMenuGroup roleGroup(RoleGroupViewModel roleGroupViewModel);
}
