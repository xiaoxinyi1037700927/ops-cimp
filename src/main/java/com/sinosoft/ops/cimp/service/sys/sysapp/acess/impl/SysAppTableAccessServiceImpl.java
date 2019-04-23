package com.sinosoft.ops.cimp.service.sys.sysapp.acess.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.sys.sysapp.QSysAppTableSet;
import com.sinosoft.ops.cimp.entity.sys.sysapp.fieldAccess.QSysAppRoleTableAccess;
import com.sinosoft.ops.cimp.entity.sys.sysapp.fieldAccess.SysAppRoleTableAccess;
import com.sinosoft.ops.cimp.entity.sys.systable.QSysTable;
import com.sinosoft.ops.cimp.mapper.sys.sysapp.access.SysAppTableAccessMapper;
import com.sinosoft.ops.cimp.repository.sys.sysapp.access.SysAppFieldAccessRepository;
import com.sinosoft.ops.cimp.repository.sys.sysapp.access.SysAppTableAccessRepository;
import com.sinosoft.ops.cimp.service.sys.sysapp.SysAppTableSetService;
import com.sinosoft.ops.cimp.service.sys.sysapp.acess.SysAppTableAccessService;
import com.sinosoft.ops.cimp.util.IdUtil;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.access.SysAppTableAccessAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.access.SysAppTableAccessModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.sysapp.access.SysAppTableAccessSearchModel;
import com.sinosoft.ops.cimp.vo.to.sys.sysapp.access.SysAppTableAccessModel;
import com.sinosoft.ops.cimp.vo.to.sys.sysapp.sysAppTableSet.SysAppTableSetModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SysAppTableAccessServiceImpl implements SysAppTableAccessService {

    @Autowired
    private SysAppTableAccessRepository tableAccessRepository;

    @Autowired
    private SysAppTableSetService tableSetService;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Autowired
    private SysAppFieldAccessRepository fieldAccessRepository;

    /**
     * 获取对表的访问权限列表
     */
    @Override
    public PaginationViewModel<SysAppTableAccessModel> listTableAccess(SysAppTableAccessSearchModel searchModel) {
        QSysAppRoleTableAccess qTableAccess = QSysAppRoleTableAccess.sysAppRoleTableAccess;
        QSysAppTableSet qTableSet = QSysAppTableSet.sysAppTableSet;
        QSysTable qSysTable = QSysTable.sysTable;

        int pageSize = searchModel.getPageSize() > 0 ? searchModel.getPageSize() : 1;
        int pageIndex = searchModel.getPageIndex() > 0 ? searchModel.getPageIndex() : 10;

        JPAQuery<SysAppRoleTableAccess> query = jpaQueryFactory.select(qTableAccess).from(qTableAccess)
                .innerJoin(qTableSet).on(qTableSet.id.eq(qTableAccess.sysAppTableSetId));

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qTableAccess.roleId.eq(searchModel.getRoleId()));
        if (StringUtils.isNotEmpty(searchModel.getName())) {
            query.innerJoin(qSysTable).on(qSysTable.id.eq(qTableSet.sysTableId));

            BooleanBuilder subBuilder = new BooleanBuilder();
            subBuilder.and(qTableSet.name.contains(searchModel.getName()));
            subBuilder.or(qTableSet.nameEn.contains(searchModel.getName()));
            subBuilder.or(new BooleanBuilder().and(qTableSet.name.isNull()).and(qSysTable.nameCn.contains(searchModel.getName())));
            subBuilder.or(new BooleanBuilder().and(qTableSet.nameEn.isNull()).and(qSysTable.nameEn.contains(searchModel.getName())));
            builder.and(subBuilder);
        }
        QueryResults<SysAppRoleTableAccess> results = query.where(builder)
                .orderBy(qTableSet.sort.asc())
                .offset((pageIndex - 1) * pageSize)
                .limit(pageSize)
                .fetchResults();

        return new PaginationViewModel
                .Builder<SysAppTableAccessModel>()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .totalCount(results.getTotal())
                .data(results.getResults().stream().map(tableAccess -> {
                    SysAppTableAccessModel tableAccessModel = SysAppTableAccessMapper.INSTANCE.tableAccessToTableAccessModel(tableAccess);

                    SysAppTableSetModel tableSetModel = tableSetService.getById(tableAccess.getSysAppTableSetId());
                    if (null != tableSetModel) {
                        tableAccessModel.setNameCn(tableSetModel.getName());
                        tableAccessModel.setNameEn(tableSetModel.getNameEn());
                    }
                    return tableAccessModel;
                }).collect(Collectors.toList()))
                .build();
    }

    /**
     * 添加对表的访问权限
     */
    @Transactional
    @Override
    public void addTableAccess(SysAppTableAccessAddModel addModel) {
        List<SysAppRoleTableAccess> tableAccesses = new ArrayList<>();

        for (String tableSetId : addModel.getSysAppTableSetIds()) {
            SysAppRoleTableAccess tableAccess = SysAppTableAccessMapper.INSTANCE.addModelToTableAccess(addModel);
            tableAccess.setId(IdUtil.uuid());
            tableAccess.setSysAppTableSetId(tableSetId);
            tableAccesses.add(tableAccess);
        }

        tableAccessRepository.saveAll(tableAccesses);
    }

    /**
     * 删除对表的访问权限
     */
    @Transactional
    @Override
    public void deleteTableAccess(List<String> ids) {
        if (ids == null || ids.size() == 0) {
            return;
        }
        for (String id : ids) {
            tableAccessRepository.deleteById(id);
        }

        //删除表下字段的访问权限信息
        fieldAccessRepository.deleteBySysAppRoleTableAccessIdIn(ids);
    }

    /**
     * 修改对表的访问权限
     */
    @Transactional
    @Override
    public boolean modifyTableAccess(SysAppTableAccessModifyModel modifyModel) {
        List<SysAppRoleTableAccess> tableAccesses = new ArrayList<>();
        for (String id : modifyModel.getIds()) {
            Optional<SysAppRoleTableAccess> tableAccessOptional = tableAccessRepository.findById(id);
            tableAccessOptional.ifPresent(tableAccess -> {
                SysAppTableAccessMapper.INSTANCE.modifyModelToTableAccess(modifyModel, tableAccess);
                tableAccesses.add(tableAccess);
            });
        }
        tableAccessRepository.saveAll(tableAccesses);

        return true;
    }

}
