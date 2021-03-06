package com.sinosoft.ops.cimp.mapper.sys.datapermission;

import com.sinosoft.ops.cimp.entity.sys.datapermission.InterfaceType;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.InterfaceTypeAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.InterfaceTypeModifyModel;
import com.sinosoft.ops.cimp.vo.to.sys.datapermission.InterfaceTypeModel;
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
public interface InterfaceTypeMapper {
    InterfaceTypeMapper INSTANCE = Mappers.getMapper(InterfaceTypeMapper.class);

    InterfaceTypeModel interfaceTypeToModel(InterfaceType interfaceType);

    @Mappings({
            @Mapping(source = "createId", target = "createId", qualifiedByName = "getCurrentId"),
            @Mapping(source = "createTime", target = "createTime", qualifiedByName = "getTime"),
    })
    InterfaceType addModelToInterfaceType(InterfaceTypeAddModel addModel);

    @Mappings({
            @Mapping(source = "modifyId", target = "modifyId", qualifiedByName = "getCurrentId"),
            @Mapping(source = "modifyTime", target = "modifyTime", qualifiedByName = "getTime")
    })
    void modifyModelToInterfaceType(InterfaceTypeModifyModel modifyModel, @MappingTarget InterfaceType interfaceType);


    @Named("getCurrentId")
    default String getCurrentId(String id) {
        return SecurityUtils.getSubject().getCurrentUser().getId();
    }

    @Named("getTime")
    default Date getTime(Date time) {
        return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
    }

}
