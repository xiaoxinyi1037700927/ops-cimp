package com.sinosoft.ops.cimp.mapper.user;

import com.sinosoft.ops.cimp.entity.user.RolePermissionPageSql;
import com.sinosoft.ops.cimp.vo.from.user.rolePermissionPageSql.RPPageSqlAddModel;
import com.sinosoft.ops.cimp.vo.to.user.rolePermissionPageSql.RPPageSqlViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RolePermissionPageSqlMapper {
    RolePermissionPageSqlMapper INSTANCE = Mappers.getMapper(RolePermissionPageSqlMapper.class);

    RPPageSqlViewModel rPPageSqlToViewModel(RolePermissionPageSql rolePermissionPageSql);

    RolePermissionPageSql viewModelToRPPageSql(RPPageSqlAddModel addModel);

    void modifyToRolePermissionPageSql(RPPageSqlViewModel rpPageSqlViewModel, @MappingTarget RolePermissionPageSql rolePermissionPageSql );


}
