package com.sinosoft.ops.cimp.mapper.user;

import com.sinosoft.ops.cimp.entity.oraganization.Organization;
import com.sinosoft.ops.cimp.vo.to.organization.BusinessUnitOrgListViewModel;
import com.sinosoft.ops.cimp.vo.to.organization.OrganizationViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrganizationViewMapper {

    OrganizationViewMapper INSTANCE = Mappers.getMapper(OrganizationViewMapper.class);

    OrganizationViewModel organizationToViewModel(Organization organization);

    @Mappings({
            @Mapping(source = "id", target = "organizationId"),
            @Mapping(source = "name", target = "organizationName"),
    })
    BusinessUnitOrgListViewModel organizationToBUOViewModel(Organization organization);

}
