package com.sinosoft.ops.cimp.service.sys.homepagecount;



import com.sinosoft.ops.cimp.vo.to.sys.homepagecount.HomePageCountQueryVO;
import com.sinosoft.ops.cimp.vo.to.sys.homepagecount.HomePageCountVO;

import java.util.List;

public interface HomePageCountService {


    List<HomePageCountVO> findAllHomePageCountVOList();


    //查询具体的统计项数字
    List<HomePageCountQueryVO> findRoleHomePageCountQueryByRoleIdList(List<String> roleIdList);


}
