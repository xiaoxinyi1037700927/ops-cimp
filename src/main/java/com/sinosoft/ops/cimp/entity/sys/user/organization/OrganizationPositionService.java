package com.sinosoft.ops.cimp.entity.sys.user.organization;

import java.util.List;

public interface OrganizationPositionService {

    /**
     * 根据 机构ID查询机构内职务
     * @return
     */
    List<OrganizationPosition> findByOrganizationId(String organizationId);

    /**
     * 根据职务ID,得到职务信息
     * @param positionId
     * @return
     */
    OrganizationPosition findById(String positionId);
}
