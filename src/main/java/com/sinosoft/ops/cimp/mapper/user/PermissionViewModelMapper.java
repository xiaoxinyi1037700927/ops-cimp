package com.sinosoft.ops.cimp.mapper.user;

import com.sinosoft.ops.cimp.entity.sys.user.Permission ;
import com.sinosoft.ops.cimp.entity.sys.user.PermissionPage ;
import com.sinosoft.ops.cimp.entity.sys.user.PermissionPageOperation ;
import com.sinosoft.ops.cimp.vo.user.PermissionViewModel ;
import com.sinosoft.ops.cimp.vo.user.PermissionPageOperationVO;
import com.sinosoft.ops.cimp.vo.user.PermissionPageVO ;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PermissionViewModelMapper {

    PermissionViewModelMapper INSTANCE = Mappers.getMapper(PermissionViewModelMapper.class);

    PermissionViewModel permissionToPermissionViewModel(Permission permission);

    PermissionPage permissionPageToEntity(PermissionPageVO permissionPageVO);

    PermissionPageVO entityToPermissionPage(PermissionPage permissionPage);

    PermissionPageOperation permissionPageOperationToEntity(PermissionPageOperationVO operationVO);

    PermissionPageOperationVO entityToPermissionPageOperation(PermissionPageOperation permissionPageOperation);

}
