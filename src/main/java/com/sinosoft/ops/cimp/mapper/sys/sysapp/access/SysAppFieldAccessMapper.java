package com.sinosoft.ops.cimp.mapper.sys.sysapp.access;

import com.sinosoft.ops.cimp.entity.sys.sysapp.fieldAccess.SysAppRoleFieldAccess;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.access.SysAppFieldAccessAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.access.SysAppFieldAccessModifyModel;
import com.sinosoft.ops.cimp.vo.to.sys.sysapp.access.SysAppFieldAccessModel;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Mapper
public interface SysAppFieldAccessMapper {

    SysAppFieldAccessMapper INSTANCE = Mappers.getMapper(SysAppFieldAccessMapper.class);

    SysAppFieldAccessModel fieldAccessToFieldAccessModel(SysAppRoleFieldAccess FieldAccess);

    @Mappings({
            @Mapping(source = "createId", target = "createId", qualifiedByName = "getCurrentId"),
            @Mapping(source = "createTime", target = "createTime", qualifiedByName = "getTime"),
    })
    SysAppRoleFieldAccess addModelToFieldAccess(SysAppFieldAccessAddModel addModel);

    @Mappings({
            @Mapping(source = "modifyId", target = "modifyId", qualifiedByName = "getCurrentId"),
            @Mapping(source = "modifyTime", target = "modifyTime", qualifiedByName = "getTime")
    })
    void modifyModelToFieldAccess(SysAppFieldAccessModifyModel modifyModel, @MappingTarget SysAppRoleFieldAccess FieldAccess);

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
