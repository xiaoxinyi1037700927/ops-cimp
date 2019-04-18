package com.sinosoft.ops.cimp.mapper.sys.datapermission;

import com.sinosoft.ops.cimp.entity.sys.datapermission.PageInterface;
import com.sinosoft.ops.cimp.vo.to.sys.datapermission.PageInterfaceVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Created by Jay on 2019/2/22.
 *
 * @author Jay
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinoSoft All Rights Received
 */
@Mapper
public interface DataPermissionMapper {
    DataPermissionMapper INSTANCE=Mappers.getMapper(DataPermissionMapper.class);

    PageInterface voToPageInterface(PageInterfaceVO pageInterfaceVO);
}
