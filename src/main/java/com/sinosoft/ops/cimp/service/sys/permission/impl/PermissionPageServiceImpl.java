package com.sinosoft.ops.cimp.service.sys.permission.impl;

import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sinosoft.ops.cimp.entity.sys.permission.*;
import com.sinosoft.ops.cimp.entity.user.QRolePermissionPage;
import com.sinosoft.ops.cimp.entity.user.RolePermissionPage;
import com.sinosoft.ops.cimp.mapper.sys.permission.PermissionViewModelMapper;
import com.sinosoft.ops.cimp.repository.sys.permission.PermissionPageOperationRepository;
import com.sinosoft.ops.cimp.repository.sys.permission.PermissionPageRepository;
import com.sinosoft.ops.cimp.repository.sys.permission.PermissionRepository;
import com.sinosoft.ops.cimp.repository.user.RolePermissionPageRepository;
import com.sinosoft.ops.cimp.repository.user.UserRoleRepository;
import com.sinosoft.ops.cimp.service.sys.permission.PermissionPageService;
import com.sinosoft.ops.cimp.vo.from.sys.permission.PermissionPageSearchVO;
import com.sinosoft.ops.cimp.vo.to.sys.permission.PermissionPageOperationVO;
import com.sinosoft.ops.cimp.vo.to.sys.permission.PermissionPageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Jay on 2019/1/29.
 *
 * @author Jay
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinoSoft All Rights Received
 */
@Service
public class PermissionPageServiceImpl implements PermissionPageService {

    @PersistenceContext
    private EntityManager entityManager;
    private JPAQueryFactory queryFactory;

