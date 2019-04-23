package com.sinosoft.ops.cimp.service.sys.sysapp.acess.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.sys.sysapp.QSysAppTableFieldSet;
import com.sinosoft.ops.cimp.entity.sys.sysapp.SysAppTableFieldSet;
import com.sinosoft.ops.cimp.entity.sys.sysapp.fieldAccess.QSysAppRoleFieldAccess;
import com.sinosoft.ops.cimp.entity.sys.sysapp.fieldAccess.QSysAppRoleTableAccess;
import com.sinosoft.ops.cimp.entity.sys.sysapp.fieldAccess.SysAppRoleFieldAccess;
import com.sinosoft.ops.cimp.entity.sys.sysapp.fieldAccess.SysAppRoleTableAccess;
import com.sinosoft.ops.cimp.entity.sys.systable.QSysTableField;
import com.sinosoft.ops.cimp.mapper.sys.sysapp.access.SysAppFieldAccessMapper;
import com.sinosoft.ops.cimp.mapper.sys.sysapp.access.SysAppTableAccessMapper;
import com.sinosoft.ops.cimp.repository.sys.sysapp.SysAppTableFieldSetRepository;
import com.sinosoft.ops.cimp.repository.sys.sysapp.access.SysAppFieldAccessRepository;
import com.sinosoft.ops.cimp.service.sys.sysapp.SysAppTableFieldSetService;
import com.sinosoft.ops.cimp.service.sys.sysapp.acess.SysAppFieldAccessService;
import com.sinosoft.ops.cimp.util.IdUtil;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.access.SysAppFieldAccessAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.access.SysAppFieldAccessModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.access.SysAppFieldAccessSearchModel;
import com.sinosoft.ops.cimp.vo.to.sys.sysapp.access.SysAppFieldAccessModel;
import com.sinosoft.ops.cimp.vo.to.sys.sysapp.access.SysAppTableAccessModel;
import com.sinosoft.ops.cimp.vo.to.sys.sysapp.sysAppTableFieldSet.SysAppTableFieldSetModel;
import com.sinosoft.ops.cimp.vo.to.sys.sysapp.sysAppTableSet.SysAppTableSetModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SysAppFieldAccessServiceImpl implements SysAppFieldAccessService {

    @Autowired
    private SysAppFieldAccessRepository fieldAccessRepository;

    @Autowired
    private SysAppTableFieldSetService fieldSetService;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    /**
     * 获取对表字段的访问权限列表
     */
    @Override
    public PaginationViewModel<SysAppFieldAccessModel> listFieldAccess(SysAppFieldAccessSearchModel searchModel) {
        QSysAppRoleFieldAccess qFieldAccess = QSysAppRoleFieldAccess.sysAppRoleFieldAccess;
        QSysAppTableFieldSet qFieldSet = QSysAppTableFieldSet.sysAppTableFieldSet;
        QSysTableField qSysTableField = QSysTableField.sysTableField;
        int pageSize = searchModel.getPageSize() > 0 ? searchModel.getPageSize() : 1;
        int pageIndex = searchModel.getPageIndex() > 0 ? searchModel.getPageIndex() : 10;

        JPAQuery<SysAppRoleFieldAccess> query = jpaQueryFactory.select(qFieldAccess).from(qFieldAccess)
                .innerJoin(qFieldSet).on(qFieldSet.id.eq(qFieldAccess.sysAppTableFieldSetId));

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qFieldAccess.sysAppRoleTableAccessId.eq(searchModel.getSysAppRoleTableAccessId()));
        if (StringUtils.isNotEmpty(searchModel.getName())) {
            query.innerJoin(qSysTableField).on(qFieldSet.sysTableFieldId.eq(qSysTableField.id));

            BooleanBuilder subBuilder = new BooleanBuilder();
            subBuilder.and(qFieldSet.name.contains(searchModel.getName()));
            subBuilder.or(qFieldSet.nameEn.contains(searchModel.getName()));
            subBuilder.or(new BooleanBuilder().and(qFieldSet.name.isNull()).and(qSysTableField.nameCn.contains(searchModel.getName())));
            subBuilder.or(new BooleanBuilder().and(qFieldSet.nameEn.isNull()).and(qSysTableField.nameEn.contains(searchModel.getName())));
            builder.and(subBuilder);
        }
        QueryResults<SysAppRoleFieldAccess> results = query.where(builder)
                .orderBy(qFieldSet.sort.asc())
                .offset((pageIndex - 1) * pageSize)
                .limit(pageSize)
                .fetchResults();

        return new PaginationViewModel
                .Builder<SysAppFieldAccessModel>()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .totalCount(results.getTotal())
                .data(results.getResults().stream().map(fieldAccess -> {
                    SysAppFieldAccessModel fieldAccessModel = SysAppFieldAccessMapper.INSTANCE.fieldAccessToFieldAccessModel(fieldAccess);

                    SysAppTableFieldSetModel fieldSetModel = fieldSetService.getById(fieldAccess.getSysAppTableFieldSetId());
                    if (null != fieldSetModel) {
                        fieldAccessModel.setNameCn(fieldSetModel.getName());
                        fieldAccessModel.setNameEn(fieldSetModel.getNameEn());
                    }
                    return fieldAccessModel;
                }).collect(Collectors.toList()))
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
