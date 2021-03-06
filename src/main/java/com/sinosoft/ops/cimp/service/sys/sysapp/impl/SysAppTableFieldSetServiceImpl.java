package com.sinosoft.ops.cimp.service.sys.sysapp.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.sys.sysapp.*;
import com.sinosoft.ops.cimp.entity.sys.sysapp.fieldAccess.QSysAppRoleFieldAccess;
import com.sinosoft.ops.cimp.entity.sys.systable.QSysTableField;
import com.sinosoft.ops.cimp.entity.sys.systable.SysTableField;
import com.sinosoft.ops.cimp.mapper.sys.sysapp.SysAppTableFieldSetMapper;
import com.sinosoft.ops.cimp.repository.sys.sysapp.SysAppTableFieldGroupRepository;
import com.sinosoft.ops.cimp.repository.sys.sysapp.SysAppTableFieldSetRepository;
import com.sinosoft.ops.cimp.repository.sys.sysapp.SysAppTableSetRepository;
import com.sinosoft.ops.cimp.repository.sys.sysapp.access.SysAppFieldAccessRepository;
import com.sinosoft.ops.cimp.repository.sys.systable.SysTableFieldRepository;
import com.sinosoft.ops.cimp.service.sys.sysapp.SysAppTableFieldSetService;
import com.sinosoft.ops.cimp.service.sys.sysapp.acess.SysAppFieldAccessService;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysAppTableFieldSet.*;
import com.sinosoft.ops.cimp.vo.to.sys.sysapp.sysAppTableFieldSet.SysAppTableFieldModel;
import com.sinosoft.ops.cimp.vo.to.sys.sysapp.sysAppTableFieldSet.SysAppTableFieldSetModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class SysAppTableFieldSetServiceImpl implements SysAppTableFieldSetService {

    @Autowired
    private SysAppTableFieldSetRepository fieldSetRepository;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Autowired
    private SysTableFieldRepository sysTableFieldRepository;

    @Autowired
    private SysAppTableFieldGroupRepository fieldGroupRepository;

    @Autowired
    private SysAppTableSetRepository tableSetRepository;

    @Autowired
    private SysAppFieldAccessRepository fieldAccessRepository;

    @Autowired
    private SysAppFieldAccessService fieldAccessService;

    /**
     * 获取系统应用字段集合列表
     */
    @Override
    public PaginationViewModel<SysAppTableFieldSetModel> listFieldSet(SysAppTableFieldSetSearchModel searchModel) {
        QSysAppTableFieldSet qFieldSet = QSysAppTableFieldSet.sysAppTableFieldSet;
        QSysTableField qSysTableField = QSysTableField.sysTableField;

        int pageSize = searchModel.getPageSize();
        int pageIndex = searchModel.getPageIndex();

        JPAQuery<SysAppTableFieldSetModel> query = jpaQueryFactory.select(Projections.bean(
                SysAppTableFieldSetModel.class,
                qFieldSet.id,
                qFieldSet.sysAppTableFieldGroupId,
                qFieldSet.sysTableFieldId,
                qFieldSet.name.coalesce(qSysTableField.nameCn).as("name"),
                Expressions.cases().when(qFieldSet.isNull()).then(false).otherwise(true).as("nameChanged"),
                qFieldSet.nameEn.coalesce(qSysTableField.nameEn).as("nameEn"),
                qFieldSet.sort,
                qFieldSet.html.coalesce(qSysTableField.defaultHtml).as("html"),
                Expressions.cases().when(qFieldSet.html.isNull()).then(false).otherwise(true).as("htmlChanged"),
                qFieldSet.script.coalesce(qSysTableField.defaultScript).as("script"),
                Expressions.cases().when(qFieldSet.script.isNull()).then(false).otherwise(true).as("scriptChanged")
        )).from(qFieldSet).innerJoin(qSysTableField).on(qFieldSet.sysTableFieldId.eq(qSysTableField.id));

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qFieldSet.sysAppTableFieldGroupId.eq(searchModel.getSysAppTableFieldGroupId()));
        if (StringUtils.isNotEmpty(searchModel.getName())) {
            BooleanBuilder subBuilder = new BooleanBuilder();
            subBuilder.and(qFieldSet.name.contains(searchModel.getName()));
            subBuilder.or(qFieldSet.nameEn.contains(searchModel.getName()));
            subBuilder.or(new BooleanBuilder().and(qFieldSet.name.isNull()).and(qSysTableField.nameCn.contains(searchModel.getName())));
            subBuilder.or(new BooleanBuilder().and(qFieldSet.nameEn.isNull()).and(qSysTableField.nameEn.contains(searchModel.getName())));
            builder.and(subBuilder);
        }
        if (StringUtils.isNotEmpty(searchModel.getSysAppRoleTableAccessId())) {
            QSysAppRoleFieldAccess qFieldAccess = QSysAppRoleFieldAccess.sysAppRoleFieldAccess;
            List<String> ids = jpaQueryFactory.select(qFieldAccess.sysAppTableFieldSetId).from(qFieldAccess).where(qFieldAccess.sysAppRoleTableAccessId.eq(searchModel.getSysAppRoleTableAccessId())).fetch();
            if (ids != null && ids.size() > 0) {
                builder.and(qFieldSet.id.notIn(ids));
            }
        }
        query = query.where(builder).orderBy(qFieldSet.sort.asc());
        if (pageSize > 0 && pageIndex > 0) {
            query = query.offset((pageIndex - 1) * pageSize).limit(pageSize);
        }
        QueryResults<SysAppTableFieldSetModel> results = query.fetchResults();

        return new PaginationViewModel
                .Builder<SysAppTableFieldSetModel>()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .totalCount(results.getTotal())
                .data(results.getResults())
                .build();
    }


    /**
     * 添加系统应用字段集合
     */
    @Transactional
    @Override
    public void addFieldSet(SysAppTableFieldSetAddModel addModel) {
        QSysAppTableFieldSet qFieldSet = QSysAppTableFieldSet.sysAppTableFieldSet;
        SysAppTableFieldSetMapper mapper = SysAppTableFieldSetMapper.INSTANCE;

        for (String fieldId : addModel.getSysTableFieldIds()) {
            //如果系统表字段不存在，忽略
            Optional<SysTableField> sysTableFieldOptional = sysTableFieldRepository.findById(fieldId);
            if (!sysTableFieldOptional.isPresent()) {
                continue;
            }
            SysTableField tableField = sysTableFieldOptional.get();

            //如果字段集合中已存在该表，忽略
            SysAppTableFieldSet fieldSet = jpaQueryFactory.selectFrom(qFieldSet).where(qFieldSet.sysAppTableFieldGroupId.eq(addModel.getSysAppTableFieldGroupId()).and(qFieldSet.sysTableFieldId.eq(fieldId))).fetchOne();
            if (fieldSet != null) {
                continue;
            }

            fieldSet = new SysAppTableFieldSet();
            fieldSet.setId(mapper.getNewId());
            fieldSet.setCreateId(mapper.getCurrentId(null));
            fieldSet.setCreateTime(mapper.getTime(null));
            fieldSet.setSysAppTableFieldGroupId(addModel.getSysAppTableFieldGroupId());
            fieldSet.setSysTableFieldId(fieldId);

            //排序号
            Integer sort = jpaQueryFactory.select(qFieldSet.sort.max()).from(qFieldSet).where(qFieldSet.sysAppTableFieldGroupId.eq(fieldSet.getSysAppTableFieldGroupId())).fetchOne();
            fieldSet.setSort(sort != null ? ++sort : 0);

            fieldSetRepository.saveAndFlush(fieldSet);

            //同步角色访问权限
            Optional<SysAppTableFieldGroup> fieldGroupOptional = fieldGroupRepository.findById(fieldSet.getSysAppTableFieldGroupId());
            fieldAccessService.addField(fieldGroupOptional.get().getSysAppTableSetId(), fieldSet.getId());

        }
    }

    /**
     * 删除系统应用字段集合
     */
    @Transactional
    @Override
    public void deleteFieldSet(List<String> ids) {
        if (ids == null || ids.size() == 0) {
            return;
        }

        for (String id : ids) {
            fieldSetRepository.deleteById(id);
        }

        //删除角色对系统应用表字段的访问权限信息
        fieldAccessRepository.deleteBySysAppTableFieldSetIdIn(ids);
    }

    /**
     * 修改系统应用字段集合
     */
    @Transactional
    @Override
    public boolean modifyFieldSet(SysAppTableFieldSetModifyModel modifyModel) {
        Optional<SysAppTableFieldSet> fieldSetOptional = fieldSetRepository.findById(modifyModel.getId());

        if (!fieldSetOptional.isPresent()) {
            return false;
        }

        SysAppTableFieldSet fieldSet = fieldSetOptional.get();
        SysAppTableFieldSetMapper.INSTANCE.modifyModelToFieldSet(modifyModel, fieldSet);

        Optional<SysTableField> tableFieldOptional = sysTableFieldRepository.findById(fieldSet.getSysTableFieldId());
        if (!tableFieldOptional.isPresent()) {
            return false;
        }
        SysTableField tableField = tableFieldOptional.get();
        //如果修改的字段值和系统表中的相等，则不保存
        if (StringUtils.equals(tableField.getNameCn(), fieldSet.getName())) {
            fieldSet.setName(null);
        }
        if (StringUtils.equals(tableField.getDefaultHtml(), fieldSet.getHtml())) {
            fieldSet.setHtml(null);
        }
        if (StringUtils.equals(tableField.getDefaultScript(), fieldSet.getScript())) {
            fieldSet.setScript(null);
        }

        fieldSetRepository.save(fieldSet);

        return true;
    }

    /**
     * 删除系统应用字段分组下的所有字段
     */
    @Transactional
    @Override
    public void deleteByFieldGroupIds(List<String> fieldGroupIds) {
        QSysAppTableFieldSet qFieldSet = QSysAppTableFieldSet.sysAppTableFieldSet;

        deleteFieldSet(jpaQueryFactory.select(qFieldSet.id).from(qFieldSet).where(qFieldSet.sysAppTableFieldGroupId.in(fieldGroupIds)).fetchResults().getResults());
    }

    /**
     * 系统字段列表
     */
    @Transactional
    @Override
    public List<SysAppTableFieldModel> listSysTableField(SysAppTableFieldSearchModel searchModel) {
        QSysTableField qSysTableField = QSysTableField.sysTableField;
        QSysAppTableFieldSet qFieldSet = QSysAppTableFieldSet.sysAppTableFieldSet;
        QSysAppTableSet qTableSet = QSysAppTableSet.sysAppTableSet;
        QSysAppTableFieldGroup qFieldGroup = QSysAppTableFieldGroup.sysAppTableFieldGroup;

        //获取系统表ID
        Optional<SysAppTableFieldGroup> fieldGroupOptional = fieldGroupRepository.findById(searchModel.getSysAppTableFieldGroupId());
        if (!fieldGroupOptional.isPresent()) {
            return null;
        }
        Optional<SysAppTableSet> tableSetOptional = tableSetRepository.findById(fieldGroupOptional.get().getSysAppTableSetId());
        if (!tableSetOptional.isPresent()) {
            return null;
        }
        String sysTableId = tableSetOptional.get().getSysTableId();

        //获取字段集合中app已添加的字段ID
        List<String> sysTableFieldIds = jpaQueryFactory.select(qFieldSet.sysTableFieldId).from(qFieldSet)
                .where(qFieldSet.sysAppTableFieldGroupId.in(
                        JPAExpressions.select(qFieldGroup.id).from(qFieldGroup).where(qFieldGroup.sysAppTableSetId.eq(
                                JPAExpressions.select(qFieldGroup.sysAppTableSetId).from(qFieldGroup).where(qFieldGroup.id.eq(searchModel.getSysAppTableFieldGroupId()))))))
                .fetch();

        BooleanBuilder builder = new BooleanBuilder();
        builder = builder.and(qSysTableField.sysTableId.eq(sysTableId));
        if (sysTableFieldIds.size() > 0) {
            builder = builder.and(qSysTableField.id.notIn(sysTableFieldIds));
        }
        if (StringUtils.isNotEmpty(searchModel.getNameCn())) {
            builder = builder.and(qSysTableField.nameCn.contains(searchModel.getNameCn()));
        }

        Iterable<SysTableField> iterable = sysTableFieldRepository.findAll(builder);

        return StreamSupport.stream(iterable.spliterator(), false).map(SysAppTableFieldSetMapper.INSTANCE::sysTableFieldToSysTableFieldModel).collect(Collectors.toList());
    }

    /**
     * 修改排序
     *
     * @param sortModel
     * @return
     */
    @Override
    public boolean modifySort(SysAppTableFieldSetSortModel sortModel) {
        String sourceId = sortModel.getSourceId();
        String targetId = sortModel.getTargetId();
        String type = sortModel.getType();
        String fieldGroupId = fieldSetRepository.getOne(sourceId).getSysAppTableFieldGroupId();

        if (sourceId.equals(targetId)) {
            //如果源和目标相同，不移动
            return true;
        }


        //筛选出排序介于源和目标之间的数据
        List<SysAppTableFieldSet> fieldSets = new ArrayList<>();
        QSysAppTableFieldSet qFieldSet = QSysAppTableFieldSet.sysAppTableFieldSet;
        int flag = 0;
        for (SysAppTableFieldSet fieldSet : fieldSetRepository.findAll(qFieldSet.sysAppTableFieldGroupId.eq(fieldGroupId), qFieldSet.sort.asc())) {
            if (fieldSet.getId().equals(sourceId) || fieldSet.getId().equals(targetId)) {
                fieldSets.add(fieldSet);
                if (flag == 0) {
                    flag = 1;
                } else {
                    flag = 2;
                    break;
                }
            } else if (flag == 1) {
                fieldSets.add(fieldSet);
            }
        }

        if (flag != 2) {
            return false;
        }

        //获取这组数据中最小的排序
        int minSort = fieldSets.get(0).getSort();

        //获取源和目标的位置
        int sourceIndex = fieldSets.get(0).getId().equals(sourceId) ? 0 : fieldSets.size() - 1;
        int targetIndex = fieldSets.get(0).getId().equals(targetId) ? 0 : fieldSets.size() - 1;

        //根据移动方式调整数据顺序
        if (fieldSets.get(0).getId().equals(sourceId)) {
            //源数据在第一个
            SysAppTableFieldSet source = fieldSets.remove(0);
            if ("0".equals(type)) {
                fieldSets.add(fieldSets.size() - 1, source);
            } else {
                fieldSets.add(source);
            }
        } else {
            //源数据在最后一个
            SysAppTableFieldSet source = fieldSets.remove(fieldSets.size() - 1);
            if ("0".equals(type)) {
                fieldSets.add(0, source);
            } else {
                fieldSets.add(1, source);
            }
        }

        //重新调整排序号
        for (SysAppTableFieldSet fieldSet : fieldSets) {
            fieldSet.setSort(minSort++);
        }

        fieldSetRepository.saveAll(fieldSets);

        return true;
    }
}

