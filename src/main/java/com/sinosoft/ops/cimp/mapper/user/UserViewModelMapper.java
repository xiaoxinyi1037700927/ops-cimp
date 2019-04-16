package com.sinosoft.ops.cimp.mapper.user;


import com.sinosoft.ops.cimp.entity.sys.oraganization.Organization;
import com.sinosoft.ops.cimp.entity.sys.user.User;
import com.sinosoft.ops.cimp.entity.sys.user.cadre.CadreInfo;
import com.sinosoft.ops.cimp.repository.user.CadreInfoRepository;
import com.sinosoft.ops.cimp.util.CachePackage.OrganizationCacheManager;
import com.sinosoft.ops.cimp.util.PasswordEncoderHelper;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.util.SpringContextUtils;
import com.sinosoft.ops.cimp.util.UserTask;
import com.sinosoft.ops.cimp.vo.from.user.UserAddViewModel;
import com.sinosoft.ops.cimp.vo.from.user.UserModifyViewModel;
import com.sinosoft.ops.cimp.vo.from.user.UserTaskAddViewModel;
import com.sinosoft.ops.cimp.vo.user.UserViewModel;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Mapper
public interface UserViewModelMapper {
    UserViewModelMapper INSTANCE = Mappers.getMapper(UserViewModelMapper.class);

    @Mappings({
            @Mapping(source = "organizationId", target = "organizationName", qualifiedByName = "getOrganizationName"),
            @Mapping(source = "organizationId", target = "organizationCode", qualifiedByName = "genOrganizationCode")
    })
    UserViewModel UserToUserViewModel(User car);

    @Mappings({
            @Mapping(source = "loginPassword",target = "loginPassword", qualifiedByName = "genLoginPassword"),
            @Mapping(source = "createId", target = "createId", qualifiedByName = "genCreateId"),
            @Mapping(source = "createTime", target = "createTime", qualifiedByName ="genCreateTime" ),
            @Mapping(source = "cadreInfoId", target = "name", qualifiedByName = "genCadreInfoName"),
            @Mapping(source = "organizationId", target = "organizationCode", qualifiedByName = "genOrganizationCode")
    })
    User UserAddViewModelToUser(UserAddViewModel userAddViewModel);

    @Mappings({
            @Mapping(target = "name",ignore = true),
            @Mapping(target = "loginName",ignore = true),
            @Mapping(target = "cadreInfoId", ignore = true),
            @Mapping(target = "organizationId",ignore = true),
            @Mapping(target = "telePhone",ignore = true),
            @Mapping(target = "mobilePhone",ignore = true),
            @Mapping(target = "description",ignore = true),
            @Mapping(target = "dataOrganizationId",ignore = true)
    })
    void userToUserModifyViewModel(User user, @MappingTarget UserModifyViewModel userModifyViewModel);


    @Mappings({
            @Mapping(source = "modifyId", target = "modifyId", qualifiedByName = "genModifyId"),
            @Mapping(source = "modifyTime", target = "modifyTime", qualifiedByName = "genModifyTime"),
            @Mapping(source = "organizationId", target = "organizationCode", qualifiedByName = "genOrganizationCode")
    })
    User userModifyViewModelToUser(UserModifyViewModel userModifyViewModel);

    @Mappings({
            @Mapping(source = "createId", target = "createId", qualifiedByName = "genCreateId"),
            @Mapping(source = "createTime", target = "createTime", qualifiedByName ="genCreateTime" )
    })
    UserTask viewModelToUserTask(UserTaskAddViewModel userTaskAddViewModel);


    @Named("genCreateId")
    default String genCreateId(String createId) {
        return SecurityUtils.getSubject().getCurrentUser().getId();
    }

    @Named("genCreateTime")
    default Date genCreateTime(Date createTime) {
        return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
    }
    @Named(value = "getOrganizationName")
    default String getOrganizationName(String organizationId) {
        Organization org = OrganizationCacheManager.getSubject().getOrganizationById(organizationId);
        if (org == null) {
            return null;
        }
        return org.getName();
    }

    @Named(value = "genOrganizationCode")
    default String genOrganizationCode(String organizationId) {
        Organization organization = OrganizationCacheManager.getSubject().getOrganizationById(organizationId);
        if (organization != null) {
            return organization.getCode();
        }
        return null;
    }

    @Named(value = "genCadreInfoName")
    default String genCadreInfoName(String cadreInfoId) {
        String name = null;
        CadreInfoRepository clazz = SpringContextUtils.getBean(CadreInfoRepository.class);
        Optional<CadreInfo> options = clazz.findById(cadreInfoId);
        if (options.isPresent()) {
            CadreInfo cadreInfo = options.get();
            name = cadreInfo.getName();
        }
        return name;
    }

    @Named(value = "genModifyId")
    default String genModifyId(String id) {
        return SecurityUtils.getSubject().getCurrentUser().getId();
    }

    @Named(value = "genModifyTime")
    default Date genModifyTime(Date modifyTime) {
        return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
    }

    @Named(value = "genLoginPassword")
    default String genLoginPassword(String loginPassword) {
        return PasswordEncoderHelper.encode(loginPassword);
    }
}
