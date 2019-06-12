package com.sinosoft.ops.cimp.service.sys.sysapp.acess.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.sys.sysapp.QSysApp;
import com.sinosoft.ops.cimp.entity.sys.sysapp.QSysAppTableFieldGroup;
import com.sinosoft.ops.cimp.entity.sys.sysapp.QSysAppTableFieldSet;
import com.sinosoft.ops.cimp.entity.sys.sysapp.QSysAppTableSet;
import com.sinosoft.ops.cimp.entity.sys.sysapp.fieldAccess.QSysAppRoleFieldAccess;
import com.sinosoft.ops.cimp.entity.sys.sysapp.fieldAccess.QSysAppRoleTableAccess;
import com.sinosoft.ops.cimp.entity.sys.sysapp.fieldAccess.SysAppRoleFieldAccess;
import com.sinosoft.ops.cimp.entity.sys.sysapp.fieldAccess.SysAppRoleTableAccess;
import com.sinosoft.ops.cimp.entity.sys.systable.QSysTableField;
import com.sinosoft.ops.cimp.entity.user.UserRole;
import com.sinosoft.ops.cimp.mapper.sys.sysapp.access.SysAppFieldAccessMapper;
import com.sinosoft.ops.cimp.repository.sys.sysapp.access.SysAppFieldAccessRepository;
import com.sinosoft.ops.cimp.repository.sys.sysapp.access.SysAppTableAccessRepository;
import com.sinosoft.ops.cimp.service.sys.sysapp.acess.SysAppFieldAccessService;
import com.sinosoft.ops.cimp.util.IdUtil;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.access.SysAppFieldAccessAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.access.SysAppFieldAccessModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.access.SysAppFieldAccessSearchModel;
import com.sinosoft.ops.cimp.vo.to.sys.sysapp.access.SysAppFieldAccessModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SysAppFieldAccessServiceImpl implements SysAppFieldAccessService {

    private final SysAppFieldAccessRepository fieldAccessRepository;
    private final SysAppTableAccessRepository tableAccessRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public SysAppFieldAccessServiceImpl(JPAQueryFactory jpaQueryFactory, SysAppFieldAccessRepository fieldAccessRepository, SysAppTableAccessRepository tableAccessRepository) {
        this.jpaQueryFactory = jpaQueryFactory;
        this.fieldAccessRepository = fieldAccessRepository;
        this.tableAccessRepository = tableAccessRepository;
    }

    /**
     * 获取对表字段的访问权限列表
     */
    @Override
    public PaginationViewModel<SysAppFieldAccessModel> listFieldAccess(SysAppFieldAccessSearchModel searchModel) {
        QSysAppRoleFieldAccess qFieldAccess = QSysAppRoleFieldAccess.sysAppRoleFieldAccess;
        QSysAppTableFieldSet qFieldSet = QSysAppTableFieldSet.sysAppTableFieldSet;
        QSysTableField qSysTableField = QSysTableField.sysTableField;
        QSysAppTableFieldGroup qFieldGroup = QSysAppTableFieldGroup.sysAppTableFieldGroup;
        int pageSize = searchModel.getPageSize();
        int pageIndex = searchModel.getPageIndex();

        JPAQuery<SysAppFieldAccessModel> query = jpaQueryFactory.select(Projections.bean(
                SysAppFieldAccessModel.class,
                qFieldAccess.id,
                qFieldSet.name.coalesce(qSysTableField.nameCn).as("nameCn"),
                qFieldSet.nameEn.coalesce(qSysTableField.nameEn).as("nameEn"),
                qFieldAccess.canRead,
                qFieldAccess.canWrite,
                qFieldSet.id.as("sysAppTableFieldSetId"),
                qFieldSet.sysTableFieldId
        )).from(qFieldAccess).innerJoin(qFieldSet).on(qFieldSet.id.eq(qFieldAccess.sysAppTableFieldSetId))
                .innerJoin(qSysTableField).on(qFieldSet.sysTableFieldId.eq(qSysTableField.id))
                .innerJoin(qFieldGroup).on(qFieldGroup.id.eq(qFieldSet.sysAppTableFieldGroupId));

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qFieldAccess.sysAppRoleTableAccessId.eq(searchModel.getSysAppRoleTableAccessId()));
        if (StringUtils.isNotEmpty(searchModel.getName())) {
            BooleanBuilder subBuilder = new BooleanBuilder();
            subBuilder.and(qFieldSet.name.contains(searchModel.getName()));
            subBuilder.or(qFieldSet.nameEn.contains(searchModel.getName()));
            subBuilder.or(new BooleanBuilder().and(qFieldSet.name.isNull()).and(qSysTableField.nameCn.contains(searchModel.getName())));
            subBuilder.or(new BooleanBuilder().and(qFieldSet.nameEn.isNull()).and(qSysTableField.nameEn.contains(searchModel.getName())));
            builder.and(subBuilder);
        }

        query = query.where(builder).orderBy(qFieldGroup.sort.asc(), qFieldSet.sort.asc());
        if (pageSize > 0 && pageIndex > 0) {
            query = query.offset((pageIndex - 1) * pageSize).limit(pageSize);
        }
        QueryResults<SysAppFieldAccessModel> results = query.fetchResults();

        return new PaginationViewModel
                .Builder<SysAppFieldAccessModel>()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .totalCount(results.getTotal())
                .data(results.getResults())
                .build();
    }

    /**
     * 添加对表字段的访问权限
     */
    @Override
    public void addFieldAccess(SysAppFieldAccessAddModel addModel) {
        List<SysAppRoleFieldAccess> fieldAccesses = new ArrayList<>();

        for (String fieldSetId : addModel.getSysAppTableFieldSetIds()) {
            SysAppRoleFieldAccess fieldAccess = SysAppFieldAccessMapper.INSTANCE.addModelToFieldAccess(addModel);
            fieldAccess.setId(IdUtil.uuid());
            fieldAccess.setSysAppTableFieldSetId(fieldSetId);
            fieldAccesses.add(fieldAccess);
        }

        fieldAccessRepository.saveAll(fieldAccesses);

    }

    /**
     * 修改对表字段的访问权限
     */
    @Override
    public boolean modifyFieldAccess(SysAppFieldAccessModifyModel modifyModel) {
        List<SysAppRoleFieldAccess> fieldtableAccesses = new ArrayList<>();
        for (String id : modifyModel.getIds()) {
            Optional<SysAppRoleFieldAccess> fieldAccessOptional = fieldAccessRepository.findById(id);
            fieldAccessOptional.ifPresent(fieldAccess -> {
                SysAppFieldAccessMapper.INSTANCE.modifyModelToFieldAccess(modifyModel, fieldAccess);
                fieldtableAccesses.add(fieldAccess);
            });
        }
        fieldAccessRepository.saveAll(fieldtableAccesses);

        return true;
    }

    /**
     * 获取用户对app下表字段的访问权限
     */
    @Override
    public Map<String, SysAppFieldAccessModel> getFieldAccess(String appCode, String sysTableId) {
        //获取登录用户的所有角色id
        List<String> roleIds = SecurityUtils.getSubject().getCurrentUserRole()
                .stream().map(UserRole::getRoleId).collect(Collectors.toList());

        QSysApp qSysApp = QSysApp.sysApp;
        QSysAppTableSet qTableSet = QSysAppTableSet.sysAppTableSet;
        QSysAppTableFieldSet qFieldSet = QSysAppTableFieldSet.sysAppTableFieldSet;
        QSysTableField qSysTableField = QSysTableField.sysTableField;
        QSysAppRoleTableAccess qTableAccess = QSysAppRoleTableAccess.sysAppRoleTableAccess;
        QSysAppRoleFieldAccess qFieldAccess = QSysAppRoleFieldAccess.sysAppRoleFieldAccess;

        Map<String, SysAppFieldAccessModel> result = new HashMap<>();

        jpaQueryFactory.select(Projections.bean(
                SysAppFieldAccessModel.class,
                qFieldSet.nameEn.coalesce(qSysTableField.nameEn).as("nameEn"),
                qFieldAccess.canRead,
                qFieldAccess.canWrite,
                qFieldSet.sysTableFieldId
        )).from(qTableAccess).innerJoin(qSysApp).on(qTableAccess.sysAppId.eq(qSysApp.id))
                .innerJoin(qTableSet).on(qTableSet.id.eq(qTableAccess.sysAppTableSetId))
                .innerJoin(qFieldAccess).on(qFieldAccess.sysAppRoleTableAccessId.eq(qTableAccess.id))
                .innerJoin(qFieldSet).on(qFieldSet.id.eq(qFieldAccess.sysAppTableFieldSetId))
                .innerJoin(qSysTableField).on(qFieldSet.sysTableFieldId.eq(qSysTableField.id))
                .where(new BooleanBuilder().and(qSysApp.code.eq(Integer.parseInt(appCode)))
                        .and(qTableAccess.roleId.in(roleIds))
                        .and(qTableSet.sysTableId.eq(sysTableId))
                ).fetch().forEach(model -> {
            if (result.containsKey(model.getSysTableFieldId())) {
                //合并权限
                SysAppFieldAccessModel old = result.get(model.getSysTableFieldId());
                old.setCanRead(old.getCanRead() | model.getCanRead());
                old.setCanWrite(old.getCanWrite() | model.getCanWrite());
            } else {
                result.put(model.getSysTableFieldId(), model);
            }
        });

        return result;
    }

    /**
     * 系统应用添加字段同步至角色访问权限
     *
     * @param tableSetId
     * @param fieldSetId
     */
    @Override
    public void addField(String tableSetId, String fieldSetId) {
        String userId = SecurityUtils.getSubject().getCurrentUser().getId();
        List<SysAppRoleTableAccess> tableAccesses = tableAccessRepository.findBySysAppTableSetId(tableSetId);

        List<SysAppRoleFieldAccess> fieldAccesses = new ArrayList<>();
        for (SysAppRoleTableAccess tableAccess : tableAccesses) {
            SysAppRoleFieldAccess fieldAccess = new SysAppRoleFieldAccess();
            fieldAccess.setSysAppTableFieldSetId(fieldSetId);
            fieldAccess.setSysAppRoleTableAccessId(tableAccess.getId());
            fieldAccess.setCanRead(true);
            fieldAccess.setCanWrite(true);
            fieldAccess.setCreateId(userId);
            fieldAccess.setCreateTime(new Date());

            fieldAccesses.add(fieldAccess);
        }

        if (fieldAccesses.size() > 0) {
            fieldAccessRepository.saveAll(fieldAccesses);
        }
    }

    /**
     * 系统应用删除字段同步至角色访问权限
     *
     * @param fieldSetIds
     */
    @Override
    public void deleteField(List<String> fieldSetIds) {
        fieldAccessRepository.deleteBySysAppTableFieldSetIdIn(fieldSetIds);
    }
}
