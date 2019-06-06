package com.sinosoft.ops.cimp.service.sys.sysapp.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.sys.sysapp.QSysAppTableFieldGroup;
import com.sinosoft.ops.cimp.entity.sys.sysapp.SysAppTableFieldGroup;
import com.sinosoft.ops.cimp.mapper.sys.sysapp.SysAppTableFieldGroupMapper;
import com.sinosoft.ops.cimp.repository.sys.sysapp.SysAppTableFieldGroupRepository;
import com.sinosoft.ops.cimp.service.sys.sysapp.SysAppTableFieldGroupService;
import com.sinosoft.ops.cimp.service.sys.sysapp.SysAppTableFieldSetService;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysAppTableFieldGroup.SysAppTableFieldGroupAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysAppTableFieldGroup.SysAppTableFieldGroupModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysAppTableFieldGroup.SysAppTableFieldGroupSearchModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysAppTableFieldGroup.SysAppTableFieldGroupSortModel;
import com.sinosoft.ops.cimp.vo.to.sys.sysapp.sysAppTableFieldGroup.SysAppTableFieldGroupModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
        int pageSize = searchModel.getPageSize();
        int pageIndex = searchModel.getPageIndex();
        //是否分页
        boolean isPaging = pageSize > 0 && pageIndex > 0;
        Sort sort = new Sort(Sort.Direction.ASC, qFieldGroup.sort.getMetadata().getName());


        BooleanBuilder builder = new BooleanBuilder();
        builder = builder.and(qFieldGroup.sysAppTableSetId.eq(searchModel.getSysAppTableSetId()));
        if (StringUtils.isNotEmpty(searchModel.getName())) {
            builder = builder.and(qFieldGroup.name.contains(searchModel.getName()));
        }

        List<SysAppTableFieldGroupModel> fieldGroupModels = null;
        long total = 0;
        if (isPaging) {
            PageRequest pageRequest = PageRequest.of(pageIndex - 1, pageSize, sort);
            Page<SysAppTableFieldGroup> page = fieldGroupRepository.findAll(builder, pageRequest);
            fieldGroupModels = page.getContent().stream().map(SysAppTableFieldGroupMapper.INSTANCE::fieldGroupToFieldGroupModel).collect(Collectors.toList());
            total = page.getTotalElements();
        } else {
            Iterable<SysAppTableFieldGroup> iterable = fieldGroupRepository.findAll(builder, sort);
            fieldGroupModels = StreamSupport.stream(iterable.spliterator(), false).map(SysAppTableFieldGroupMapper.INSTANCE::fieldGroupToFieldGroupModel).collect(Collectors.toList());
            total = fieldGroupModels.size();
        }

        return new PaginationViewModel
                .Builder<SysAppTableFieldGroupModel>()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .totalCount(total)
                .data(fieldGroupModels)
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
        if (ids == null || ids.size() == 0) {
            return;
        }

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
     * 修改排序
     *
     * @param sortModel
     * @return
     */
    @Override
    public boolean modifySort(SysAppTableFieldGroupSortModel sortModel) {
        String sourceId = sortModel.getSourceId();
        String targetId = sortModel.getTargetId();
        String type = sortModel.getType();
        String tableSetId = fieldGroupRepository.getOne(sourceId).getSysAppTableSetId();

        if (sourceId.equals(targetId)) {
            //如果源和目标相同，不移动
            return true;
        }


        //筛选出排序介于源和目标之间的数据
        List<SysAppTableFieldGroup> fieldGroups = new ArrayList<>();
        QSysAppTableFieldGroup qFieldGroup = QSysAppTableFieldGroup.sysAppTableFieldGroup;
        int flag = 0;
        for (SysAppTableFieldGroup fieldGroup : fieldGroupRepository.findAll(qFieldGroup.sysAppTableSetId.eq(tableSetId), qFieldGroup.sort.asc())) {
            if (fieldGroup.getId().equals(sourceId) || fieldGroup.getId().equals(targetId)) {
                fieldGroups.add(fieldGroup);
                if (flag == 0) {
                    flag = 1;
                } else {
                    flag = 2;
                    break;
                }
            } else if (flag == 1) {
                fieldGroups.add(fieldGroup);
            }
        }

        if (flag != 2) {
            return false;
        }

        //获取这组数据中最小的排序
        int minSort = fieldGroups.get(0).getSort();

        //获取源和目标的位置
        int sourceIndex = fieldGroups.get(0).getId().equals(sourceId) ? 0 : fieldGroups.size() - 1;
        int targetIndex = fieldGroups.get(0).getId().equals(targetId) ? 0 : fieldGroups.size() - 1;

        //根据移动方式调整数据顺序
        if (fieldGroups.get(0).getId().equals(sourceId)) {
            //源数据在第一个
            SysAppTableFieldGroup source = fieldGroups.remove(0);
            if ("0".equals(type)) {
                fieldGroups.add(fieldGroups.size() - 1, source);
            } else {
                fieldGroups.add(source);
            }
        } else {
            //源数据在最后一个
            SysAppTableFieldGroup source = fieldGroups.remove(fieldGroups.size() - 1);
            if ("0".equals(type)) {
                fieldGroups.add(0, source);
            } else {
                fieldGroups.add(1, source);
            }
        }

        //重新调整排序号
        for (SysAppTableFieldGroup fieldGroup : fieldGroups) {
            fieldGroup.setSort(minSort++);
        }

        fieldGroupRepository.saveAll(fieldGroups);

        return true;
    }

}
