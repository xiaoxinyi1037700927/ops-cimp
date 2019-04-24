package com.sinosoft.ops.cimp.service.sys.sysapp.acess.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.sys.sysapp.QSysAppTableFieldSet;
import com.sinosoft.ops.cimp.entity.sys.sysapp.fieldAccess.QSysAppRoleFieldAccess;
import com.sinosoft.ops.cimp.entity.sys.sysapp.fieldAccess.SysAppRoleFieldAccess;
import com.sinosoft.ops.cimp.entity.sys.systable.QSysTableField;
import com.sinosoft.ops.cimp.mapper.sys.sysapp.access.SysAppFieldAccessMapper;
import com.sinosoft.ops.cimp.repository.sys.sysapp.access.SysAppFieldAccessRepository;
import com.sinosoft.ops.cimp.service.sys.sysapp.acess.SysAppFieldAccessService;
import com.sinosoft.ops.cimp.util.IdUtil;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.access.SysAppFieldAccessAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.access.SysAppFieldAccessModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.access.SysAppFieldAccessSearchModel;
import com.sinosoft.ops.cimp.vo.to.sys.sysapp.access.SysAppFieldAccessModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SysAppFieldAccessServiceImpl implements SysAppFieldAccessService {

    private final SysAppFieldAccessRepository fieldAccessRepository;

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public SysAppFieldAccessServiceImpl(JPAQueryFactory jpaQueryFactory, SysAppFieldAccessRepository fieldAccessRepository) {
        this.jpaQueryFactory = jpaQueryFactory;
        this.fieldAccessRepository = fieldAccessRepository;
    }

    /**
     * 获取对表字段的访问权限列表
     */
    @Override
    public PaginationViewModel<SysAppFieldAccessModel> listFieldAccess(SysAppFieldAccessSearchModel searchModel) {
        QSysAppRoleFieldAccess qFieldAccess = QSysAppRoleFieldAccess.sysAppRoleFieldAccess;
        QSysAppTableFieldSet qFieldSet = QSysAppTableFieldSet.sysAppTableFieldSet;
        QSysTableField qSysTableField = QSysTableField.sysTableField;
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
                .innerJoin(qSysTableField).on(qFieldSet.sysTableFieldId.eq(qSysTableField.id));

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

        query = query.where(builder).orderBy(qFieldSet.sort.asc());
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
     * 删除对表字段的访问权限
     */
    @Override
    public void deleteFieldAccess(List<String> ids) {
        if (ids == null || ids.size() == 0) {
            return;
        }
        for (String id : ids) {
            fieldAccessRepository.deleteById(id);
        }
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
}
