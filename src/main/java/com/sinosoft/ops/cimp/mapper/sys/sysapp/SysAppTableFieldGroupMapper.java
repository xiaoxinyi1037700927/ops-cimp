package com.sinosoft.ops.cimp.mapper.sys.sysapp;

import com.sinosoft.ops.cimp.entity.sys.sysapp.SysAppTableFieldGroup;
import com.sinosoft.ops.cimp.util.IdUtil;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysAppTableFieldGroup.SysAppTableFieldGroupAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysAppTableFieldGroup.SysAppTableFieldGroupModifyModel;
import com.sinosoft.ops.cimp.vo.to.sys.sysapp.sysAppTableFieldGroup.SysAppTableFieldGroupModel;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Mapper
public interface SysAppTableFieldGroupMapper {
    SysAppTableFieldGroupMapper INSTANCE = Mappers.getMapper(SysAppTableFieldGroupMapper.class);

    SysAppTableFieldGroupModel fieldGroupToFieldGroupModel(SysAppTableFieldGroup fieldGroup);

    @Mappings({
            @Mapping(source = "createId", target = "createId", qualifiedByName = "getCurrentId"),
            @Mapping(source = "createTime", target = "createTime", qualifiedByName = "getTime"),
            @Mapping(target = "id", qualifiedByName = "getNewId")
    })
    SysAppTableFieldGroup addModelToFieldGroup(SysAppTableFieldGroupAddModel addModel);

    @Mappings({
            @Mapping(source = "modifyId", target = "modifyId", qualifiedByName = "getCurrentId"),
            @Mapping(source = "modifyTime", target = "modifyTime", qualifiedByName = "getTime")
    })
    void modifyModelToFieldGroup(SysAppTableFieldGroupModifyModel modifyModel, @MappingTarget SysAppTableFieldGroup fieldGroup);


    @Named("getCurrentId")
    default String getCurrentId(String id) {
        return "0";
    }

    @Named("getTime")
    default Date getTime(Date time) {
        return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
    }

    @Named("getNewId")
    default String getNewId() {
        return IdUtil.uuid();
    }
}
