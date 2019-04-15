package com.sinosoft.ops.cimp.service.sys.app.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.sys.app.QSysApp;
import com.sinosoft.ops.cimp.entity.sys.app.SysApp;
import com.sinosoft.ops.cimp.mapper.sys.app.SysAppMapper;
import com.sinosoft.ops.cimp.repository.sys.app.SysAppRepository;
import com.sinosoft.ops.cimp.service.sys.app.SysAppService;
import com.sinosoft.ops.cimp.service.sys.app.SysAppTableGroupService;
import com.sinosoft.ops.cimp.vo.from.sys.app.sysApp.SysAppAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.app.sysApp.SysAppModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.app.sysApp.SysAppSearchModel;
import com.sinosoft.ops.cimp.vo.to.sys.app.sysApp.SysAppModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 系统应用服务实现类
 */
@Service
public class SysAppServiceImpl implements SysAppService {

    @Autowired
    private SysAppRepository sysAppRepository;

    @Autowired
    private SysAppTableGroupService tableGroupService;

    /**
     * 获取系统应用列表
     */
    @Override
    public PaginationViewModel<SysAppModel> listSysApp(SysAppSearchModel searchModel) {
        int pageSize = searchModel.getPageSize() > 0 ? searchModel.getPageSize() : 1;
        int pageIndex = searchModel.getPageIndex() > 0 ? searchModel.getPageIndex() : 10;
        PageRequest pageRequest = PageRequest.of(pageIndex - 1, pageSize);

        QSysApp qSysApp = QSysApp.sysApp;
        BooleanBuilder builder = new BooleanBuilder();
        if (null != searchModel.getCode()) {
            builder = builder.and(qSysApp.code.eq(searchModel.getCode()));
        }
        if (StringUtils.isNotEmpty(searchModel.getName())) {
            builder = builder.and(qSysApp.name.contains(searchModel.getName()));
        }

        Page<SysApp> page = sysAppRepository.findAll(builder, pageRequest);

        return new PaginationViewModel
                .Builder<SysAppModel>()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .totalCount(page.getTotalElements())
                .data(page.getContent().stream().map(SysAppMapper.INSTANCE::sysAppToSysAppModel).collect(Collectors.toList()))
                .build();
    }

    /**
     * 添加系统应用
     */
    @Transactional
    @Override
    public void addSysApp(SysAppAddModel addModel) {
        sysAppRepository.save(SysAppMapper.INSTANCE.addModelToSysApp(addModel));
    }

    /**
     * 删除系统应用
     */
    @Transactional
    @Override
    public void deleteSysApp(List<String> ids) {
        for (String id : ids) {
            sysAppRepository.deleteById(id);
        }

        //删除系统应用下所有表分组
        tableGroupService.deleteBySysAppIds(ids);
    }

    /**
     * 修改系统应用
     */
    @Transactional
    @Override
    public boolean modifySysApp(SysAppModifyModel modifyModel) {
        Optional<SysApp> sysAppOptional = sysAppRepository.findById(modifyModel.getId());
        if (!sysAppOptional.isPresent()) {
            return false;
        }

        SysApp sysApp = sysAppOptional.get();
        SysAppMapper.INSTANCE.modifyModelToSysApp(modifyModel, sysApp);
        sysAppRepository.save(sysApp);

        return true;
    }
}
