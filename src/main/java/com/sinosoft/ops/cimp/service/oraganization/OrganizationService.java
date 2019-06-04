package com.sinosoft.ops.cimp.service.oraganization;


import com.sinosoft.ops.cimp.entity.oraganization.Organization;
import com.sinosoft.ops.cimp.vo.to.organization.OrganizationSearchViewModel;
import com.sinosoft.ops.cimp.vo.to.organization.OrganizationViewModel;

import java.util.List;
import java.util.Set;

public interface OrganizationService {


    /**
     * 加载机构数
     *
     * @param organizationSearchViewModel
     * @return
     */
    Set<OrganizationViewModel> lstTreeNode(OrganizationSearchViewModel organizationSearchViewModel);

    /**
     * 根据父ID查询子节点
     *
     * @param parentId
     * @return
     */
    List<OrganizationViewModel> findOrganizationByParentId(String parentId);

    /**
     * 根据机构ID 查询机构信息
     *
     * @param organizationId
     * @return
     */
    Organization findOrganizationById(String organizationId);

    /**
     * 查询根节点
     *
     * @param parentCode
     * @return
     */
    Organization findRoot(String parentCode);

    /**
     * 查询所有机构
     *
     * @return
     */
    List<Organization> findAll();


    /**
     * 根据名字查询组织
     *
     * @param name
     * @return
     */
    List<OrganizationViewModel> findOrganizationByName(String name, String permission);

}
