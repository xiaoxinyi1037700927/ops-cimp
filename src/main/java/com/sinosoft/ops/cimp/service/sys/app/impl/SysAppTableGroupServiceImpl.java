package com.sinosoft.ops.cimp.service.sys.app.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.sys.app.QSysAppTableGroup;
import com.sinosoft.ops.cimp.entity.sys.app.SysAppTableGroup;
import com.sinosoft.ops.cimp.mapper.sys.app.SysAppMapper;
import com.sinosoft.ops.cimp.mapper.sys.app.SysAppTableGroupMapper;
import com.sinosoft.ops.cimp.repository.sys.app.SysAppTableGroupRepository;
import com.sinosoft.ops.cimp.service.sys.app.SysAppTableGroupService;
import com.sinosoft.ops.cimp.service.sys.app.SysAppTableSetService;
import com.sinosoft.ops.cimp.vo.from.sys.app.sysAppTableGroup.SysAppTableGroupAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.app.sysAppTableGroup.SysAppTableGroupModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.app.sysAppTableGroup.SysAppTableGroupSearchModel;
import com.sinosoft.ops.cimp.vo.to.sys.app.sysApp.SysAppModel;
import com.sinosoft.ops.cimp.vo.to.sys.app.sysAppTableGroup.SysAppTableGroupModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        int pageSize = searchModel.getPageSize() > 0 ? searchModel.getPageSize() : 1;
        int pageIndex = searchModel.getPageIndex() > 0 ? searchModel.getPageIndex() : 10;
        PageRequest pageRequest = PageRequest.of(pageIndex - 1, pageSize, new Sort(Sort.Direction.ASC, qTableGroup.sort.getMetadata().getName()));

        BooleanBuilder builder = new BooleanBuilder();
        builder = builder.and(qTableGroup.sysAppId.eq(searchModel.getSysAppId()));
        if (StringUtils.isNotEmpty(searchModel.getName())) {
            builder = builder.and(qTableGroup.name.contains(searchModel.getName()));
        }

        Page<SysAppTableGroup> page = tableGroupRepository.findAll(builder, pageRequest);

        return new PaginationViewModel
                .Builder<SysAppTableGroupModel>()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .totalCount(page.getTotalElements())
                .data(page.getContent().stream().map(SysAppTableGroupMapper.INSTANCE::tableGroupToTableGroupModel).collect(Collectors.toList()))
                .build();
    }

    /**
     * 添加系统应用表分组
     */
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
    @Override
    public void deleteTableGroup(List<String> ids) {
        for (String id : ids) {
            tableGroupRepository.deleteById(id);
        }

        //删除表分组下所有的表集合
        tableSetService.deleteByTableGroupIds(ids);
    }

    /**
     * 修改系统应用表分组
     */
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
    @Override
    public void deleteBySysAppIds(List<String> sysAppIds) {
        QSysAppTableGroup qTableGroup = QSysAppTableGroup.sysAppTableGroup;

        deleteTableGroup(jpaQueryFactory.select(qTableGroup.id).from(qTableGroup).where(qTableGroup.sysAppId.in(sysAppIds)).fetchResults().getResults());
    }
}
