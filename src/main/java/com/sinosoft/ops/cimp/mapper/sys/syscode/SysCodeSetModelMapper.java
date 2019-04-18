package com.sinosoft.ops.cimp.mapper.sys.syscode;

import com.sinosoft.ops.cimp.entity.sys.syscode.SysCodeSet;
import com.sinosoft.ops.cimp.vo.from.sys.syscode.SysCodeSetAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.syscode.SysCodeSetModifyModel;
import com.sinosoft.ops.cimp.vo.to.sys.syscode.SysCodeSetDisplayModel;
import com.sinosoft.ops.cimp.vo.to.sys.syscode.SysCodeSetModel;
import com.sinosoft.ops.cimp.vo.to.sys.syscode.SysCodeSetObtainModel;
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
