package com.sinosoft.ops.cimp.service.sys.app.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.sys.app.QSysAppTableSet;
import com.sinosoft.ops.cimp.entity.sys.app.SysAppTableSet;
import com.sinosoft.ops.cimp.entity.sys.table.QSysTable;
import com.sinosoft.ops.cimp.entity.sys.table.SysTable;
import com.sinosoft.ops.cimp.mapper.sys.app.SysAppTableSetMapper;
import com.sinosoft.ops.cimp.repository.sys.app.SysAppTableSetRepository;
import com.sinosoft.ops.cimp.repository.table.SysTableRepository;
import com.sinosoft.ops.cimp.repository.table.SysTableTypeRepository;
import com.sinosoft.ops.cimp.service.sys.app.SysAppTableFieldGroupService;
import com.sinosoft.ops.cimp.service.sys.app.SysAppTableSetService;
import com.sinosoft.ops.cimp.vo.from.sys.app.sysAppTableSet.SysAppTableSearchModel;
import com.sinosoft.ops.cimp.vo.from.sys.app.sysAppTableSet.SysAppTableSetAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.app.sysAppTableSet.SysAppTableSetModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.app.sysAppTableSet.SysAppTableSetSearchModel;
import com.sinosoft.ops.cimp.vo.to.sys.app.sysAppTableSet.SysAppTableModel;
import com.sinosoft.ops.cimp.vo.to.sys.app.sysAppTableSet.SysAppTableSetModel;
import com.sinosoft.ops.cimp.vo.to.sys.app.sysAppTableSet.SysAppTableTypeModel;
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

    /**
     * 获取系统应用表集合列表
     */
    @Override
    public PaginationViewModel<SysAppTableSetModel> listTableSet(SysAppTableSetSearchModel searchModel) {
        QSysAppTableSet qTableSet = QSysAppTableSet.sysAppTableSet;
        QSysTable qSysTable = QSysTable.sysTable;

        int pageSize = searchModel.getPageSize() > 0 ? searchModel.getPageSize() : 1;
        int pageIndex = searchModel.getPageIndex() > 0 ? searchModel.getPageIndex() : 10;
        PageRequest pageRequest = PageRequest.of(pageIndex - 1, pageSize, new Sort(Sort.Direction.ASC, qTableSet.sort.getMetadata().getName()));

        BooleanBuilder builder = new BooleanBuilder();
        builder = builder.and(qTableSet.sysAppTableGroupId.eq(searchModel.getSysAppTableGroupId()));
        if (StringUtils.isNotEmpty(searchModel.getName())) {
            builder = builder.and(qTableSet.name.contains(searchModel.getName()));
        }
        Page<SysAppTableSet> page = tableSetRepository.findAll(builder, pageRequest);

        return new PaginationViewModel
                .Builder<SysAppTableSetModel>()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .totalCount(page.getTotalElements())
                .data(page.getContent().stream().map(tableSet -> {
                    SysAppTableSetModel model = SysAppTableSetMapper.INSTANCE.tableSetToTableSetModel(tableSet);
                    //如果英文名为空，去系统表中查询
                    if (StringUtils.isEmpty(model.getNameEn())) {
                        model.setNameEn(jpaQueryFactory.select(qSysTable.nameEn).from(qSysTable).where(qSysTable.id.eq(model.getSysTableId())).fetchOne());
                    }
                    return model;
                }).collect(Collectors.toList()))
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
            tableSet.setName(sysTableOptional.get().getNameCn());
            tableSet.setNameEn(sysTableOptional.get().getNameEn());

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
        for (String id : ids) {
            tableSetRepository.deleteById(id);
        }

        //删除表集合下的所有字段分组
        fieldGroupService.deleteByTableSetIds(ids);
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

        //获取集合中已存在的系统表ID
        List<String> sysTableIds = jpaQueryFactory.select(qTableSet.sysTableId).from(qTableSet).where(qTableSet.sysAppTableGroupId.eq(searchModel.getSysAppTableGroupId())).fetchResults().getResults();

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
