package com.sinosoft.ops.cimp.service.sys.datapermission.impl;

import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sinosoft.ops.cimp.entity.oraganization.Organization;
import com.sinosoft.ops.cimp.entity.sys.datapermission.PageInterface;
import com.sinosoft.ops.cimp.entity.sys.datapermission.QPageInterface;
import com.sinosoft.ops.cimp.entity.sys.datapermission.QRolePageInterface;
import com.sinosoft.ops.cimp.entity.sys.datapermission.RolePageInterface;
import com.sinosoft.ops.cimp.entity.user.QUserRole;
import com.sinosoft.ops.cimp.entity.user.UserRole;
import com.sinosoft.ops.cimp.mapper.sys.datapermission.DataPermissionMapper;
import com.sinosoft.ops.cimp.repository.oraganization.OrganizationRepository;
import com.sinosoft.ops.cimp.repository.sys.datapermission.PageInterfaceRepository;
import com.sinosoft.ops.cimp.repository.sys.datapermission.RolePageInterfaceRepository;
import com.sinosoft.ops.cimp.repository.user.UserRoleRepository;
import com.sinosoft.ops.cimp.service.sys.datapermission.DataPermissionService;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.vo.to.sys.datapermission.PageInterfaceVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Jay on 2019/2/22.
 *
 * @author Jay
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinoSoft All Rights Received
 */
@Service
public class DataPermissionServiceImpl implements DataPermissionService {
    @Autowired
    private PageInterfaceRepository pageInterfaceRepository;
    @Autowired
    private RolePageInterfaceRepository rolePageInterfaceRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private OrganizationRepository organizationRepository;

    @PersistenceContext
    private EntityManager entityManager;
    private JPAQueryFactory queryFactory;

