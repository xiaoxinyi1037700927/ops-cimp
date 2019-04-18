package com.sinosoft.ops.cimp.mapper.user;

import com.sinosoft.ops.cimp.entity.user.Role;
import com.sinosoft.ops.cimp.entity.user.RoleMenuGroup;
import com.sinosoft.ops.cimp.vo.to.sys.role.RoleGroupViewModel;
import com.sinosoft.ops.cimp.vo.to.sys.role.RoleViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleViewModelMapper {

    RoleViewModelMapper INSTANCE = Mappers.getMapper(RoleViewModelMapper.class);

    RoleViewModel roleToRoleViewModel(Role role);

    RoleMenuGroup roleGroup(RoleGroupViewModel roleGroupViewModel);
}
