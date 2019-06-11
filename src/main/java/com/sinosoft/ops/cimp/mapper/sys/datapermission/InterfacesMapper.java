package com.sinosoft.ops.cimp.mapper.sys.datapermission;

import com.sinosoft.ops.cimp.entity.sys.datapermission.Interfaces;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.InterfacesAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.dataPermission.InterfacesModifyModel;
import com.sinosoft.ops.cimp.vo.to.sys.datapermission.InterfacesModel;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Mapper
public interface InterfacesMapper {
    InterfacesMapper INSTANCE = Mappers.getMapper(InterfacesMapper.class);

    InterfacesModel interfacesToModel(Interfaces interfaces);

    @Mappings({
            @Mapping(source = "createId", target = "createId", qualifiedByName = "getCurrentId"),
            @Mapping(source = "createTime", target = "createTime", qualifiedByName = "getTime"),
    })
    Interfaces addModelToInterfaces(InterfacesAddModel addModel);

    @Mappings({
            @Mapping(source = "modifyId", target = "modifyId", qualifiedByName = "getCurrentId"),
            @Mapping(source = "modifyTime", target = "modifyTime", qualifiedByName = "getTime")
    })
    void modifyModelToInterfaces(InterfacesModifyModel modifyModel, @MappingTarget Interfaces interfaces);


    @Named("getCurrentId")
    default String getCurrentId(String id) {
        return SecurityUtils.getSubject().getCurrentUser().getId();
    }

    @Named("getTime")
    default Date getTime(Date time) {
        return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
    }

}
