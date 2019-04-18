package com.sinosoft.ops.cimp.mapper.table;

import com.sinosoft.ops.cimp.entity.sys.systable.SysTableField;
import com.sinosoft.ops.cimp.vo.from.sys.systable.SysTableFieldAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.systable.SysTableFieldModifyModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SysTableFieldModelMapper {

    SysTableFieldModelMapper INSTANCE = Mappers.getMapper(SysTableFieldModelMapper.class);

    SysTableField addModelToSysTableField(SysTableFieldAddModel sysTableFieldAddModel);

    SysTableField modifyModelToSysTableField(SysTableFieldModifyModel sysTableFieldModifyModel);

    SysTableFieldModifyModel sysTableFieldToModel(SysTableField sysTableField);



}
