package com.sinosoft.ops.cimp.service.impl.code;

import com.querydsl.core.BooleanBuilder;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.sys.code.QSysCodeItem;
import com.sinosoft.ops.cimp.entity.sys.code.SysCodeItem;
import com.sinosoft.ops.cimp.mapper.code.SysCodeItemModelMapper;
import com.sinosoft.ops.cimp.repository.code.SysCodeItemRepository;
import com.sinosoft.ops.cimp.service.code.SysCodeItemService;
import com.sinosoft.ops.cimp.vo.from.code.SysCodeItemAddModel;
import com.sinosoft.ops.cimp.vo.from.code.SysCodeItemModifyModel;
import com.sinosoft.ops.cimp.vo.from.code.SysCodeItemPageModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SysCodeItemServiceImpl implements SysCodeItemService {

    @Autowired
    private SysCodeItemRepository sysCodeItemDao;

    @Override
    @Transactional
    public boolean delSysCodeItemById(Integer id) {
        sysCodeItemDao.deleteById(id);
        return true;
    }

    @Override
    @Transactional
    public boolean saveSysCodeItem(SysCodeItemAddModel sysCodeItemAddModel) {
        if (sysCodeItemAddModel == null) {
            return false;
        }
        SysCodeItem sysCodeItem = SysCodeItemModelMapper.INSTANCE.addModelToSysCodeItem(sysCodeItemAddModel);
        sysCodeItem.setCreatedTime(new Date());
        sysCodeItem.setLastModifiedTime(new Date());
        sysCodeItemDao.save(sysCodeItem);
        return true;
    }

    @Override
    @Transactional
    public boolean upSysCodeItem(SysCodeItemModifyModel sysCodeItemModifyModel) {
        if (sysCodeItemModifyModel.getId() == null) {
            return false;
        }
        SysCodeItem sysCodeItem = SysCodeItemModelMapper.INSTANCE.modifyModelToSysCodeItem(sysCodeItemModifyModel);
        sysCodeItem.setLastModifiedTime(new Date());
        sysCodeItemDao.save(sysCodeItem);
        return true;
    }

    @Override
    public PaginationViewModel<SysCodeItemModifyModel> getSysCodeItemBySearchModel(SysCodeItemPageModel searchModel) {
        int pageIndex = searchModel.getPageIndex();
        int pageSize = searchModel.getPageSize();
        if (pageIndex <= 0) pageIndex = 1;
        if (pageSize <= 0) pageSize = 10;

        QSysCodeItem qSysCodeItem = QSysCodeItem.sysCodeItem;

        PageRequest pageRequest = PageRequest.of(pageIndex - 1, pageSize, new Sort(Sort.Direction.ASC, qSysCodeItem.ordinal.getMetadata().getName()));
        BooleanBuilder builder = new BooleanBuilder();

        builder = builder.and(qSysCodeItem.codeSetId.eq(searchModel.getId()));


        if (StringUtils.isNotEmpty(searchModel.getNameCn())) {
            builder = builder.and(qSysCodeItem.name.contains(searchModel.getNameCn()));
        }

        Page<SysCodeItem> all = sysCodeItemDao.findAll(builder, pageRequest);
        List<SysCodeItemModifyModel> collect = all.getContent()
                .stream()
                .map(SysCodeItemModelMapper.INSTANCE::sysCodeItemToModifyModel)
                .collect(Collectors.toList());

        PaginationViewModel<SysCodeItemModifyModel> page = new PaginationViewModel<SysCodeItemModifyModel>();
        page.setPageIndex(searchModel.getPageIndex());
        page.setPageSize(searchModel.getPageSize());
        page.setTotalCount(all.getTotalElements());
        page.setData(collect);

        return page;
    }

    @Override
    public SysCodeItemModifyModel getSysCodeItemById(Integer id) {
        Optional<SysCodeItem> sysCodeItems = sysCodeItemDao.findById(id);
        if (!sysCodeItems.isPresent()) {
            return null;
        }
        SysCodeItem sysCodeItem = sysCodeItems.get();
        return SysCodeItemModelMapper.INSTANCE.sysCodeItemToModifyModel(sysCodeItem);
    }
}
