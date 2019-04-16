package com.sinosoft.ops.cimp.mapper.sys.app;

import com.sinosoft.ops.cimp.entity.sys.app.SysAppTableGroup;
import com.sinosoft.ops.cimp.util.IdUtil;
import com.sinosoft.ops.cimp.vo.from.sys.app.sysAppTableGroup.SysAppTableGroupAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.app.sysAppTableGroup.SysAppTableGroupModifyModel;
import com.sinosoft.ops.cimp.vo.to.sys.app.sysAppTableGroup.SysAppTableGroupModel;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Mapper
public interface SysAppTableGroupMapper {
    SysAppTableGroupMapper INSTANCE = Mappers.getMapper(SysAppTableGroupMapper.class);

    SysAppTableGroupModel tableGroupToTableGroupModel(SysAppTableGroup tableGroup);

    @Mappings({
            @Mapping(source = "createId", target = "createId", qualifiedByName = "getCurrentId"),
            @Mapping(source = "createTime", target = "createTime", qualifiedByName = "getTime"),
            @Mapping(target = "id", qualifiedByName = "getNewId")
    })
    SysAppTableGroup addModelToTableGroup(SysAppTableGroupAddModel addModel);

    @Mappings({
            @Mapping(source = "modifyId", target = "modifyId", qualifiedByName = "getCurrentId"),
            @Mapping(source = "modifyTime", target = "modifyTime", qualifiedByName = "getTime")
    })
    void modifyModelToTableGroup(SysAppTableGroupModifyModel modifyModel, @MappingTarget SysAppTableGroup tableGroup);


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
