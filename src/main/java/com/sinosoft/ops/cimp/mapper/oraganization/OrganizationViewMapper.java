package com.sinosoft.ops.cimp.mapper.oraganization;

import com.sinosoft.ops.cimp.entity.oraganization.Organization;
import com.sinosoft.ops.cimp.vo.to.organization.OrganizationViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrganizationViewMapper {

    OrganizationViewMapper INSTANCE = Mappers.getMapper(OrganizationViewMapper.class);

    OrganizationViewModel organizationToViewModel(Organization organization);

}
