package com.sinosoft.ops.cimp.mapper.sys.syscode;

import com.sinosoft.ops.cimp.entity.sys.syscode.SysCodeItem;
import com.sinosoft.ops.cimp.vo.from.sys.syscode.SysCodeItemAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.syscode.SysCodeItemModifyModel;
import com.sinosoft.ops.cimp.vo.to.sys.syscode.SysCodeItemModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SysCodeItemModelMapper {

    SysCodeItemModelMapper INSTANCE = Mappers.getMapper(SysCodeItemModelMapper.class);

    SysCodeItem addModelToSysCodeItem(SysCodeItemAddModel sysCodeItemAddModel);

    SysCodeItem modifyModelToSysCodeItem(SysCodeItemModifyModel sysCodeItemModifyModel);

    SysCodeItemModifyModel sysCodeItemToModifyModel(SysCodeItem sysCodeItem);

    SysCodeItemModel codeItemToModel(SysCodeItem sysCodeItem);
}
