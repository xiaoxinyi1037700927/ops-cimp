package com.sinosoft.ops.cimp.mapper.sys.sysapp.access;

import com.sinosoft.ops.cimp.entity.sys.sysapp.fieldAccess.SysAppRoleTableAccess;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.access.SysAppTableAccessAddModel;
import com.sinosoft.ops.cimp.vo.to.sys.sysapp.access.SysAppTableAccessModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Mapper
public interface SysAppTableAccessMapper {

    SysAppTableAccessMapper INSTANCE = Mappers.getMapper(SysAppTableAccessMapper.class);

    SysAppTableAccessModel tableAccessToTableAccessModel(SysAppRoleTableAccess tableAccess);

    @Mappings({
            @Mapping(source = "createId", target = "createId", qualifiedByName = "getCurrentId"),
            @Mapping(source = "createTime", target = "createTime", qualifiedByName = "getTime"),
    })
    SysAppRoleTableAccess addModelToTableAccess(SysAppTableAccessAddModel addModel);


    @Named("getCurrentId")
    default String getCurrentId(String id) {
//        return SecurityUtils.getSubject().getCurrentUser().getId();
        return "0";
    }

    @Named("getTime")
    default Date getTime(Date time) {
        return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
    }

}
