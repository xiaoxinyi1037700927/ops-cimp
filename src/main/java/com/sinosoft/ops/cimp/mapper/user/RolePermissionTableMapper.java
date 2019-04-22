package com.sinosoft.ops.cimp.mapper.user;

import com.sinosoft.ops.cimp.entity.user.RolePermissionTable;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.vo.from.user.rolePermissionTable.RPTableAddModel;
import com.sinosoft.ops.cimp.vo.to.user.rolePermissionTable.RPTableViewModel;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Mapper
public interface RolePermissionTableMapper {
    RolePermissionTableMapper INSTANCE = Mappers.getMapper(RolePermissionTableMapper.class);

    @Mappings({
            @Mapping(source = "createId", target = "createId", qualifiedByName = "getCreateId"),
            @Mapping(source = "createTime", target = "createTime", qualifiedByName ="getCreateTime" )
    })
    RolePermissionTable addModelToRPTableAddModel(RPTableAddModel addModel);


    RPTableViewModel rPTableAddModelToViewModel(RolePermissionTable rolePermissionTable);

    @Mappings({
            @Mapping(source = "modifyId", target = "modifyId", qualifiedByName = "getCreateId"),
            @Mapping(source = "modifyTime", target = "modifyTime", qualifiedByName ="getCreateTime" )
    })
    void modifyRolePermissionTable(RPTableViewModel modifyModel, @MappingTarget RolePermissionTable rolePermissionTable);




    @Named(value = "getCreateId")
    default String getCreateId(String id) {
        return SecurityUtils.getSubject().getCurrentUser().getId();
    }

    @Named(value = "getCreateTime")
    default Date getCreateTime(Date modifyTime) {
        return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
    }

}
