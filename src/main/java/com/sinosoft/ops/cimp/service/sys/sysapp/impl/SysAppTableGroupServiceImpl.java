package com.sinosoft.ops.cimp.service.sys.sysapp.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.sys.sysapp.QSysAppTableGroup;
import com.sinosoft.ops.cimp.entity.sys.sysapp.SysAppTableGroup;
import com.sinosoft.ops.cimp.mapper.sys.sysapp.SysAppTableGroupMapper;
import com.sinosoft.ops.cimp.repository.sys.sysapp.SysAppTableGroupRepository;
import com.sinosoft.ops.cimp.service.sys.sysapp.SysAppTableGroupService;
import com.sinosoft.ops.cimp.service.sys.sysapp.SysAppTableSetService;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysAppTableGroup.SysAppTableGroupAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysAppTableGroup.SysAppTableGroupModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysAppTableGroup.SysAppTableGroupSearchModel;
import com.sinosoft.ops.cimp.vo.to.sys.sysapp.sysAppTableGroup.SysAppTableGroupModel;
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
public class SysAppTableGroupServiceImpl implements SysAppTableGroupService {

    @Autowired
    private SysAppTableGroupRepository tableGroupRepository;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Autowired
    private SysAppTableSetService tableSetService;

    /**
     * 获取系统应用表分组列表
     */
    @Override
    public PaginationViewModel<SysAppTableGroupModel> listTableGroup(SysAppTableGroupSearchModel searchModel) {
        QSysAppTableGroup qTableGroup = QSysAppTableGroup.sysAppTableGroup;
        int pageSize = searchModel.getPageSize();
        int pageIndex = searchModel.getPageIndex();
        Sort sort = new Sort(Sort.Direction.ASC, qTableGroup.sort.getMetadata().getName());
        //是否分页
        boolean isPaging = pageSize > 0 && pageIndex > 0;

        BooleanBuilder builder = new BooleanBuilder();
        builder = builder.and(qTableGroup.sysAppId.eq(searchModel.getSysAppId()));
        if (StringUtils.isNotEmpty(searchModel.getName())) {
            builder = builder.and(qTableGroup.name.contains(searchModel.getName()));
        }

        List<SysAppTableGroupModel> tableGroupModels = null;
        long total = 0;
        if (isPaging) {
            PageRequest pageRequest = PageRequest.of(pageIndex - 1, pageSize, sort);
            Page<SysAppTableGroup> page = tableGroupRepository.findAll(builder, pageRequest);
            tableGroupModels = page.getContent().stream().map(SysAppTableGroupMapper.INSTANCE::tableGroupToTableGroupModel).collect(Collectors.toList());
            total = page.getTotalElements();
        } else {
            Iterable<SysAppTableGroup> iterable = tableGroupRepository.findAll(builder, sort);
            tableGroupModels = StreamSupport.stream(iterable.spliterator(), false).map(SysAppTableGroupMapper.INSTANCE::tableGroupToTableGroupModel).collect(Collectors.toList());
            total = tableGroupModels.size();
        }

        return new PaginationViewModel
                .Builder<SysAppTableGroupModel>()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .totalCount(total)
                .data(tableGroupModels)
                .build();
    }

    /**
     * 添加系统应用表分组
     */
    @Transactional
    @Override
    public void addTableGroup(SysAppTableGroupAddModel addModel) {
        SysAppTableGroup tableGroup = SysAppTableGroupMapper.INSTANCE.addModelToTableGroup(addModel);

        QSysAppTableGroup qTableGroup = QSysAppTableGroup.sysAppTableGroup;
        Integer sort = jpaQueryFactory.select(qTableGroup.sort.max()).from(qTableGroup).where(qTableGroup.sysAppId.eq(tableGroup.getSysAppId())).fetchOne();
        tableGroup.setSort(sort != null ? ++sort : 0);

        tableGroupRepository.save(tableGroup);
    }

    /**
     * 删除系统应用表分组
     */
    @Transactional
    @Override
    public void deleteTableGroup(List<String> ids) {
        if (ids == null || ids.size() == 0) {
            return;
        }

        for (String id : ids) {
            tableGroupRepository.deleteById(id);
        }

        //删除表分组下所有的表集合
        tableSetService.deleteByTableGroupIds(ids);
    }

    /**
     * 修改系统应用表分组
     */
    @Transactional
    @Override
    public boolean modifyTableGroup(SysAppTableGroupModifyModel modifyModel) {
        Optional<SysAppTableGroup> tableGroupOptional = tableGroupRepository.findById(modifyModel.getId());

        if (!tableGroupOptional.isPresent()) {
            return false;
        }

        SysAppTableGroup tableGroup = tableGroupOptional.get();
        SysAppTableGroupMapper.INSTANCE.modifyModelToTableGroup(modifyModel, tableGroup);
        tableGroupRepository.save(tableGroup);

        return true;
    }

    /**
     * 删除系统应用下的所有分组
     */
    @Transactional
    @Override
    public void deleteBySysAppIds(List<String> sysAppIds) {
        QSysAppTableGroup qTableGroup = QSysAppTableGroup.sysAppTableGroup;

        deleteTableGroup(jpaQueryFactory.select(qTableGroup.id).from(qTableGroup).where(qTableGroup.sysAppId.in(sysAppIds)).fetchResults().getResults());
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

        Optional<SysAppTableGroup> optional1 = tableGroupRepository.findById(ids.get(0));
        Optional<SysAppTableGroup> optional2 = tableGroupRepository.findById(ids.get(1));

        if (!optional1.isPresent() || !optional2.isPresent()) {
            return false;
        }

        SysAppTableGroup tableGroup1 = optional1.get();
        SysAppTableGroup tableGroup2 = optional2.get();

        Integer sort = tableGroup1.getSort();
        tableGroup1.setSort(tableGroup2.getSort());
        tableGroup2.setSort(sort);

        tableGroupRepository.save(tableGroup1);
        tableGroupRepository.save(tableGroup2);

        return true;
    }
}
