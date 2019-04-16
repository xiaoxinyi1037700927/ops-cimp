package com.sinosoft.ops.cimp.mapper.user;


import com.sinosoft.ops.cimp.entity.sys.user.SystemCode;
import com.sinosoft.ops.cimp.entity.sys.user.organization.OrganizationPosition;
import com.sinosoft.ops.cimp.util.SystemCodeCacheManager;
import com.sinosoft.ops.cimp.vo.to.user.OrganizationPositionViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrganizationPositionViewMapper {

    OrganizationPositionViewMapper INSTANCE = Mappers.getMapper(OrganizationPositionViewMapper.class);


    @Mappings({
            @Mapping(source = "rankId", target = "rankName", qualifiedByName = "genRankName"),
            @Mapping(source = "rankId", target = "rankCode", qualifiedByName = "genRankCode"),
    })
    OrganizationPositionViewModel organizationPositionToViewModel(OrganizationPosition organizationPosition);


    @Named("genRankName")
    default String genRankName(String rankId) {
        //从职级从代码表里面取出
        SystemCode systemCode = SystemCodeCacheManager.getSubject().getSystemCodeById(rankId);
        if (systemCode != null) {
            return systemCode.getName();
        }
        return null;
    }

    @Named("genRankCode")
    default String genRankCode(String rankId) {
        //从职级从代码表里面取出
        SystemCode systemCode = SystemCodeCacheManager.getSubject().getSystemCodeById(rankId);
        if (systemCode != null) {
            return systemCode.getCode();
        }
        return null;
    }
}
