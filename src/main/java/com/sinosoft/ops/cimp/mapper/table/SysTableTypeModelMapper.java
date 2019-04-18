package com.sinosoft.ops.cimp.mapper.table;

import com.sinosoft.ops.cimp.entity.sys.systable.SysTableType;
import com.sinosoft.ops.cimp.vo.from.sys.systable.SysTableTypeAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.systable.SysTableTypeModifyModel;
import com.sinosoft.ops.cimp.vo.to.sys.systable.SysTableTypeModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SysTableTypeModelMapper {

    SysTableTypeModelMapper INSTANCE = Mappers.getMapper(SysTableTypeModelMapper.class);

    SysTableTypeModifyModel sysTableTypeToModel(SysTableType sysTableType);

    SysTableType modifyModelToSysTableType(SysTableTypeModifyModel sysTableModifyModel);

    SysTableType addModelfyToSysTableType(SysTableTypeAddModel sysTableTypeAddModel);

    SysTableTypeModel toSysTableTypeModel(SysTableType sysTableType);

}
