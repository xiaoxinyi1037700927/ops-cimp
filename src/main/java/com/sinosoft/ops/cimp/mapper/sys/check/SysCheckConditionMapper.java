package com.sinosoft.ops.cimp.mapper.sys.check;

import com.sinosoft.ops.cimp.entity.sys.check.SysCheckCondition;
import com.sinosoft.ops.cimp.vo.from.sys.check.SysCheckConditionAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.check.SysCheckConditionModifyModel;
import com.sinosoft.ops.cimp.vo.to.sys.check.SysCheckConditionModel;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Mapper
public interface SysCheckConditionMapper {

    SysCheckConditionMapper INSTANCE = Mappers.getMapper(SysCheckConditionMapper.class);

    SysCheckConditionModel sysCheckConditionToModel(SysCheckCondition condition);

    @Mappings({
            @Mapping(source = "createId", target = "createId", qualifiedByName = "getCurrentId"),
            @Mapping(source = "createTime", target = "createTime", qualifiedByName = "getTime")
    })
    SysCheckCondition addModelToSysCheckCondition(SysCheckConditionAddModel addModel);

    @Mappings({
            @Mapping(source = "modifyId", target = "modifyId", qualifiedByName = "getCurrentId"),
            @Mapping(source = "modifyTime", target = "modifyTime", qualifiedByName = "getTime")
    })
    void modifyModelToSysCheckCondition(SysCheckConditionModifyModel modifyModel, @MappingTarget SysCheckCondition condition);


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
