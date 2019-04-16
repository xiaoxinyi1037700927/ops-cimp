package com.sinosoft.ops.cimp.mapper.user.homePageCount;
import com.sinosoft.ops.cimp.entity.sys.user.RoleHomePageCount;
import com.sinosoft.ops.cimp.vo.to.homePageCount.RoleHomePageCountVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface RoleHomePageCountMapper {
    RoleHomePageCountMapper INSTANCE = Mappers.getMapper(RoleHomePageCountMapper.class);

    RoleHomePageCountVO poToRoleHomePageCountVO(RoleHomePageCount po);

    List<RoleHomePageCountVO> poListToRoleHomePageCountVOList(List<RoleHomePageCount> poList);


}
