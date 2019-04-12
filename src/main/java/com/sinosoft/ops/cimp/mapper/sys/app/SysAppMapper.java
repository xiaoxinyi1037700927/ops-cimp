package com.sinosoft.ops.cimp.mapper.sys.app;

import com.sinosoft.ops.cimp.entity.sys.app.SysApp;
import com.sinosoft.ops.cimp.util.IdUtil;
import com.sinosoft.ops.cimp.vo.from.sys.app.sysApp.SysAppAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.app.sysApp.SysAppModifyModel;
import com.sinosoft.ops.cimp.vo.to.sys.app.sysApp.SysAppModel;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Mapper
public interface SysAppMapper {

    SysAppMapper INSTANCE = Mappers.getMapper(SysAppMapper.class);

    SysAppModel sysAppToSysAppModel(SysApp sysApp);

    @Mappings({
            @Mapping(source = "createId", target = "createId", qualifiedByName = "getCurrentId"),
            @Mapping(source = "createTime", target = "createTime", qualifiedByName = "getTime"),
            @Mapping(target = "id", qualifiedByName = "getNewId")
    })
    SysApp addModelToSysApp(SysAppAddModel addModel);

    @Mappings({
            @Mapping(source = "modifyId", target = "modifyId", qualifiedByName = "getCurrentId"),
            @Mapping(source = "modifyTime", target = "modifyTime", qualifiedByName = "getTime")
    })
    void modifyModelToSysApp(SysAppModifyModel modifyModel, @MappingTarget SysApp sysApp);


    @Named("getCurrentId")
    default String getCurrentId(String id) {
        return "test";
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
