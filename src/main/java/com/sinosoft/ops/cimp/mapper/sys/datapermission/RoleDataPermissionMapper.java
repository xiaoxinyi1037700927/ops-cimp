package com.sinosoft.ops.cimp.mapper.sys.datapermission;

import com.sinosoft.ops.cimp.entity.sys.datapermission.RoleDataPermission;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.RoleDataPerAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.RoleDataPerModifyModel;
import com.sinosoft.ops.cimp.vo.to.sys.datapermission.RoleDataPerModel;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by Jay on 2019/2/22.
 *
 * @author Jay
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinoSoft All Rights Received
 */
@Mapper
public interface RoleDataPermissionMapper {
    RoleDataPermissionMapper INSTANCE = Mappers.getMapper(RoleDataPermissionMapper.class);

    RoleDataPerModel roleDataPermissionToModel(RoleDataPermission roleDataPermission);

    @Mappings({
            @Mapping(source = "createId", target = "createId", qualifiedByName = "getCurrentId"),
            @Mapping(source = "createTime", target = "createTime", qualifiedByName = "getTime"),
    })
    RoleDataPermission addModelToRoleDataPermission(RoleDataPerAddModel addModel);

    @Mappings({
            @Mapping(source = "modifyId", target = "modifyId", qualifiedByName = "getCurrentId"),
            @Mapping(source = "modifyTime", target = "modifyTime", qualifiedByName = "getTime")
    })
    void modifyModelToRoleDataPermission(RoleDataPerModifyModel modifyModel, @MappingTarget RoleDataPermission roleDataPermission);


    @Named("getCurrentId")
    default String getCurrentId(String id) {
        return SecurityUtils.getSubject().getCurrentUser().getId();
    }

    @Named("getTime")
    default Date getTime(Date time) {
        return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
    }

}