    @PostConstruct
    public void getFactory() {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    @Autowired
    private PermissionPageRepository permissionPageRepository;
    @Autowired
    private PermissionPageOperationRepository permissionPageOperationRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private RolePermissionPageRepository rolePermissionPageRepository;
    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public List<PermissionPageOperationVO> findProhibitOperation(PermissionPageSearchVO searchVO) {
        String roleId = searchVO.getRoleId();
        String permissionId = searchVO.getPermissionId();
        String url = searchVO.getUrl();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(QRolePermissionPage.rolePermissionPage.roleId.eq(roleId));
        if (!StringUtils.isEmpty(permissionId)) {
            booleanBuilder.and(QPermissionPage.permissionPage.permissionId.eq(permissionId));
        }
        if (!StringUtils.isEmpty(url)) {
            booleanBuilder.and(QPermissionPage.permissionPage.url.eq(url));
        }
        JPAQuery<PermissionPageOperationVO> permissionPageId = queryFactory.select(Projections.bean(
                PermissionPageOperationVO.class,
                QRolePermissionPage.rolePermissionPage.id,
                QPermissionPage.permissionPage.id.as("permissionPageId"),
                QPermissionPageOperation.permissionPageOperation.operationId,
                QPermissionPageOperation.permissionPageOperation.name,
                QRolePermissionPage.rolePermissionPage.status
        )).from(QRolePermissionPage.rolePermissionPage)
                .leftJoin(QPermissionPageOperation.permissionPageOperation).on(QPermissionPageOperation.permissionPageOperation.id.eq(QRolePermissionPage.rolePermissionPage.permissionPageOperationId))
                .leftJoin(QPermissionPage.permissionPage).on(QPermissionPage.permissionPage.id.eq(QPermissionPageOperation.permissionPageOperation.permissionPageId))
                .where(booleanBuilder);
        ArrayList<PermissionPageOperationVO> permissionPageVOS = Lists.newArrayList(permissionPageId.fetch());
        return permissionPageVOS;
    }

    @Override
    public Boolean addPermissionPage(PermissionPageVO permissionPageVO) {
        PermissionPage permissionPage = PermissionViewModelMapper.INSTANCE.permissionPageToEntity(permissionPageVO);
        permissionPageRepository.save(permissionPage);
        //查询页面数量
        String permissionId = permissionPageVO.getPermissionId();
        pageCount(permissionId);
        return true;
    }

    @Override
    public void pageCount(String permissionId) {
        long count = permissionPageRepository.count(QPermissionPage.permissionPage.permissionId.eq(permissionId));
        Permission permission = permissionRepository.findById(permissionId).get();
        permission.setPageCount((int) count);
        permissionRepository.save(permission);
    }

    @Override
    public List<PermissionPageVO> findPermissionPage(PermissionPageSearchVO searchVO) {
        String permissionId = searchVO.getPermissionId();
        String url = searchVO.getUrl();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (!StringUtils.isEmpty(permissionId)) {
            booleanBuilder.and(QPermissionPage.permissionPage.permissionId.eq(permissionId));
        }
        if (!StringUtils.isEmpty(url)) {
            booleanBuilder.and(QPermissionPage.permissionPage.url.eq(url));
        }
        ArrayList<PermissionPage> permissionPages = Lists.newArrayList(permissionPageRepository.findAll(booleanBuilder));
        List<PermissionPageVO> collect = permissionPages.stream().map(x -> PermissionViewModelMapper.INSTANCE.entityToPermissionPage(x)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public Boolean addPermissionPageOperation(PermissionPageOperationVO permissionPageOperationVO) {
        PermissionPageOperation permissionPageOperation = PermissionViewModelMapper.INSTANCE.permissionPageOperationToEntity(permissionPageOperationVO);
        permissionPageOperationRepository.save(permissionPageOperation);
        return true;
    }


    @Override
    public List<PermissionPageOperationVO> findPermissionPageOperation(String permissionPageId) {
        JPAQuery<PermissionPageOperationVO> where = queryFactory.select(
                Projections.bean(PermissionPageOperationVO.class,
                        QPermissionPageOperation.permissionPageOperation.id,
                        QPermissionPageOperation.permissionPageOperation.name,
                        QPermissionPageOperation.permissionPageOperation.operationId,
                        QPermissionPageOperation.permissionPageOperation.permissionPageId,
                        QPermissionPageOperation.permissionPageOperation.description
                        ))
                .from(QPermissionPageOperation.permissionPageOperation)
                .where(QPermissionPageOperation.permissionPageOperation.permissionPageId.eq(permissionPageId));
        List<PermissionPageOperationVO> permissionPageOperationVOS = where.fetch();
        return permissionPageOperationVOS;
    }

    @SuppressWarnings("all")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean switchPermissionPageOperation(String permissionPageOperationId, String roleId) {
        PermissionPageOperation permissionPageOperation = permissionPageOperationRepository.findById(permissionPageOperationId).get();
        ArrayList<RolePermissionPage> rolePermissionPages = Lists.newArrayList(rolePermissionPageRepository.findAll(QRolePermissionPage.rolePermissionPage.permissionPageOperationId.eq(permissionPageOperationId).and(QRolePermissionPage.rolePermissionPage.roleId.eq(roleId))));
        if (rolePermissionPages.size() == 0) {
            RolePermissionPage rolePermissionPage = new RolePermissionPage();
            rolePermissionPage.setRoleId(roleId);
            rolePermissionPage.setPermissionPageId(permissionPageOperation.getPermissionPageId());
            rolePermissionPage.setPermissionPageOperationId(permissionPageOperationId);
            rolePermissionPage.setStatus("0");
            rolePermissionPageRepository.save(rolePermissionPage);
        } else {
            rolePermissionPageRepository.deleteAll(rolePermissionPages);
        }
        return true;
    }

    @SuppressWarnings("all")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean switchPermissionPageOperation(List<String> permissionPageOperationIds, String roleId, String type) {
        for (String permissionPageOperationId : permissionPageOperationIds) {
            PermissionPageOperation permissionPageOperation = permissionPageOperationRepository.findById(permissionPageOperationId).get();
            ArrayList<RolePermissionPage> rolePermissionPages = Lists.newArrayList(rolePermissionPageRepository.findAll(QRolePermissionPage.rolePermissionPage.permissionPageOperationId.eq(permissionPageOperationId).and(QRolePermissionPage.rolePermissionPage.roleId.eq(roleId))));
            if ("0".equals(type)) {
                //禁用
                if (rolePermissionPages.size() == 0) {
                    RolePermissionPage rolePermissionPage = new RolePermissionPage();
                    rolePermissionPage.setRoleId(roleId);
                    rolePermissionPage.setPermissionPageId(permissionPageOperation.getPermissionPageId());
                    rolePermissionPage.setPermissionPageOperationId(permissionPageOperationId);
                    rolePermissionPage.setStatus("0");
                    rolePermissionPageRepository.save(rolePermissionPage);
                }
            } else {
                //启用
                if (rolePermissionPages.size() > 0) {
                    rolePermissionPageRepository.deleteAll(rolePermissionPages);
                }
            }
        }
        return true;
    }
}
