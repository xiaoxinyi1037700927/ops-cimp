package com.sinosoft.ops.cimp.service.impl.code;

import com.google.common.collect.Lists;
import com.sinosoft.ops.cimp.entity.sys.code.SysCodeSet;
import com.sinosoft.ops.cimp.mapper.code.SysCodeSetModelMapper;
import com.sinosoft.ops.cimp.repository.code.SysCodeSetRepository;
import com.sinosoft.ops.cimp.service.code.SysCodeSetService;
import com.sinosoft.ops.cimp.vo.from.code.SysCodeSetAddModel;
import com.sinosoft.ops.cimp.vo.from.code.SysCodeSetModifyModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SysCodeSetServiceImpl implements SysCodeSetService {

    @Autowired
    private SysCodeSetRepository sysCodeSetDao;


    @Override
    @Transactional
    public boolean delSysCodeSetById(Integer id) {
        sysCodeSetDao.deleteById(id);
        return true;
    }

    @Override
    @Transactional
    public boolean saveSysCodeSet(SysCodeSetAddModel sysCodeSetAddModel) {
        if (sysCodeSetAddModel == null) {
            return false;
        }
        SysCodeSet sysCodeSet = SysCodeSetModelMapper.INSTANCE.sysCodeSetAddModelToSysCodeSet(sysCodeSetAddModel);
        sysCodeSet.setCreatedTime(new Date());
        sysCodeSet.setLastModifiedTime(new Date());
        sysCodeSetDao.save(sysCodeSet);
        return true;
    }

    @Override
    @Transactional
    public boolean upSysCodeSet(SysCodeSetModifyModel sysCodeSetModifyModel) {
        if (sysCodeSetModifyModel.getId() == null) {
            return false;
        }
        SysCodeSet sysCodeSet = SysCodeSetModelMapper.INSTANCE.sysCodeSetModifyModelToSysCodeSet(sysCodeSetModifyModel);
        sysCodeSet.setLastModifiedTime(new Date());
        sysCodeSetDao.save(sysCodeSet);
        return true;
    }

    @Override
    public List<SysCodeSetModifyModel> findAllSysCodeSets() {
        List<SysCodeSet> sysCodeSets = sysCodeSetDao.findAll();
        List<SysCodeSetModifyModel> sysCodeSetModifyModels = sysCodeSets.stream().map(SysCodeSetModelMapper.INSTANCE :: sysCodeSetToModifyModel).collect(Collectors.toList());
       sysCodeSetModifyModels.sort(Comparator.comparing(SysCodeSetModifyModel::getId));
        return sysCodeSetModifyModels;
    }

    @Override
    public SysCodeSetModifyModel getSysCodeById(Integer id) {
        Optional<SysCodeSet> sysCodeSet = sysCodeSetDao.findById(id);
        if (!sysCodeSet.isPresent()) {
            return null;
        }
        SysCodeSet sysCodeSet1 = sysCodeSet.get();
        return SysCodeSetModelMapper.INSTANCE.sysCodeSetToModifyModel(sysCodeSet1);
    }
}
