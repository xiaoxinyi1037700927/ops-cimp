package com.sinosoft.ops.cimp.service.user;



import com.sinosoft.ops.cimp.entity.sys.oraganization.Organization;
import com.sinosoft.ops.cimp.vo.user.organization.OrganizationSearchViewModel;
import com.sinosoft.ops.cimp.vo.user.organization.OrganizationViewModel;

import java.util.List;

public interface OrganizationService {


    /**
     * 加载机构数
     *
     * @param organizationSearchViewModel
     * @return
     */
    OrganizationViewModel lstTreeNode(OrganizationSearchViewModel organizationSearchViewModel);

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
     * 异步加载机构数 相关部门配置页面
     *
     * @param organizationSearchViewModel
     * @return
     */
    OrganizationViewModel lstTreeNodeOfSuperBusiness(OrganizationSearchViewModel organizationSearchViewModel);

    /**
     * 根据名字查询组织
     *
     * @param name
     * @return
     */
    List<OrganizationViewModel> findOrganizationByName(String name, String permission);
}
