package com.sinosoft.ops.cimp.service.impl.table;

import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.sys.table.QSysTableType;
import com.sinosoft.ops.cimp.entity.sys.table.SysTableType;
import com.sinosoft.ops.cimp.mapper.table.SysTableTypeModelMapper;
import com.sinosoft.ops.cimp.repository.table.SysTableTypeRepository;
import com.sinosoft.ops.cimp.service.table.SysTableService;
import com.sinosoft.ops.cimp.service.table.SysTableTypeService;
import com.sinosoft.ops.cimp.vo.from.table.SysTableTypeAddModel;
import com.sinosoft.ops.cimp.vo.from.table.SysTableTypeModifyModel;
import com.sinosoft.ops.cimp.vo.from.table.SysTableTypeSearchModel;
import com.sinosoft.ops.cimp.vo.to.table.SysTableTypeModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysTableTypeServiceImpl implements SysTableTypeService {

    @Autowired
    private SysTableTypeRepository sysTableTypeDao;

    @Autowired
    private SysTableService sysTableService;

    @Override
    @Transactional
    public boolean addSysTableType(SysTableTypeAddModel sysTableTypeAddModel) {
        if (sysTableTypeAddModel == null) {
            return false;
        }
        SysTableType sysTableType = SysTableTypeModelMapper.INSTANCE.addModelfyToSysTableType(sysTableTypeAddModel);
        sysTableTypeDao.save(sysTableType);
        return true;
    }

    @Override
    @Transactional
    public boolean upSysTableType(SysTableTypeModifyModel sysTableTypeModifyModel) {
        if (sysTableTypeModifyModel.getId() == null) {
            return false;
        }
        SysTableType sysTableType = SysTableTypeModelMapper.INSTANCE.modifyModelToSysTableType(sysTableTypeModifyModel);
        sysTableTypeDao.save(sysTableType);
        return true;
    }

    @Override
    @Transactional
    public boolean delSysTableType(String id) {
        sysTableTypeDao.deleteById(id);
        return true;
    }

    @Override
    public List<SysTableTypeModifyModel> findSysTableTypeByCode(String code) {
        List<SysTableType> sysTableTypes = null;//sysTableTypeDao.findByCode(code);
        List<SysTableTypeModifyModel> sysTableTypeModifyModels = Lists.newArrayList();
        sysTableTypes.forEach(e -> {
            SysTableTypeModifyModel sysTableTypeModifyModel = SysTableTypeModelMapper.INSTANCE.sysTableTypeToModel(e);
            sysTableTypeModifyModels.add(sysTableTypeModifyModel);
        });
        return sysTableTypeModifyModels;
    }

    @Override
    public PaginationViewModel<SysTableTypeModifyModel> findSysTableTypeByPageOrName(SysTableTypeSearchModel searchModel) {
        int pageIndex = searchModel.getPageIndex();
        int pageSize = searchModel.getPageSize();
        if (pageIndex <= 0) pageIndex = 1;
        if (pageSize <= 0) pageSize = 10;

        QSysTableType qSysTableType = QSysTableType.sysTableType;
        PageRequest pageRequest = PageRequest.of(pageIndex - 1, pageSize, new Sort(Sort.Direction.ASC, qSysTableType.nameCn.getMetadata().getName()));
        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.isNotEmpty(searchModel.getCode())) {
            builder = builder.and(qSysTableType.id.eq(searchModel.getCode()));
        }

        if (StringUtils.isNotEmpty(searchModel.getNameCn())) {
            builder = builder.and(qSysTableType.nameCn.contains(searchModel.getNameCn()));
        }
        Page<SysTableType> all = sysTableTypeDao.findAll(builder, pageRequest);
        List<SysTableTypeModifyModel> collect = all.getContent().stream().map(x -> SysTableTypeModelMapper.INSTANCE.sysTableTypeToModel(x)).collect(Collectors.toList());

        PaginationViewModel<SysTableTypeModifyModel> page = new PaginationViewModel<SysTableTypeModifyModel>();
        page.setPageIndex(searchModel.getPageIndex());
        page.setPageSize(searchModel.getPageSize());
        page.setTotalCount(all.getTotalElements());
        page.setData(collect);

        return page;
    }

    @Override
    public List<SysTableTypeModel> getAllSysTableType() {
        List<SysTableType> sysTableTypes = sysTableTypeDao.findAll();
        List<SysTableTypeModel> sysTableTypeModels = Lists.newArrayList();
        sysTableTypes.forEach(e ->{
            SysTableTypeModel sysTableTypeModel = SysTableTypeModelMapper.INSTANCE.toSysTableTypeModel(e);
            sysTableTypeModel.setSysTableModifyModels(sysTableService.findBySysTableTypeId(e.getId()));
        });

        return sysTableTypeModels;
    }


}
