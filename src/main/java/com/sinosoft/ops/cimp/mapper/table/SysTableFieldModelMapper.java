package com.sinosoft.ops.cimp.mapper.table;

import com.sinosoft.ops.cimp.entity.sys.table.SysTableField;
import com.sinosoft.ops.cimp.vo.from.table.SysTableFieldAddModel;
import com.sinosoft.ops.cimp.vo.from.table.SysTableFieldModifyModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SysTableFieldModelMapper {

    SysTableFieldModelMapper INSTANCE = Mappers.getMapper(SysTableFieldModelMapper.class);

    SysTableField addModelToSysTableField(SysTableFieldAddModel sysTableFieldAddModel);

    SysTableField modifyModelToSysTableField(SysTableFieldModifyModel sysTableFieldModifyModel);

    SysTableFieldModifyModel sysTableFieldToModel(SysTableField sysTableField);



}
