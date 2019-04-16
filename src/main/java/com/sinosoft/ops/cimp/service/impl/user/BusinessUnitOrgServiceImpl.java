package com.sinosoft.ops.cimp.service.impl.user;

import com.sinosoft.ops.cimp.entity.sys.oraganization.BusinessUnitOrg;
import com.sinosoft.ops.cimp.entity.sys.oraganization.Organization;
import com.sinosoft.ops.cimp.entity.sys.oraganization.QBusinessUnitOrg;
import com.sinosoft.ops.cimp.mapper.user.OrganizationViewMapper;
import com.sinosoft.ops.cimp.repository.user.BusinessUnitOrgRepository;
import com.sinosoft.ops.cimp.repository.user.OrganizationRepository;
import com.sinosoft.ops.cimp.service.user.BusinessUnitOrgService;
import com.sinosoft.ops.cimp.vo.user.organization.BusinessUnitOrgListViewModel;
import com.sinosoft.ops.cimp.vo.user.organization.OrganizationSearchViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusinessUnitOrgServiceImpl implements BusinessUnitOrgService {
    @Autowired
    private BusinessUnitOrgRepository businessUnitOrgRepository;
    @Autowired
    private OrganizationRepository organizationRepository;

    @Override
    public Long countByBusinessUnitId(String businessUnitId) {
        long l = businessUnitOrgRepository.countByBusinessUnitId(businessUnitId);
        return l;
    }

    @Override
    public List<BusinessUnitOrgListViewModel> businessUnitOrgListByBusinessUnitId(OrganizationSearchViewModel searchViewModel) {
        List<BusinessUnitOrg> all = (List<BusinessUnitOrg>) businessUnitOrgRepository.findAll(QBusinessUnitOrg.businessUnitOrg.businessUnitId.eq(searchViewModel.getBusinessUnitId()));
        List<String> orgIds = all.stream().map(x -> x.getOrganizationId()).collect(Collectors.toList());
        List<Organization> organizationList = organizationRepository.findByIdIn(orgIds);
        List<BusinessUnitOrgListViewModel> collect = organizationList.stream().map(x -> OrganizationViewMapper.INSTANCE.organizationToBUOViewModel(x)).collect(Collectors.toList());
        return collect;
    }
}
