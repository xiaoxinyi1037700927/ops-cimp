package com.sinosoft.ops.cimp.mapper.sys.app;

import com.sinosoft.ops.cimp.entity.sys.app.SysAppTableFieldSet;
import com.sinosoft.ops.cimp.entity.sys.table.SysTableField;
import com.sinosoft.ops.cimp.util.IdUtil;
import com.sinosoft.ops.cimp.vo.from.sys.app.sysAppTableFieldSet.SysAppTableFieldSetModifyModel;
import com.sinosoft.ops.cimp.vo.to.sys.app.sysAppTableFieldSet.SysAppTableFieldModel;
import com.sinosoft.ops.cimp.vo.to.sys.app.sysAppTableFieldSet.SysAppTableFieldSetModel;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Mapper
public interface SysAppTableFieldSetMapper {
    SysAppTableFieldSetMapper INSTANCE = Mappers.getMapper(SysAppTableFieldSetMapper.class);

    SysAppTableFieldSetModel fieldSetToFieldSetModel(SysAppTableFieldSet fieldSet);

    @Mappings({
            @Mapping(source = "modifyId", target = "modifyId", qualifiedByName = "getCurrentId"),
            @Mapping(source = "modifyTime", target = "modifyTime", qualifiedByName = "getTime")
    })
    void modifyModelToFieldSet(SysAppTableFieldSetModifyModel modifyModel, @MappingTarget SysAppTableFieldSet fieldSet);

    SysAppTableFieldModel sysTableFieldToSysTableFieldModel(SysTableField sysTableField);

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
