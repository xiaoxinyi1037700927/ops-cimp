package com.sinosoft.ops.cimp.mapper.code;

import com.sinosoft.ops.cimp.entity.sys.code.SysCodeSet;
import com.sinosoft.ops.cimp.vo.from.code.SysCodeSetAddModel;
import com.sinosoft.ops.cimp.vo.from.code.SysCodeSetModifyModel;
import com.sinosoft.ops.cimp.vo.to.sys.code.SysCodeSetDisplayModel;
import com.sinosoft.ops.cimp.vo.to.sys.code.SysCodeSetModel;
import com.sinosoft.ops.cimp.vo.to.sys.code.SysCodeSetObtainModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SysCodeSetModelMapper {

    SysCodeSetModelMapper INSTANCE = Mappers.getMapper(SysCodeSetModelMapper.class);

    SysCodeSet sysCodeSetAddModelToSysCodeSet(SysCodeSetAddModel sysCodeSetAddModel);

    SysCodeSet sysCodeSetModifyModelToSysCodeSet(SysCodeSetModifyModel sysCodeSetModifyModel);

    SysCodeSetModifyModel sysCodeSetToModifyModel(SysCodeSet sysCodeSet);

    SysCodeSetModel codeSetToModel(SysCodeSet sysCodeSet);

    SysCodeSetDisplayModel codeSetToDisplay(SysCodeSet sysCodeSet);

    SysCodeSetObtainModel codeSetToObtail(SysCodeSet sysCodeSet);
 }
