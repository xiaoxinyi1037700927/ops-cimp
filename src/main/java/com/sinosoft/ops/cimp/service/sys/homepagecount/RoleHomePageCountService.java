package com.sinosoft.ops.cimp.service.sys.homepagecount;



import com.sinosoft.ops.cimp.entity.user.RoleHomePageCount;
import com.sinosoft.ops.cimp.vo.from.sys.homepagecount.RoleHomePageCountModel;
import com.sinosoft.ops.cimp.vo.to.sys.homepagecount.RoleHomePageCountVO;

import java.util.List;

public interface RoleHomePageCountService {


    RoleHomePageCount addOrUpdateRoleCount(RoleHomePageCountModel model);

    boolean deleteRoleCountByRoleCountIdList(List<String> roleCountIdList);

    List<RoleHomePageCountVO> findRoleCountByRoleId(String roleId);

}
