package com.sinosoft.ops.cimp.service.sys.app.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.sys.app.*;
import com.sinosoft.ops.cimp.entity.sys.table.QSysTableField;
import com.sinosoft.ops.cimp.entity.sys.table.SysTableField;
import com.sinosoft.ops.cimp.mapper.sys.app.SysAppTableFieldSetMapper;
import com.sinosoft.ops.cimp.repository.sys.app.SysAppTableFieldGroupRepository;
import com.sinosoft.ops.cimp.repository.sys.app.SysAppTableFieldSetRepository;
import com.sinosoft.ops.cimp.repository.sys.app.SysAppTableSetRepository;
import com.sinosoft.ops.cimp.repository.table.SysTableFieldRepository;
import com.sinosoft.ops.cimp.service.sys.app.SysAppTableFieldSetService;
import com.sinosoft.ops.cimp.vo.from.sys.app.sysAppTableFieldSet.SysAppTableFieldSetAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.app.sysAppTableFieldSet.SysAppTableFieldSetModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.app.sysAppTableFieldSet.SysAppTableFieldSetSearchModel;
import com.sinosoft.ops.cimp.vo.from.sys.app.sysAppTableFieldSet.SysAppTableFieldSearchModel;
import com.sinosoft.ops.cimp.vo.to.sys.app.sysAppTableFieldSet.SysAppTableFieldSetModel;
import com.sinosoft.ops.cimp.vo.to.sys.app.sysAppTableFieldSet.SysAppTableFieldModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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


    /**
     * 获取系统应用字段集合列表
     */
    @Override
    public PaginationViewModel<SysAppTableFieldSetModel> listFieldSet(SysAppTableFieldSetSearchModel searchModel) {
        QSysAppTableFieldSet qFieldSet = QSysAppTableFieldSet.sysAppTableFieldSet;
        QSysTableField qSysTableField = QSysTableField.sysTableField;

        int pageSize = searchModel.getPageSize() > 0 ? searchModel.getPageSize() : 1;
        int pageIndex = searchModel.getPageIndex() > 0 ? searchModel.getPageIndex() : 10;
        PageRequest pageRequest = PageRequest.of(pageIndex - 1, pageSize, new Sort(Sort.Direction.ASC, qFieldSet.sort.getMetadata().getName()));

        BooleanBuilder builder = new BooleanBuilder();
        builder = builder.and(qFieldSet.sysAppTableFieldGroupId.eq(searchModel.getSysAppTableFieldGroupId()));
        if (StringUtils.isNotEmpty(searchModel.getName())) {
            builder = builder.and(qFieldSet.name.contains(searchModel.getName()));
        }
        Page<SysAppTableFieldSet> page = fieldSetRepository.findAll(builder, pageRequest);

        return new PaginationViewModel
                .Builder<SysAppTableFieldSetModel>()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .totalCount(page.getTotalElements())
                .data(page.getContent().stream().map(fieldSet -> {
                    SysAppTableFieldSetModel model = SysAppTableFieldSetMapper.INSTANCE.fieldSetToFieldSetModel(fieldSet);
                    //如果英文名为空，去系统表字段表中查询
                    if (StringUtils.isNotEmpty(model.getNameEn())) {
                        model.setNameEn(jpaQueryFactory.select(qSysTableField.nameEn).from(qSysTableField).where(qSysTableField.id.eq(model.getSysTableFieldId())).fetchOne());
                    }
                    return model;
                }).collect(Collectors.toList()))
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
            fieldSet.setName(tableField.getNameCn());
            fieldSet.setNameEn(tableField.getNameEn());
            fieldSet.setScript(tableField.getDefaultScript());
            fieldSet.setHtml(tableField.getDefaultHtml());

            //排序号
            Integer sort = jpaQueryFactory.select(qFieldSet.sort.max()).from(qFieldSet).where(qFieldSet.sysAppTableFieldGroupId.eq(fieldSet.getSysAppTableFieldGroupId())).fetchOne();
            fieldSet.setSort(sort != null ? ++sort : 0);

            fieldSetRepository.save(fieldSet);
        }
    }

    /**
     * 删除系统应用字段集合
     */
    @Transactional
    @Override
    public void deleteFieldSet(List<String> ids) {
        for (String id : ids) {
            fieldSetRepository.deleteById(id);
        }
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

        //获取字段集合中已存在的字段ID
        List<String> sysTableFieldIds = jpaQueryFactory.select(qFieldSet.sysTableFieldId).from(qFieldSet).where(qFieldSet.sysAppTableFieldGroupId.eq(searchModel.getSysAppTableFieldGroupId())).fetchResults().getResults();

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
     * 交换排序
     */
    @Transactional
    @Override
    public boolean swapSort(List<String> ids) {
        if (ids == null || ids.size() != 2) {
            return false;
        }

        Optional<SysAppTableFieldSet> optional1 = fieldSetRepository.findById(ids.get(0));
        Optional<SysAppTableFieldSet> optional2 = fieldSetRepository.findById(ids.get(1));

        if (!optional1.isPresent() || !optional2.isPresent()) {
            return false;
        }

        SysAppTableFieldSet fieldSet1 = optional1.get();
        SysAppTableFieldSet fieldSet2 = optional2.get();

        Integer sort = fieldSet1.getSort();
        fieldSet1.setSort(fieldSet2.getSort());
        fieldSet2.setSort(sort);

        fieldSetRepository.save(fieldSet1);
        fieldSetRepository.save(fieldSet2);

        return true;
    }
}

