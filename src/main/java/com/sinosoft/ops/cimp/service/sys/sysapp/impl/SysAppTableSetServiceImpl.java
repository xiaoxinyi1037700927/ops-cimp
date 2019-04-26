package com.sinosoft.ops.cimp.service.sys.sysapp.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.sys.sysapp.QSysAppTableGroup;
import com.sinosoft.ops.cimp.entity.sys.sysapp.QSysAppTableSet;
import com.sinosoft.ops.cimp.entity.sys.sysapp.SysAppTableSet;
import com.sinosoft.ops.cimp.entity.sys.sysapp.fieldAccess.QSysAppRoleTableAccess;
import com.sinosoft.ops.cimp.entity.sys.systable.QSysTable;
import com.sinosoft.ops.cimp.entity.sys.systable.SysTable;
import com.sinosoft.ops.cimp.mapper.sys.sysapp.SysAppTableSetMapper;
import com.sinosoft.ops.cimp.repository.sys.sysapp.SysAppTableSetRepository;
import com.sinosoft.ops.cimp.repository.sys.sysapp.access.SysAppTableAccessRepository;
import com.sinosoft.ops.cimp.repository.sys.systable.SysTableRepository;
import com.sinosoft.ops.cimp.repository.sys.systable.SysTableTypeRepository;
import com.sinosoft.ops.cimp.service.sys.sysapp.SysAppTableFieldGroupService;
import com.sinosoft.ops.cimp.service.sys.sysapp.SysAppTableSetService;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysAppTableSet.SysAppTableSearchModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysAppTableSet.SysAppTableSetAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysAppTableSet.SysAppTableSetModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysAppTableSet.SysAppTableSetSearchModel;
import com.sinosoft.ops.cimp.vo.to.sys.sysapp.sysAppTableSet.SysAppTableModel;
import com.sinosoft.ops.cimp.vo.to.sys.sysapp.sysAppTableSet.SysAppTableSetModel;
import com.sinosoft.ops.cimp.vo.to.sys.sysapp.sysAppTableSet.SysAppTableTypeModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class SysAppTableSetServiceImpl implements SysAppTableSetService {

    @Autowired
    private SysAppTableSetRepository tableSetRepository;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Autowired
    private SysTableRepository sysTableRepository;

    @Autowired
    private SysAppTableFieldGroupService fieldGroupService;

    @Autowired
    private SysTableTypeRepository sysTableTypeRepository;

    @Autowired
    private SysAppTableAccessRepository tableAccessRepository;

    /**
     * 获取系统应用表集合列表
     */
    @Override
    public PaginationViewModel<SysAppTableSetModel> listTableSet(SysAppTableSetSearchModel searchModel) {
        QSysAppTableSet qTableSet = QSysAppTableSet.sysAppTableSet;
        QSysTable qSysTable = QSysTable.sysTable;
        int pageSize = searchModel.getPageSize();
        int pageIndex = searchModel.getPageIndex();

        JPAQuery<SysAppTableSetModel> query = jpaQueryFactory.select(Projections.bean(
                SysAppTableSetModel.class,
                qTableSet.id,
                qTableSet.sysAppTableGroupId,
                qTableSet.sysTableId,
                qTableSet.name.coalesce(qSysTable.nameCn).as("name"),
                Expressions.cases().when(qTableSet.name.isNull()).then(false).otherwise(true).as("nameChanged"),
                qTableSet.nameEn.coalesce(qSysTable.nameEn).as("nameEn"),
                qTableSet.sort
        )).from(qTableSet).innerJoin(qSysTable).on(qTableSet.sysTableId.eq(qSysTable.id));

        BooleanBuilder builder = new BooleanBuilder();
        builder = builder.and(qTableSet.sysAppTableGroupId.eq(searchModel.getSysAppTableGroupId()));
        if (StringUtils.isNotEmpty(searchModel.getName())) {
            BooleanBuilder subBuilder = new BooleanBuilder();
            subBuilder.and(qTableSet.name.contains(searchModel.getName()));
            subBuilder.or(qTableSet.nameEn.contains(searchModel.getName()));
            subBuilder.or(new BooleanBuilder().and(qTableSet.name.isNull()).and(qSysTable.nameCn.contains(searchModel.getName())));
            subBuilder.or(new BooleanBuilder().and(qTableSet.nameEn.isNull()).and(qSysTable.nameEn.contains(searchModel.getName())));
            builder.and(subBuilder);
        }
        if (StringUtils.isNotEmpty(searchModel.getRoleId())) {
            QSysAppRoleTableAccess qTableAccess = QSysAppRoleTableAccess.sysAppRoleTableAccess;
            List<String> ids = jpaQueryFactory.select(qTableAccess.sysAppTableSetId).from(qTableAccess).where(qTableAccess.roleId.eq(searchModel.getRoleId())).fetch();
            if (ids != null && ids.size() > 0) {
                builder.and(qTableSet.id.notIn(ids));
            }
        }

        query = query.where(builder).orderBy(qTableSet.sort.asc());
        if (pageSize > 0 && pageIndex > 0) {
            query = query.offset((pageIndex - 1) * pageSize).limit(pageSize);
        }
        QueryResults<SysAppTableSetModel> results = query.fetchResults();

        return new PaginationViewModel
                .Builder<SysAppTableSetModel>()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .totalCount(results.getTotal())
                .data(results.getResults())
                .build();
    }

    /**
     * 添加系统应用表集合
     */
    @Transactional
    @Override
    public void addTableSet(SysAppTableSetAddModel addModel) {
        QSysAppTableSet qTableSet = QSysAppTableSet.sysAppTableSet;
        SysAppTableSetMapper mapper = SysAppTableSetMapper.INSTANCE;

        for (String sysTableId : addModel.getSysTableIds()) {
            //如果系统表不存在，忽略
            Optional<SysTable> sysTableOptional = sysTableRepository.findById(sysTableId);
            if (!sysTableOptional.isPresent()) {
                continue;
            }

            //如果表集合中已存在该表，忽略
            SysAppTableSet tableSet = jpaQueryFactory.selectFrom(qTableSet).where(qTableSet.sysAppTableGroupId.eq(addModel.getSysAppTableGroupId()).and(qTableSet.sysTableId.eq(sysTableId))).fetchOne();
            if (tableSet != null) {
                continue;
            }

            tableSet = new SysAppTableSet();
            tableSet.setId(mapper.getNewId());
            tableSet.setCreateId(mapper.getCurrentId(null));
            tableSet.setCreateTime(mapper.getTime(null));
            tableSet.setSysAppTableGroupId(addModel.getSysAppTableGroupId());
            tableSet.setSysTableId(sysTableId);

            //排序号
            Integer sort = jpaQueryFactory.select(qTableSet.sort.max()).from(qTableSet).where(qTableSet.sysAppTableGroupId.eq(tableSet.getSysAppTableGroupId())).fetchOne();
            tableSet.setSort(sort != null ? ++sort : 0);

            tableSetRepository.save(tableSet);
        }
    }

    /**
     * 删除系统应用表集合
     */
    @Transactional
    @Override
    public void deleteTableSet(List<String> ids) {
        if (ids == null || ids.size() == 0) {
            return;
        }

        for (String id : ids) {
            tableSetRepository.deleteById(id);
        }

        //删除表集合下的所有字段分组
        fieldGroupService.deleteByTableSetIds(ids);

        //删除角色对该表集合的访问权限记录
        tableAccessRepository.deleteBySysAppTableSetIdIn(ids);

    }

    /**
     * 修改系统应用表集合
     */
    @Transactional
    @Override
    public boolean modifyTableSet(SysAppTableSetModifyModel modifyModel) {
        Optional<SysAppTableSet> tableSetOptional = tableSetRepository.findById(modifyModel.getId());

        if (!tableSetOptional.isPresent()) {
            return false;
        }
        SysAppTableSet tableSet = tableSetOptional.get();
        SysAppTableSetMapper.INSTANCE.modifyModelToTableSet(modifyModel, tableSet);


        Optional<SysTable> sysTableOptional = sysTableRepository.findById(tableSet.getSysTableId());
        if (!sysTableOptional.isPresent()) {
            return false;
        }
        //如果修改的表名和系统表中的相等，则不保存
        if (StringUtils.equals(tableSet.getName(), sysTableOptional.get().getNameCn())) {
            tableSet.setName(null);
        }

        tableSetRepository.save(tableSet);

        return true;
    }

    /**
     * 删除系统应用表分组下的所有表
     */
    @Transactional
    @Override
    public void deleteByTableGroupIds(List<String> tableGroupIds) {
        QSysAppTableSet qTableSet = QSysAppTableSet.sysAppTableSet;

        deleteTableSet(jpaQueryFactory.select(qTableSet.id).from(qTableSet).where(qTableSet.sysAppTableGroupId.in(tableGroupIds)).fetchResults().getResults());
    }

    /**
     * 系统表分类列表
     */
    @Override
    public List<SysAppTableTypeModel> listSysTableType() {
        return sysTableTypeRepository.findAll()
                .stream()
                .map(SysAppTableSetMapper.INSTANCE::sysTableTypeToSysAppTableTypeModel)
                .collect(Collectors.toList());
    }

    /**
     * 系统表列表
     */
    @Override
    public List<SysAppTableModel> listSysTable(SysAppTableSearchModel searchModel) {
        QSysTable qSysTable = QSysTable.sysTable;
        QSysAppTableSet qTableSet = QSysAppTableSet.sysAppTableSet;
        QSysAppTableGroup qTableGroup = QSysAppTableGroup.sysAppTableGroup;

        //获取集合中app已添加的系统表ID
        List<String> sysTableIds = jpaQueryFactory.select(qTableSet.sysTableId)
                .from(qTableSet)
                .where(qTableSet.sysAppTableGroupId.in(
                        JPAExpressions.select(qTableGroup.id).from(qTableGroup).where(qTableGroup.sysAppId.eq(
                                JPAExpressions.select(qTableGroup.sysAppId).from(qTableGroup).where(qTableGroup.id.eq(searchModel.getSysAppTableGroupId()))))))
                .fetch();

        BooleanBuilder builder = new BooleanBuilder();
        builder = builder.and(qSysTable.sysTableTypeId.eq(searchModel.getSysTableTypeId()));
        if (sysTableIds.size() > 0) {
            builder = builder.and(qSysTable.id.notIn(sysTableIds));
        }
        if (StringUtils.isNotEmpty(searchModel.getNameCn())) {
            builder = builder.and(qSysTable.nameCn.contains(searchModel.getNameCn()));
        }

        Iterable<SysTable> iterable = sysTableRepository.findAll(builder, new Sort(Sort.Direction.ASC, qSysTable.sort.getMetadata().getName()));

        return StreamSupport.stream(iterable.spliterator(), false)
                .map(SysAppTableSetMapper.INSTANCE::sysTableToSysTableModel)
                .collect(Collectors.toList());
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

        Optional<SysAppTableSet> optional1 = tableSetRepository.findById(ids.get(0));
        Optional<SysAppTableSet> optional2 = tableSetRepository.findById(ids.get(1));

        if (!optional1.isPresent() || !optional2.isPresent()) {
            return false;
        }

        SysAppTableSet tableSet1 = optional1.get();
        SysAppTableSet tableSet2 = optional2.get();

        Integer sort = tableSet1.getSort();
        tableSet1.setSort(tableSet2.getSort());
        tableSet2.setSort(sort);

        tableSetRepository.save(tableSet1);
        tableSetRepository.save(tableSet2);

        return true;
    }
}
