package com.sinosoft.ops.cimp.mapper.table;

import com.sinosoft.ops.cimp.entity.sys.systable.SysTable;
import com.sinosoft.ops.cimp.vo.from.sys.systable.SysTableAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.systable.SysTableModifyModel;
import com.sinosoft.ops.cimp.vo.to.sys.systable.SysTableModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface SysTableModelMapper {

    SysTableModelMapper INSTANCE = Mappers.getMapper(SysTableModelMapper.class);

    SysTable addModelToSysTable(SysTableAddModel sysTableAddModel);

    SysTable modifyModelToSysTable(SysTableModifyModel sysTableModifyModel);

    SysTableModel sysTableToModel(SysTable sysTable);

    SysTableModifyModel toModifyModel(SysTable sysTable);


}
