package com.sinosoft.ops.cimp.mapper.organization;

import com.sinosoft.ops.cimp.entity.oraganization.Organization;
import com.sinosoft.ops.cimp.vo.from.DepB001;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Mapper
public interface OrganizationMapper {

    OrganizationMapper INSTANCE = Mappers.getMapper(OrganizationMapper.class);

    @Mappings({
            @Mapping(source = "depId", target = "id"),
            @Mapping(source = "b01001", target = "name"),
            @Mapping(source = "treeLevelCode", target = "code"),
            @Mapping(source = "b01004", target = "briefName"),
            @Mapping(source = "b01097", target = "description"),
            @Mapping(source = "pptr", target = "parentCode"),
            @Mapping(source = "b01025", target = "subOrdination"),
            @Mapping(source = "b01031", target = "orgType"),
            @Mapping(source = "b01027", target = "orgLevel"),
            @Mapping(source = "ordinal", target = "sortNumber"),
            @Mapping(source = "b01061", target = "createId", qualifiedByName = "genCreateId"),
            @Mapping(source = "b01068", target = "createTime", qualifiedByName = "genCreateTime")
    })
    Organization depB001ToOrganization(DepB001 car);

    @Named("genCreateId")
    default String genCreateId(String code) {
        return "admin";
    }

    @Named("genCreateTime")
    default Date genCreateTime(Date time) {
        return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
    }
}
