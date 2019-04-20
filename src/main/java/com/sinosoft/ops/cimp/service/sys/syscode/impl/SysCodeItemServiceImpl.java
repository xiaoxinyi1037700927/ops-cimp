package com.sinosoft.ops.cimp.service.sys.syscode.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.sys.syscode.QSysCodeItem;
import com.sinosoft.ops.cimp.entity.sys.syscode.QSysCodeSet;
import com.sinosoft.ops.cimp.entity.sys.syscode.SysCodeItem;
import com.sinosoft.ops.cimp.entity.sys.syscode.SysCodeSet;
import com.sinosoft.ops.cimp.mapper.sys.syscode.SysCodeItemModelMapper;
import com.sinosoft.ops.cimp.repository.sys.syscode.SysCodeItemRepository;
import com.sinosoft.ops.cimp.repository.sys.syscode.SysCodeSetRepository;
import com.sinosoft.ops.cimp.service.sys.syscode.SysCodeItemService;
import com.sinosoft.ops.cimp.vo.from.sys.syscode.SysCodeItemAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.syscode.SysCodeItemModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.syscode.SysCodeItemPageModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SysCodeItemServiceImpl implements SysCodeItemService {

    @Autowired
    private SysCodeSetRepository sysCodeSetDao;

    @Autowired
    private SysCodeItemRepository sysCodeItemDao;

    @PersistenceContext
    private final EntityManager entityManager;
    private JPAQueryFactory queryFactory;

    @Autowired
    public SysCodeItemServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @PostConstruct
    public void init() {
        queryFactory = new JPAQueryFactory(entityManager);
    }


    @Override
    @Transactional
    public boolean delSysCodeItemById(Integer id) {
        Optional<SysCodeItem> sysCodeItem = sysCodeItemDao.findById(id);
        QSysCodeSet qSysCodeSet = QSysCodeSet.sysCodeSet;
        Integer codeSetId = sysCodeItem.get().getCodeSetId();
        Optional<SysCodeSet> sysCodeSet = sysCodeSetDao.findById(codeSetId);
        Integer version = sysCodeSet.get().getVersion();
        queryFactory.update(qSysCodeSet)
                .set(qSysCodeSet.version, version + 1)
                .where(qSysCodeSet.id.eq(codeSetId))
                .execute();

        sysCodeItemDao.deleteById(id);
        return true;
    }

    @Override
    @Transactional
    public boolean saveSysCodeItem(SysCodeItemAddModel sysCodeItemAddModel) {
        if (sysCodeItemAddModel == null) {
            return false;
        }

        Integer codeSetId = sysCodeItemAddModel.getCodeSetId();
        Optional<SysCodeSet> sysCodeSet = sysCodeSetDao.findById(codeSetId);
        Integer version = sysCodeSet.get().getVersion();
        QSysCodeSet qSysCodeSet = QSysCodeSet.sysCodeSet;
        queryFactory.update(qSysCodeSet)
                .set(qSysCodeSet.version, version + 1)
                .where(qSysCodeSet.id.eq(codeSetId))
                .execute();

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

        Integer codeSetId = sysCodeItemModifyModel.getCodeSetId();
        Optional<SysCodeSet> sysCodeSet = sysCodeSetDao.findById(codeSetId);
        Integer version = sysCodeSet.get().getVersion();
        QSysCodeSet qSysCodeSet = QSysCodeSet.sysCodeSet;
        queryFactory.update(qSysCodeSet)
                .set(qSysCodeSet.version, version + 1)
                .where(qSysCodeSet.id.eq(codeSetId))
                .execute();

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

    @Override
    public List<SysCodeItem> findByCodeSetName(String name) {
        SysCodeSet sysCodeSet = sysCodeSetDao.getByName(name);

        if (null != sysCodeSet) {
            return sysCodeItemDao.findByCodeSetId(sysCodeSet.getId());
        }
        return null;
    }
}
