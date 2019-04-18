package com.sinosoft.ops.cimp.mapper.user;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrganizationPositionViewMapper {

    OrganizationPositionViewMapper INSTANCE = Mappers.getMapper(OrganizationPositionViewMapper.class);

}
