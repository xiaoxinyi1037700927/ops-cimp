package com.sinosoft.ops.cimp.mapper.table;

import com.sinosoft.ops.cimp.entity.sys.table.SysTable;
import com.sinosoft.ops.cimp.vo.from.table.SysTableAddModel;
import com.sinosoft.ops.cimp.vo.to.table.SysTableModel;
import com.sinosoft.ops.cimp.vo.from.table.SysTableModifyModel;
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
