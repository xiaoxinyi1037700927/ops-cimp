package com.sinosoft.ops.cimp.mapper.user;

import com.google.common.collect.Lists;

import com.sinosoft.ops.cimp.entity.sys.user.*;
import com.sinosoft.ops.cimp.repository.user.MenuGroupPerRelRepository;
import com.sinosoft.ops.cimp.repository.user.MenuGroupRepository;
import com.sinosoft.ops.cimp.repository.user.permissionPage.PermissionPageRepository;
import com.sinosoft.ops.cimp.util.SpringContextUtils;
import com.sinosoft.ops.cimp.vo.user.role.MenuChildViewModel;
import com.sinosoft.ops.cimp.vo.user.role.MenuParentViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Optional;

/**
 * Created by Jay on 2018/12/9.
 *
 * @author Jay
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinoSoft All Rights Received
 */
@Mapper
public interface MenuMapper {
    MenuMapper INSTANCE= Mappers.getMapper(MenuMapper.class);

    MenuParentViewModel entityToMenuParentViewModel(MenuGroup menuGroup);

    MenuParentViewModel entityToRoleMenuViewModel(RoleMenuGroup roleMenuGroup);

    MenuGroup menuParentViewModelToEntity(MenuParentViewModel menuParentViewModel);

    @Mappings({
            @Mapping(source = "id", target = "groupId", qualifiedByName = "groupId"),
            @Mapping(source = "id", target = "groupName", qualifiedByName = "groupName"),
            @Mapping(source = "id", target = "pageCount", qualifiedByName = "pageCount"),
    })
    MenuChildViewModel entityToMenuChildViewModel(Permission permission);

    Permission permissionViewModelToEntity(MenuChildViewModel menuChildViewModel);

    @Named("groupId")
    default String groupId(String id) {
        MenuGroupPerRelRepository menuGroupPerRelRepository = SpringContextUtils.getBean(MenuGroupPerRelRepository.class);
        List<PermissionMenuGroupRel> all = Lists.newArrayList(menuGroupPerRelRepository.findAll(QPermissionMenuGroupRel.permissionMenuGroupRel.permissionId.eq(id)));
        if(all.size()>0){
            return all.get(0).getMenuGroupId();
        }

        return "";
    }

    @Named("pageCount")
    default int pageCount(String id) {
        PermissionPageRepository pageRepository = SpringContextUtils.getBean(PermissionPageRepository.class);
        long count = pageRepository.count(QPermissionPage.permissionPage.permissionId.eq(id));
        return (int)count;
    }

    @Named("groupName")
    default String groupName(String id) {
        MenuGroupPerRelRepository menuGroupPerRelRepository = SpringContextUtils.getBean(MenuGroupPerRelRepository.class);
        MenuGroupRepository menuGroupRepository = SpringContextUtils.getBean(MenuGroupRepository.class);
        List<PermissionMenuGroupRel> all = Lists.newArrayList(menuGroupPerRelRepository.findAll(QPermissionMenuGroupRel.permissionMenuGroupRel.permissionId.eq(id)));
        if(all.size()>0){
            Optional<MenuGroup> byId = menuGroupRepository.findById(all.get(0).getMenuGroupId());
            if(byId.isPresent()){
                return byId.get().getName();
            }
        }
        return "";
    }

}