    @PostConstruct
    public void getFactory() {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Boolean savePageInterface(PageInterfaceVO vo) {
        PageInterface pageInterface = DataPermissionMapper.INSTANCE.voToPageInterface(vo);
        pageInterfaceRepository.save(pageInterface);
        return true;
    }

    @Override
    public List<PageInterfaceVO> findPageInterfaceVOList(String permissionPageId) {
        String sql = findSearchCondition();
        BooleanBuilder booleanBuilder=new BooleanBuilder();
        booleanBuilder.and(QPageInterface.pageInterface.permissionPageId.eq(permissionPageId));
        if(!StringUtils.isEmpty(sql)){
            booleanBuilder.and(Expressions.booleanTemplate(sql));
        }

        JPAQuery<PageInterfaceVO> where = queryFactory.select(Projections.bean(
                PageInterfaceVO.class,
                QPageInterface.pageInterface.id,
                QPageInterface.pageInterface.name,
                QPageInterface.pageInterface.showName,
                QPageInterface.pageInterface.permissionPageId,
                QPageInterface.pageInterface.description,
                QPageInterface.pageInterface.sql
//                QRolePageInterface.rolePageInterface.sql.as("roleSql")
        ))
                .from(QPageInterface.pageInterface)
//                .leftJoin(QRolePageInterface.rolePageInterface).on(QRolePageInterface.rolePageInterface.pageInterfaceId.eq(QPageInterface.pageInterface.id))
                .where(booleanBuilder);
        List<PageInterfaceVO> fetch = Lists.newArrayList(where.fetch());
        return fetch;
    }

    @Override
    public List<PageInterfaceVO> findPageInterfaceVOListForRole(String permissionPageId, String roleId) {
        String sql = findSearchCondition();
        BooleanBuilder booleanBuilder=new BooleanBuilder();
        booleanBuilder.and(QPageInterface.pageInterface.permissionPageId.eq(permissionPageId));
        if(!StringUtils.isEmpty(sql)){
            booleanBuilder.and(Expressions.booleanTemplate(sql));
        }
        if(StringUtils.isEmpty(roleId)){
            roleId="-1";
        }
        JPAQuery<PageInterfaceVO> where = queryFactory.select(Projections.bean(
                PageInterfaceVO.class,
                QPageInterface.pageInterface.id,
                QPageInterface.pageInterface.name,
                QPageInterface.pageInterface.showName,
                QPageInterface.pageInterface.permissionPageId,
                QPageInterface.pageInterface.description,
                QPageInterface.pageInterface.sql,
                QRolePageInterface.rolePageInterface.sql.as("roleSql")
        )).from(QPageInterface.pageInterface)
                .leftJoin(QRolePageInterface.rolePageInterface).on(QRolePageInterface.rolePageInterface.pageInterfaceId.eq(QPageInterface.pageInterface.id).and(QRolePageInterface.rolePageInterface.roleId.eq(roleId)))
                .where(booleanBuilder);
        List<PageInterfaceVO> fetch = Lists.newArrayList(where.fetch());
        return fetch;
    }

    @Override
    public String findDataByInterfaceAndRoleId(String pageInterfaceId, String roleId) {
        ArrayList<RolePageInterface> rolePageInterfaces = Lists.newArrayList(rolePageInterfaceRepository.findAll(QRolePageInterface.rolePageInterface.roleId.eq(roleId).and(QRolePageInterface.rolePageInterface.pageInterfaceId.eq(pageInterfaceId))));
        if(rolePageInterfaces.size()>0){
            String sql = rolePageInterfaces.get(0).getSql();
            if(!StringUtils.isEmpty(sql)){
                return sql;
            }
        }
        Optional<PageInterface> byId = pageInterfaceRepository.findById(pageInterfaceId);
        if(byId.isPresent()){
            PageInterface pageInterface = byId.get();
            return pageInterface.getSql();
        }
        return null;
    }

    @Override
    public String findSearchCondition() {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String requestURI = request.getRequestURI();
        if (SecurityUtils.getSubject().getCurrentUser() == null) {
            return "";
        }
        String userId = SecurityUtils.getSubject().getCurrentUser().getId();
        Organization dataOrganization = organizationRepository.findById(SecurityUtils.getSubject().getCurrentUser().getOrganizationId()).get();
        ArrayList<UserRole> userRoles = Lists.newArrayList(userRoleRepository.findAll(QUserRole.userRole.userId.eq(userId)));
        String roleId = userRoles.get(0).getRoleId();
        ArrayList<PageInterface> pageInterfaces = Lists.newArrayList(pageInterfaceRepository.findAll(QPageInterface.pageInterface.name.eq(requestURI)));
        if(pageInterfaces.size() == 0){
            return null;
        }
        String returnSql=null;
        PageInterface pageInterface=pageInterfaces.get(0);
        ArrayList<RolePageInterface> rolePageInterfaces = Lists.newArrayList(rolePageInterfaceRepository.findAll(QRolePageInterface.rolePageInterface.roleId.eq(roleId).and(QRolePageInterface.rolePageInterface.pageInterfaceId.eq(pageInterface.getId()))));
        if(rolePageInterfaces.size()>0){
            returnSql = rolePageInterfaces.get(0).getSql();
        }
        if(StringUtils.isEmpty(returnSql)){
            returnSql=pageInterface.getSql();
        }
        if(StringUtils.isEmpty(returnSql)){
            return null;
        }
        returnSql=returnSql.replaceAll("\\[自己单位code\\]",dataOrganization.getCode());
        returnSql=returnSql.replaceAll("\\[自己单位id\\]",dataOrganization.getCode());

        return returnSql;
    }

    @Override
    public Boolean fillSqlToRole(String id, String roleId,String roleSql) {
        ArrayList<RolePageInterface> rolePageInterfaces = Lists.newArrayList(rolePageInterfaceRepository.findAll(QRolePageInterface.rolePageInterface.pageInterfaceId.eq(id).and(QRolePageInterface.rolePageInterface.roleId.eq(roleId))));
        RolePageInterface rolePageInterface;
        if(rolePageInterfaces.size() ==0){
            rolePageInterface=new RolePageInterface();
            rolePageInterface.setPageInterfaceId(id);
            rolePageInterface.setRoleId(roleId);
        }else{
            rolePageInterface=rolePageInterfaces.get(0);
        }
        rolePageInterface.setSql(roleSql);
        rolePageInterfaceRepository.save(rolePageInterface);
        return true;
    }
}

