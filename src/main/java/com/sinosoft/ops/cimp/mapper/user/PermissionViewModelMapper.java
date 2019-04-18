package com.sinosoft.ops.cimp.mapper.user;

import com.sinosoft.ops.cimp.entity.sys.permission.Permission;
import com.sinosoft.ops.cimp.entity.sys.permission.PermissionPage;
import com.sinosoft.ops.cimp.entity.sys.permission.PermissionPageOperation;
import com.sinosoft.ops.cimp.vo.to.sys.permission.PermissionPageOperationVO;
import com.sinosoft.ops.cimp.vo.to.sys.permission.PermissionPageVO;
import com.sinosoft.ops.cimp.vo.to.sys.permission.PermissionViewModel;
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
