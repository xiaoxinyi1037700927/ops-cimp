package com.sinosoft.ops.cimp.service.user;


import com.sinosoft.ops.cimp.vo.user.organization.BusinessUnitOrgListViewModel;
import com.sinosoft.ops.cimp.vo.user.organization.OrganizationSearchViewModel;

import java.util.List;

public interface BusinessUnitOrgService {

    /**
     * 根据业务部门查询数量
     */
    Long countByBusinessUnitId(String businessUnitId);

    /**
     * 根据业务部门Id查询关联单位
     * @param searchViewModel
     * @return
     */
    List<BusinessUnitOrgListViewModel> businessUnitOrgListByBusinessUnitId(OrganizationSearchViewModel searchViewModel);
}
