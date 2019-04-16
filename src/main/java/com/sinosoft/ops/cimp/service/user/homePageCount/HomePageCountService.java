package com.sinosoft.ops.cimp.service.user.homePageCount;



import com.sinosoft.ops.cimp.vo.to.homePageCount.HomePageCountQueryVO;
import com.sinosoft.ops.cimp.vo.to.homePageCount.HomePageCountVO;

import java.util.List;

public interface HomePageCountService {


    List<HomePageCountVO> findAllHomePageCountVOList();


    //查询具体的统计项数字
    List<HomePageCountQueryVO> findRoleHomePageCountQueryByRoleIdList(List<String> roleIdList);


}
