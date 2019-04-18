package com.sinosoft.ops.cimp.service.sys.systable.impl;

import com.google.common.collect.Lists;
import com.sinosoft.ops.cimp.entity.sys.systable.SysTable;
import com.sinosoft.ops.cimp.entity.sys.systable.SysTableType;
import com.sinosoft.ops.cimp.mapper.sys.systable.SysTableModelMapper;
import com.sinosoft.ops.cimp.mapper.sys.systable.SysTableTypeModelMapper;
import com.sinosoft.ops.cimp.repository.sys.systable.SysTableRepository;
import com.sinosoft.ops.cimp.repository.sys.systable.SysTableTypeRepository;
import com.sinosoft.ops.cimp.service.sys.systable.SysTableTypeService;
import com.sinosoft.ops.cimp.vo.from.sys.systable.SysTableAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.systable.SysTableModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.systable.SysTableTypeAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.systable.SysTableTypeModifyModel;
import com.sinosoft.ops.cimp.vo.to.sys.systable.SysTableTypeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysTableTypeServiceImpl implements SysTableTypeService {

    @Autowired
    private SysTableRepository sysTableDao;

    @Autowired
    private SysTableTypeRepository sysTableTypeDao;

    @Override
    @Transactional
    public boolean addSysTableType(SysTableTypeAddModel sysTableTypeAddModel) {
        if (sysTableTypeAddModel == null) {
            return false;
        }
        SysTableType sysTableType = SysTableTypeModelMapper.INSTANCE.addModelfyToSysTableType(sysTableTypeAddModel);
        sysTableType.setCreateTime(new Date());
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
        sysTableType.setModifyTime(new Date());
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
    public List<SysTableTypeModel> getAllSysTableType() {
        List<SysTableType> sysTableTypes = sysTableTypeDao.findAll();
        List<SysTable> sysTables = sysTableDao.findAll();
        List<SysTableModifyModel> sysTableModifyModels = sysTables.stream()
                .map(SysTableModelMapper.INSTANCE::toModifyModel)
                .collect(Collectors.toList());
        List<SysTableTypeModel> sysTableTypeModels = Lists.newArrayList();


        sysTableTypes.forEach(e -> {
            SysTableTypeModel sysTableTypeModel = SysTableTypeModelMapper.INSTANCE.toSysTableTypeModel(e);
            List<SysTableModifyModel> adSysTableModifyModels = sysTableModifyModels
                    .stream()
                    .filter(s ->s.getSysTableTypeId().equals(e.getId()))
                    .collect(Collectors.toList());

            adSysTableModifyModels.sort(Comparator.comparing(SysTableAddModel::getSort));

            sysTableTypeModel.setSysTableModifyModels(adSysTableModifyModels);

            sysTableTypeModels.add(sysTableTypeModel);
        });

        sysTableTypeModels.sort(Comparator.comparing(SysTableTypeModel::getSort));
        return sysTableTypeModels;
    }


}
