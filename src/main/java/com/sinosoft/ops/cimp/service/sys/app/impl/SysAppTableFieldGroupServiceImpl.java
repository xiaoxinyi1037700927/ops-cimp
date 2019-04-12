package com.sinosoft.ops.cimp.service.sys.app.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.sys.app.QSysAppTableFieldGroup;
import com.sinosoft.ops.cimp.entity.sys.app.QSysAppTableGroup;
import com.sinosoft.ops.cimp.entity.sys.app.SysAppTableFieldGroup;
import com.sinosoft.ops.cimp.entity.sys.app.SysAppTableGroup;
import com.sinosoft.ops.cimp.mapper.sys.app.SysAppTableFieldGroupMapper;
import com.sinosoft.ops.cimp.mapper.sys.app.SysAppTableGroupMapper;
import com.sinosoft.ops.cimp.repository.sys.app.SysAppRepository;
import com.sinosoft.ops.cimp.repository.sys.app.SysAppTableFieldGroupRepository;
import com.sinosoft.ops.cimp.repository.sys.app.SysAppTableFieldSetRepository;
import com.sinosoft.ops.cimp.service.sys.app.SysAppTableFieldGroupService;
import com.sinosoft.ops.cimp.service.sys.app.SysAppTableFieldSetService;
import com.sinosoft.ops.cimp.vo.from.sys.app.sysAppTableFieldGroup.SysAppTableFieldGroupAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.app.sysAppTableFieldGroup.SysAppTableFieldGroupModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.app.sysAppTableFieldGroup.SysAppTableFieldGroupSearchModel;
import com.sinosoft.ops.cimp.vo.to.sys.app.sysAppTableFieldGroup.SysAppTableFieldGroupModel;
import com.sinosoft.ops.cimp.vo.to.sys.app.sysAppTableGroup.SysAppTableGroupModel;
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

@Service
public class SysAppTableFieldGroupServiceImpl implements SysAppTableFieldGroupService {

    @Autowired
    private SysAppTableFieldGroupRepository fieldGroupRepository;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Autowired
    private SysAppTableFieldSetService fieldSetService;


    /**
     * 获取系统应用字段分组列表
     */
    @Override
    public PaginationViewModel<SysAppTableFieldGroupModel> listFieldGroup(SysAppTableFieldGroupSearchModel searchModel) {
        QSysAppTableFieldGroup qFieldGroup = QSysAppTableFieldGroup.sysAppTableFieldGroup;

        int pageSize = searchModel.getPageSize() > 0 ? searchModel.getPageSize() : 1;
        int pageIndex = searchModel.getPageIndex() > 0 ? searchModel.getPageIndex() : 10;
        PageRequest pageRequest = PageRequest.of(pageIndex - 1, pageSize, new Sort(Sort.Direction.ASC, qFieldGroup.sort.getMetadata().getName()));

        BooleanBuilder builder = new BooleanBuilder();
        builder = builder.and(qFieldGroup.sysAppTableSetId.eq(searchModel.getSysAppTableSetId()));
        if (StringUtils.isNotEmpty(searchModel.getName())) {
            builder = builder.and(qFieldGroup.name.contains(searchModel.getName()));
        }

        Page<SysAppTableFieldGroup> page = fieldGroupRepository.findAll(builder, pageRequest);

        return new PaginationViewModel
                .Builder<SysAppTableFieldGroupModel>()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .totalCount(page.getTotalElements())
                .data(page.getContent().stream().map(SysAppTableFieldGroupMapper.INSTANCE::fieldGroupToFieldGroupModel).collect(Collectors.toList()))
                .build();
    }

    /**
     * 添加系统应用字段分组
     */
    @Transactional
    @Override
    public void addFieldGroup(SysAppTableFieldGroupAddModel addModel) {
        SysAppTableFieldGroup fieldGroup = SysAppTableFieldGroupMapper.INSTANCE.addModelToFieldGroup(addModel);

        QSysAppTableFieldGroup qFieldGroup = QSysAppTableFieldGroup.sysAppTableFieldGroup;
        Integer sort = jpaQueryFactory.select(qFieldGroup.sort.max()).from(qFieldGroup).where(qFieldGroup.sysAppTableSetId.eq(fieldGroup.getSysAppTableSetId())).fetchOne();
        fieldGroup.setSort(sort != null ? ++sort : 0);

        fieldGroupRepository.save(fieldGroup);
    }

    /**
     * 删除系统应用字段分组
     */
    @Transactional
    @Override
    public void deleteFieldGroup(List<String> ids) {
        for (String id : ids) {
            fieldGroupRepository.deleteById(id);
        }

        //删除字段集合下所有的字段
        fieldSetService.deleteByFieldGroupIds(ids);
    }

    /**
     * 修改系统应用字段分组
     */
    @Transactional
    @Override
    public boolean modifyFieldGroup(SysAppTableFieldGroupModifyModel modifyModel) {
        Optional<SysAppTableFieldGroup> fieldGroupOptional = fieldGroupRepository.findById(modifyModel.getId());

        if (!fieldGroupOptional.isPresent()) {
            return false;
        }

        SysAppTableFieldGroup fieldGroup = fieldGroupOptional.get();
        SysAppTableFieldGroupMapper.INSTANCE.modifyModelToFieldGroup(modifyModel, fieldGroup);
        fieldGroupRepository.save(fieldGroup);

        return true;
    }

    /**
     * 删除系统应用表集合下的所有字段分组
     */
    @Transactional
    @Override
    public void deleteByTableSetIds(List<String> tableSetIds) {
        QSysAppTableFieldGroup qFieldGroup = QSysAppTableFieldGroup.sysAppTableFieldGroup;

        deleteFieldGroup(jpaQueryFactory.select(qFieldGroup.id).from(qFieldGroup).where(qFieldGroup.sysAppTableSetId.in(tableSetIds)).fetchResults().getResults());
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

        Optional<SysAppTableFieldGroup> optional1 = fieldGroupRepository.findById(ids.get(0));
        Optional<SysAppTableFieldGroup> optional2 = fieldGroupRepository.findById(ids.get(1));

        if (!optional1.isPresent() || !optional2.isPresent()) {
            return false;
        }

        SysAppTableFieldGroup fieldGroup1 = optional1.get();
        SysAppTableFieldGroup fieldGroup2 = optional2.get();

        Integer sort = fieldGroup1.getSort();
        fieldGroup1.setSort(fieldGroup2.getSort());
        fieldGroup2.setSort(sort);

        fieldGroupRepository.save(fieldGroup1);
        fieldGroupRepository.save(fieldGroup2);

        return true;
    }
}
