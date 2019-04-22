package com.sinosoft.ops.cimp.mapper.sys.sysapp;

import com.sinosoft.ops.cimp.entity.sys.sysapp.SysAppTableSet;
import com.sinosoft.ops.cimp.entity.sys.systable.SysTable;
import com.sinosoft.ops.cimp.entity.sys.systable.SysTableType;
import com.sinosoft.ops.cimp.util.IdUtil;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysAppTableSet.SysAppTableSetModifyModel;
import com.sinosoft.ops.cimp.vo.to.sys.sysapp.sysAppTableSet.SysAppTableModel;
import com.sinosoft.ops.cimp.vo.to.sys.sysapp.sysAppTableSet.SysAppTableSetModel;
import com.sinosoft.ops.cimp.vo.to.sys.sysapp.sysAppTableSet.SysAppTableTypeModel;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Mapper
public interface SysAppTableSetMapper {
    SysAppTableSetMapper INSTANCE = Mappers.getMapper(SysAppTableSetMapper.class);

    SysAppTableSetModel tableSetToTableSetModel(SysAppTableSet tableSet);

    @Mappings({
            @Mapping(source = "modifyId", target = "modifyId", qualifiedByName = "getCurrentId"),
            @Mapping(source = "modifyTime", target = "modifyTime", qualifiedByName = "getTime")
    })
    void modifyModelToTableSet(SysAppTableSetModifyModel modifyModel, @MappingTarget SysAppTableSet tableSet);

    SysAppTableModel sysTableToSysTableModel(SysTable sysTable);

    SysAppTableTypeModel sysTableTypeToSysAppTableTypeModel(SysTableType sysTableType);

    @Named("getCurrentId")
    default String getCurrentId(String id) {
        return SecurityUtils.getSubject().getCurrentUser().getId();
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
