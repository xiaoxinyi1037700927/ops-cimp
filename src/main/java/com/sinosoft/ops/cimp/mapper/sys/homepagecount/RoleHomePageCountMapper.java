package com.sinosoft.ops.cimp.mapper.sys.homepagecount;
import com.sinosoft.ops.cimp.entity.user.RoleHomePageCount;
import com.sinosoft.ops.cimp.vo.to.sys.homepagecount.RoleHomePageCountVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface RoleHomePageCountMapper {
    RoleHomePageCountMapper INSTANCE = Mappers.getMapper(RoleHomePageCountMapper.class);

    RoleHomePageCountVO poToRoleHomePageCountVO(RoleHomePageCount po);

    List<RoleHomePageCountVO> poListToRoleHomePageCountVOList(List<RoleHomePageCount> poList);


}
