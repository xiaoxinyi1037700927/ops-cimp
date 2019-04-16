package com.sinosoft.ops.cimp.service.user.homePageCount;



import com.sinosoft.ops.cimp.entity.sys.user.RoleHomePageCount;
import com.sinosoft.ops.cimp.vo.from.user.homePageCount.RoleHomePageCountModel;
import com.sinosoft.ops.cimp.vo.to.homePageCount.RoleHomePageCountVO;

import java.util.List;

public interface RoleHomePageCountService {


    RoleHomePageCount addOrUpdateRoleCount(RoleHomePageCountModel model);

    boolean deleteRoleCountByRoleCountIdList(List<String> roleCountIdList);

    List<RoleHomePageCountVO> findRoleCountByRoleId(String roleId);

}
