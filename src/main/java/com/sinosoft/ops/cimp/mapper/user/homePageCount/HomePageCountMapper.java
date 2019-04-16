package com.sinosoft.ops.cimp.mapper.user.homePageCount;


import com.sinosoft.ops.cimp.entity.sys.user.HomePageCount;
import com.sinosoft.ops.cimp.vo.to.homePageCount.HomePageCountQueryVO;
import com.sinosoft.ops.cimp.vo.to.homePageCount.HomePageCountVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface HomePageCountMapper {
    HomePageCountMapper INSTANCE = Mappers.getMapper(HomePageCountMapper.class);

    HomePageCountVO poToHomePageCountVO(HomePageCount po);

    List<HomePageCountVO> poListToHomePageCountVOList(List<HomePageCount> poList);

    HomePageCountQueryVO poToHomePageCountQueryVO(HomePageCount po);

}
