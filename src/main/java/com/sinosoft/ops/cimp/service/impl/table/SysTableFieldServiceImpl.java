package com.sinosoft.ops.cimp.service.impl.table;

import com.querydsl.core.BooleanBuilder;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.sys.table.QSysTableField;
import com.sinosoft.ops.cimp.entity.sys.table.SysTableField;
import com.sinosoft.ops.cimp.mapper.table.SysTableFieldModelMapper;
import com.sinosoft.ops.cimp.repository.table.SysTableFieldRepository;
import com.sinosoft.ops.cimp.service.table.SysTableFieldService;
import com.sinosoft.ops.cimp.vo.from.table.SysTableFieldAddModel;
import com.sinosoft.ops.cimp.vo.from.table.SysTableFieldModifyModel;
import com.sinosoft.ops.cimp.vo.from.table.SysTableFieldSearchModel;
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

@Service
public class SysTableFieldServiceImpl implements SysTableFieldService {

    @Autowired
    private SysTableFieldRepository sysTableFieldDao;

    @Override
    @Transactional
    public boolean delSysTableField(String id) {
        sysTableFieldDao.deleteById(id);
        return true;
    }

    @Override
    @Transactional
    public boolean upSysTableField(SysTableFieldModifyModel sysTableFieldModifyModel) {
        if (sysTableFieldModifyModel.getId() == null) {
            return false;
        }
        SysTableField sysTableField = SysTableFieldModelMapper.INSTANCE.modifyModelToSysTableField(sysTableFieldModifyModel);
        sysTableFieldDao.save(sysTableField);
        return true;
    }

    @Override
    @Transactional
    public boolean addSysTableField(SysTableFieldAddModel sysTableFieldAddModel) {
        if (sysTableFieldAddModel == null) {
            return false;
        }
        SysTableField sysTableField = SysTableFieldModelMapper.INSTANCE.addModelToSysTableField(sysTableFieldAddModel);
        sysTableFieldDao.save(sysTableField);
        return true;
    }

    @Override
    public PaginationViewModel<SysTableFieldModifyModel> findBySysTableFieldByPageOrName(SysTableFieldSearchModel searchModel) {
        int pageIndex = searchModel.getPageIndex();
        int pageSize = searchModel.getPageSize();
        if (pageIndex <= 0) pageIndex = 1;
        if (pageSize <= 0) pageSize = 10;

        QSysTableField qSysTableField = QSysTableField.sysTableField;
        PageRequest pageRequest = PageRequest.of(pageIndex - 1, pageSize, new Sort(Sort.Direction.ASC, qSysTableField.nameCn.getMetadata().getName()));
        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.isNotEmpty(searchModel.getSysTableId())) {
            builder = builder.and(qSysTableField.id.eq(searchModel.getSysTableId()));
        }

        if (StringUtils.isNotEmpty(searchModel.getNameCn())) {
            builder = builder.and(qSysTableField.nameCn.contains(searchModel.getNameCn()));
        }
        Page<SysTableField> all = sysTableFieldDao.findAll(builder, pageRequest);
        List<SysTableFieldModifyModel> collect = all.getContent().stream().map(x ->
                SysTableFieldModelMapper.INSTANCE.sysTableFieldToModel(x)).collect(Collectors.toList());

        PaginationViewModel<SysTableFieldModifyModel> page = new PaginationViewModel<SysTableFieldModifyModel>();
        page.setPageIndex(searchModel.getPageIndex());
        page.setPageSize(searchModel.getPageSize());
        page.setTotalCount(all.getTotalElements());
        page.setData(collect);
        return page;
    }

    @Override
    public SysTableFieldModifyModel findById(String id) {
        Optional<SysTableField> sysTableField = sysTableFieldDao.findById(id);
        if (!sysTableField.isPresent()) {
            return null;
        }
        SysTableField sysTableField1 = sysTableField.get();
        SysTableFieldModifyModel sysTableFieldModifyModel = SysTableFieldModelMapper.INSTANCE.sysTableFieldToModel(sysTableField1);
        return sysTableFieldModifyModel;
    }


}
