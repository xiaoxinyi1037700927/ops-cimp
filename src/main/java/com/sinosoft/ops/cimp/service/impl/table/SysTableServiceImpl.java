package com.sinosoft.ops.cimp.service.impl.table;

import com.google.common.collect.Lists;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.sys.table.SysTable;
import com.sinosoft.ops.cimp.mapper.table.SysTableModelMapper;
import com.sinosoft.ops.cimp.repository.table.SysTableRepository;
import com.sinosoft.ops.cimp.service.table.SysTableService;
import com.sinosoft.ops.cimp.service.table.SysTableTypeService;
import com.sinosoft.ops.cimp.vo.from.table.SysTableAddModel;
import com.sinosoft.ops.cimp.vo.to.table.SysTableModel;
import com.sinosoft.ops.cimp.vo.from.table.SysTableModifyModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysTableServiceImpl implements SysTableService {

    @Autowired
    private SysTableRepository sysTableDao;

    @Autowired
    private SysTableTypeService sysTableTypeService;

    @Override
    @Transactional
    public boolean addSysTable(SysTableAddModel sysTableAddModel) {
        if (sysTableAddModel == null) {
            return false;
        }
        SysTable sysTable = SysTableModelMapper.INSTANCE.addModelToSysTable(sysTableAddModel);
        sysTableDao.save(sysTable);
        return true;
    }

    @Override
    @Transactional
    public boolean delSysTable(String id) {
        sysTableDao.deleteById(id);
        return true;
    }

    @Override
    @Transactional
    public boolean upSysTable(SysTableModifyModel sysTableModifyModel) {
        if (sysTableModifyModel.getId() == null) {
            return false;
        }
        SysTable sysTable = SysTableModelMapper.INSTANCE.modifyModelToSysTable(sysTableModifyModel);
        sysTableDao.save(sysTable);
        return true;
    }

    @Override
    public List<SysTableModel> findSysTableModels() {
        List<SysTable> sysTables = sysTableDao.findAll();
        List<SysTableModel> sysTableModels = Lists.newArrayList();
        sysTables.forEach(e ->{
            SysTableModel sysTableModel = new SysTableModel();
            sysTableModel = SysTableModelMapper.INSTANCE.sysTableToModel(e);
            sysTableModel.setSysTableTypeModifyModels(sysTableTypeService.findSysTableTypeByCode(e.getSysTableTypeId()));
        });
        return sysTableModels;
    }

    @Override
    public List<SysTableModifyModel> findBySysTableTypeId(String sysTableTypeId) {
        List<SysTable> sysTables = sysTableDao.findBySysTableTypeId(sysTableTypeId);
        return sysTables.stream().map(e ->
                SysTableModelMapper.INSTANCE.toModifyModel(e)).collect(Collectors.toList());
    }

    @Override
    public PaginationViewModel<SysTable> getSysTableByPage(String sysTableTypeId, String nameCn) {




        return null;
    }


}
