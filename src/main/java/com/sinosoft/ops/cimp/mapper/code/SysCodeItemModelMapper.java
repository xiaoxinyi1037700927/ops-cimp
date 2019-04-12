package com.sinosoft.ops.cimp.mapper.code;

import com.sinosoft.ops.cimp.entity.sys.code.SysCodeItem;
import com.sinosoft.ops.cimp.vo.from.code.SysCodeItemAddModel;
import com.sinosoft.ops.cimp.vo.from.code.SysCodeItemModifyModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SysCodeItemModelMapper {

    SysCodeItemModelMapper INSTANCE = Mappers.getMapper(SysCodeItemModelMapper.class);

    SysCodeItem addModelToSysCodeItem(SysCodeItemAddModel sysCodeItemAddModel);

    SysCodeItem modifyModelToSysCodeItem(SysCodeItemModifyModel sysCodeItemModifyModel);

    SysCodeItemModifyModel sysCodeItemToModifyModel(SysCodeItem sysCodeItem);
}
