package com.sinosoft.ops.cimp.service.user.impl;

import com.sinosoft.ops.cimp.entity.sys.oraganization.BusinessUnitOrg;
import com.sinosoft.ops.cimp.entity.sys.oraganization.Organization;
import com.sinosoft.ops.cimp.entity.sys.oraganization.QBusinessUnitOrg;
import com.sinosoft.ops.cimp.entity.sys.oraganization.QOrganization;
import com.sinosoft.ops.cimp.entity.sys.user.User;
import com.sinosoft.ops.cimp.mapper.user.OrganizationViewMapper;
import com.sinosoft.ops.cimp.repository.user.BusinessUnitOrgRepository;
import com.sinosoft.ops.cimp.repository.user.OrganizationRepository;
import com.sinosoft.ops.cimp.service.user.OrganizationService;
import com.sinosoft.ops.cimp.util.CachePackage.OrganizationCacheManager;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.vo.user.organization.OrganizationSearchViewModel;
import com.sinosoft.ops.cimp.vo.user.organization.OrganizationViewModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private BusinessUnitOrgRepository businessUnitOrgRepository;


    public void setChildrenFlag(List<OrganizationViewModel> subTreeNodes, List<Organization> allOrgList) {
        for (OrganizationViewModel subTreeNode : subTreeNodes) {
            long count = allOrgList.stream().filter(x -> subTreeNode.getId().equals(x.getParentId())).count();
            if (count > 0) {
                subTreeNode.setHasChildren("1");
            }
            this.setChildrenFlag(subTreeNode.getSubTreeNode(), allOrgList);
        }
    }

    @Override
    public OrganizationViewModel lstTreeNode(OrganizationSearchViewModel organizationSearchViewModel) {
        OrganizationViewModel viewModel = null;
        String organizationId = organizationSearchViewModel.getOrganizationId();
        if (StringUtils.isNotBlank(organizationId)) {
            List<Organization> allOrgList = OrganizationCacheManager.getSubject().getAllList();
            //说明传入了机构ID,是反显的
            Organization organization = OrganizationCacheManager.getSubject().getOrganizationById(organizationId);
            String parentCode = organization.getParentCode();
            Organization root = this.getRoot();
            viewModel = OrganizationViewMapper.INSTANCE.organizationToViewModel(root);
            List<Organization> childNodes = this.getParentNodesByCode(organization.getCode());
            viewModel.setHasChildren("1");
            List<String> relNodes = this.getRelNodes(parentCode);
            childNodes.sort(Comparator.comparing(Organization::getSortNumber));
            List<OrganizationViewModel> subTreeNodes = this.getSubTreeNodes(viewModel.getId(), childNodes, relNodes);
            this.setChildrenFlag(subTreeNodes, allOrgList);
            viewModel.setSubTreeNode(subTreeNodes);
        } else {
            //说明是查询树
            Organization organization = null;
            if (organizationSearchViewModel.isNoPermission()) {
                User currentUser = SecurityUtils.getSubject().getCurrentUser();
                String loginDataOrganId;
                if (currentUser == null) {
                    loginDataOrganId = "";
                } else {
                    loginDataOrganId = currentUser.getDataOrganizationId();
                }
                organization = OrganizationCacheManager.getSubject().getOrganizationById(loginDataOrganId);
            } else {
                organization = this.findRoot("ROOT");
            }
            if (organization != null) {
                viewModel = OrganizationViewMapper.INSTANCE.organizationToViewModel(organization);
                List<Organization> childNodeList = organizationRepository.findByParentCode(organization.getCode());
                childNodeList.sort(Comparator.comparing(Organization::getSortNumber));
                List<String> codeList = childNodeList.stream().map(x -> x.getCode()).collect(Collectors.toList());
                List<Organization> parentOrganList = organizationRepository.findByParentCodeIn(codeList);
                List<OrganizationViewModel> viewModelList = new ArrayList<>();
                childNodeList.forEach(childNode -> {
                    OrganizationViewModel organizationViewModel = OrganizationViewMapper.INSTANCE.organizationToViewModel(childNode);
                    long count = parentOrganList.stream().filter(x -> x.getParentCode().equals(childNode.getCode())).count();
                    if (count > 0) organizationViewModel.setHasChildren("1");
                    else organizationViewModel.setHasChildren("0");
                    viewModelList.add(organizationViewModel);
                });
                viewModel.setSubTreeNode(viewModelList);
            }
        }
        return viewModel;
    }


    private Organization getRoot() {
        User currentUser = SecurityUtils.getSubject().getCurrentUser();
        if (currentUser != null) {
            String organizationId = currentUser.getDataOrganizationId();
            return OrganizationCacheManager.getSubject().getOrganizationById(organizationId);
        } else {
            return OrganizationCacheManager.getSubject().getOrganizationByCode("001");
        }
    }

    private List<String> getRelNodes(String parentCode) {
        List<String> codeList = Arrays.asList(parentCode.split("\\."));
        StringBuilder builder = new StringBuilder();
        List<String> newCodeList = new ArrayList<>();
        codeList.forEach(code -> {
            builder.append(code).append(".");
            newCodeList.add(builder.toString().substring(0, builder.length() - 1));
        });
        return newCodeList;
    }

    private List<Organization> getParentNodesByCode(String parentCode) {
        List<String> codeList = Arrays.asList(parentCode.split("\\."));
        StringBuilder builder = new StringBuilder();
        List<String> newCodeList = new ArrayList<>();
        codeList.forEach(code -> {
            builder.append(code).append(".");
            newCodeList.add(builder.toString().substring(0, builder.length() - 1));
        });
        List<Organization> organizationList = organizationRepository.findByParentCodeIn(newCodeList);
        return organizationList;
    }

    public List<OrganizationViewModel> getSubTreeNodes(String parentId, List<Organization> allChildNodes, List<String> relNodes) {
        List<OrganizationViewModel> subTreeNodeLst = new ArrayList<>();
        allChildNodes.forEach(subNode -> {
            if (parentId.equals(subNode.getParentId())) {
                OrganizationViewModel viewModel = OrganizationViewMapper.INSTANCE.organizationToViewModel(subNode);
                viewModel.setHasChildren("0");
                if (relNodes.contains(viewModel.getCode())) viewModel.setHasChildren("1");
                List<OrganizationViewModel> subTreeNodes = this.getSubTreeNodes(subNode.getId(), allChildNodes, relNodes);
                viewModel.setSubTreeNode(subTreeNodes);
                subTreeNodeLst.add(viewModel);
            }
        });
        return subTreeNodeLst;
    }

    @Override
    public List<OrganizationViewModel> findOrganizationByParentId(String parentId) {
        List<OrganizationViewModel> organizationViewModelList = new ArrayList<>();
        List<Organization> organizationList = organizationRepository.findByParentIdOrderBySortNumber(parentId);
        List<String> codeList = organizationList.stream().map(x -> x.getCode()).collect(Collectors.toList());
        List<Organization> childNodeList = organizationRepository.findByParentCodeInOrderBySortNumber(codeList);


        organizationList.forEach(organization -> {
            OrganizationViewModel viewModel = OrganizationViewMapper.INSTANCE.organizationToViewModel(organization);
            List<Organization> childNodes = childNodeList.stream().filter(child -> organization.getCode().equals(child.getParentCode())).collect(Collectors.toList());
            if (childNodes.size() > 0) viewModel.setHasChildren("1");
            else viewModel.setHasChildren("0");
            organizationViewModelList.add(viewModel);
        });
        return organizationViewModelList;
    }

    @Override
    public Organization findOrganizationById(String organizationId) {
        Organization organization = null;
        Optional<Organization> options = organizationRepository.findById(organizationId);
        if (options.isPresent()) {
            organization = options.get();
        }
        return organization;
    }

    @Override
    public Organization findRoot(String parentCode) {
        Organization organization = null;
        List<Organization> organizationList = organizationRepository.findByParentCode(parentCode);
        if (organizationList.size() > 0) {
            organization = organizationList.get(0);
        }
        return organization;
    }

    public Organization findByCode(String code) {
        Organization organization = null;
        List<Organization> organizationList = organizationRepository.findByCode(code);
        if (organizationList.size() > 0) {
            organization = organizationList.get(0);
        }
        return organization;
    }

    @Override
    public List<Organization> findAll() {
        List<Organization> organizationList = organizationRepository.findData();
        return organizationList;
    }

    @Override
    public OrganizationViewModel lstTreeNodeOfSuperBusiness(OrganizationSearchViewModel organizationSearchViewModel) {
        OrganizationViewModel viewModel;
        String organizationId = organizationSearchViewModel.getOrganizationId();
        String businessUnitId = organizationSearchViewModel.getBusinessUnitId();

        if (StringUtils.isNotBlank(businessUnitId)) {
            /**
             * 查询相关部门关联的所有部门id及父类id
             */
            //相关部门关联机构id
            List<BusinessUnitOrg> businessUnitOrgList = (List<BusinessUnitOrg>) businessUnitOrgRepository.findAll(QBusinessUnitOrg.businessUnitOrg.businessUnitId.eq(businessUnitId));
            List<String> businessUnitOrgIdList = businessUnitOrgList.stream().map(x -> x.getOrganizationId()).collect(Collectors.toList());
            //查询关联机构
            List<Organization> organizationList = organizationRepository.findByIdIn(businessUnitOrgIdList);
            List<String> allCodes = new ArrayList<>();
            //获得所有关联机构及其父类code
            for (Organization organization : organizationList) {
                List<String> relNodes = this.getRelNodes(organization.getParentCode());
                allCodes.addAll(relNodes);
            }
            //去重
            HashSet h = new HashSet(allCodes);
            allCodes.clear();
            allCodes.addAll(h);
            List<Organization> parentOrganizationList = (List<Organization>) organizationRepository.findAll(QOrganization.organization.code.in(allCodes));
            //所有需要显示被选中的机构id
            List<String> parentOrganizationIdList = parentOrganizationList.stream().map(x -> x.getId()).collect(Collectors.toList());
            parentOrganizationIdList.addAll(businessUnitOrgIdList);

            Organization passOrganization = null;
            if (StringUtils.isNotBlank(organizationId)) {
                passOrganization = this.findOrganizationById(organizationId);
            } else {
                passOrganization = this.findRoot("ROOT");
            }
            viewModel = OrganizationViewMapper.INSTANCE.organizationToViewModel(passOrganization);
            if (parentOrganizationIdList.contains(viewModel.getId())) viewModel.setIsCheck("1");
            List<Organization> children = (List<Organization>) organizationRepository.findAll(QOrganization.organization.parentCode.eq(passOrganization.getCode()));
            if (children.size() > 0) {
                viewModel.setHasChildren("1");
                List<OrganizationViewModel> subTreeNode = children.stream().map(x -> OrganizationViewMapper.INSTANCE.organizationToViewModel(x)).collect(Collectors.toList());
                subTreeNode.forEach(subNode -> {
                    if (parentOrganizationIdList.contains(subNode.getId())) subNode.setIsCheck("1");
                    List<Organization> childList = (List<Organization>) organizationRepository.findAll(QOrganization.organization.parentCode.eq(subNode.getCode()));
                    if (childList.size() > 0) subNode.setHasChildren("1");
                });
                viewModel.setSubTreeNode(subTreeNode);
            }

        } else {
            Organization passOrganization = null;
            if (StringUtils.isNotBlank(organizationId)) {
                passOrganization = this.findOrganizationById(organizationId);
            } else {
                passOrganization = this.findRoot("ROOT");
            }
            viewModel = OrganizationViewMapper.INSTANCE.organizationToViewModel(passOrganization);
            List<Organization> children = (List<Organization>) organizationRepository.findAll(QOrganization.organization.parentCode.eq(passOrganization.getCode()));
            if (children.size() > 0) {
                viewModel.setHasChildren("1");
                List<OrganizationViewModel> subTreeNode = children.stream().map(x -> OrganizationViewMapper.INSTANCE.organizationToViewModel(x)).collect(Collectors.toList());
                subTreeNode.forEach(subNode -> {
                    List<Organization> childList = (List<Organization>) organizationRepository.findAll(QOrganization.organization.parentCode.eq(subNode.getCode()));
                    if (childList.size() > 0) subNode.setHasChildren("1");
                });
                viewModel.setSubTreeNode(subTreeNode);
            }

        }
        return viewModel;
    }

    @Override
    public List<OrganizationViewModel> findOrganizationByName(String name, String permission) {
        List<Organization> allOrgList = OrganizationCacheManager.getSubject().getAllList();
        if ("1".equals(permission)) {
            //当前单位数据权限
            User currentUser = SecurityUtils.getSubject().getCurrentUser();
            if (currentUser.getDataOrganizationId() == null || "".equals(currentUser.getDataOrganizationId())) {
                List<OrganizationViewModel> collect = allOrgList.stream().filter(x -> x.getId().equals(currentUser.getOrganizationId())).map(y -> OrganizationViewMapper.INSTANCE.organizationToViewModel(y)).collect(Collectors.toList());
                return collect;
            } else {
                Organization DataOrg = OrganizationCacheManager.getSubject().getOrganizationById(currentUser.getDataOrganizationId());
                List<OrganizationViewModel> collect = allOrgList.stream().filter(x -> x.getCode().startsWith(DataOrg.getCode())
                        && x.getName().contains(name))
                        .map(y -> OrganizationViewMapper.INSTANCE.organizationToViewModel(y))
                        .collect(Collectors.toList());
                return collect;
            }
        } else {
            List<OrganizationViewModel> collect = allOrgList.stream().filter(x -> x.getName().contains(name)).map(y -> OrganizationViewMapper.INSTANCE.organizationToViewModel(y)).collect(Collectors.toList());
            return collect;
        }
    }
}
