package com.sinosoft.ops.cimp.service.impl.code;

import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.sys.code.QSysCodeSet;
import com.sinosoft.ops.cimp.entity.sys.code.SysCodeItem;
import com.sinosoft.ops.cimp.entity.sys.code.SysCodeSet;
import com.sinosoft.ops.cimp.entity.sys.table.SysTableField;
import com.sinosoft.ops.cimp.mapper.code.SysCodeItemModelMapper;
import com.sinosoft.ops.cimp.mapper.code.SysCodeSetModelMapper;
import com.sinosoft.ops.cimp.mapper.table.SysTableFieldModelMapper;
import com.sinosoft.ops.cimp.repository.code.SysCodeItemRepository;
import com.sinosoft.ops.cimp.repository.code.SysCodeSetRepository;
import com.sinosoft.ops.cimp.service.code.SysCodeSetService;
import com.sinosoft.ops.cimp.vo.from.code.SysCodeSetAddModel;
import com.sinosoft.ops.cimp.vo.from.code.SysCodeSetModifyModel;
import com.sinosoft.ops.cimp.vo.from.code.SysCodeSetSearchListModel;
import com.sinosoft.ops.cimp.vo.from.code.SysCodeSetSearchModel;
import com.sinosoft.ops.cimp.vo.from.table.SysTableFieldModifyModel;
import com.sinosoft.ops.cimp.vo.to.sys.code.SysCodeItemModel;
import com.sinosoft.ops.cimp.vo.to.sys.code.SysCodeSetDisplayModel;
import com.sinosoft.ops.cimp.vo.to.sys.code.SysCodeSetModel;
import com.sinosoft.ops.cimp.vo.to.sys.code.SysCodeSetObtainModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    @Autowired
    private SysCodeItemRepository sysCodeItemDao;

    @Override
    @Transactional
    public boolean delSysCodeSetById(Integer id) {
//        sysCodeItemDao.deleteByCodeSetId(id);
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
        List<SysCodeSetModifyModel> sysCodeSetModifyModels = sysCodeSets.stream().map(SysCodeSetModelMapper.INSTANCE::sysCodeSetToModifyModel).collect(Collectors.toList());
        sysCodeSetModifyModels.sort(Comparator.comparing(SysCodeSetModifyModel::getId));
        return sysCodeSetModifyModels;
    }

    @Override
    public PaginationViewModel<SysCodeSetDisplayModel> getPageSysCodeSet(SysCodeSetSearchModel searchModel) {
        int pageIndex = searchModel.getPageIndex();
        int pageSize = searchModel.getPageSize();
        if (pageIndex <= 0) pageIndex = 1;
        if (pageSize <= 0) pageSize = 10;
        Page<SysCodeSet> all;
        QSysCodeSet qSysCodeSet = QSysCodeSet.sysCodeSet;
        PageRequest pageRequest = PageRequest.of(pageIndex - 1, pageSize, new Sort(Sort.Direction.ASC, qSysCodeSet.ordinal.getMetadata().getName()));
        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.isNotEmpty(searchModel.getName())) {
            builder = builder.and(qSysCodeSet.name.contains(searchModel.getName()));
        }

         all = sysCodeSetDao.findAll(builder, pageRequest);
        if (all.getTotalPages() == 0) {
            builder = new BooleanBuilder();
            builder = builder.and(qSysCodeSet.nameCn.contains(searchModel.getName()));
            all = sysCodeSetDao.findAll(builder, pageRequest);
        }

        List<SysCodeSetDisplayModel> collect = all
                .getContent()
                .stream()
                .map(SysCodeSetModelMapper.INSTANCE::codeSetToDisplay)
                .collect(Collectors.toList());

        PaginationViewModel<SysCodeSetDisplayModel> page = new PaginationViewModel<SysCodeSetDisplayModel>();
        page.setPageIndex(searchModel.getPageIndex());
        page.setPageSize(searchModel.getPageSize());
        page.setTotalCount(all.getTotalElements());
        page.setData(collect);
        return page;

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

    @Override
    public List<SysCodeSetObtainModel> getSysCodeSetAndSysCodeItem(SysCodeSetSearchListModel searchListModel) {
        List<SysCodeSet> sysCodeSets = Lists.newArrayList();
        if (searchListModel.getIds().size() == 0) {
            sysCodeSets = sysCodeSetDao.findAll();
        }
        for (Integer id : searchListModel.getIds()) {
            Optional<SysCodeSet> sysCodeSet = sysCodeSetDao.findById(id);
            if (sysCodeSet.isPresent()) {
                sysCodeSets.add(sysCodeSet.get());
            }
        }

        List<SysCodeSetObtainModel> sysCodeSetObtainModels = sysCodeSets.stream().map(SysCodeSetModelMapper.INSTANCE::codeSetToObtail).collect(Collectors.toList());
        for (SysCodeSetObtainModel setObtainModel : sysCodeSetObtainModels) {
            List<SysCodeItem> sysCodeItems = sysCodeItemDao.findByCodeSetId(setObtainModel.getId());
            List<SysCodeItemModel> sysCodeItemModels = sysCodeItems.stream().map(SysCodeItemModelMapper.INSTANCE::codeItemToModel).collect(Collectors.toList());
            setObtainModel.setSysCodeItemModels(sysCodeItemModels);
        }

        return sysCodeSetObtainModels;
    }

    @Override
    public List<SysCodeSetModel> getSysCodeSet() {
        List<SysCodeSet> sysCodeSets = sysCodeSetDao.findAll();
        List<SysCodeSetModel> sysCodeSetModifyModels = sysCodeSets.stream().map(SysCodeSetModelMapper.INSTANCE::codeSetToModel).collect(Collectors.toList());
        sysCodeSetModifyModels.sort(Comparator.comparing(SysCodeSetModel::getOrdinal));
        return sysCodeSetModifyModels;
    }
}
