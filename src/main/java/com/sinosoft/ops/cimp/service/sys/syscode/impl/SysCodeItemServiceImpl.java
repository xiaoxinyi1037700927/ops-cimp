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
import com.sinosoft.ops.cimp.service.sys.syscode.SysCodeSetService;
import com.sinosoft.ops.cimp.vo.from.sys.syscode.SysCodeItemAddModel;
import com.sinosoft.ops.cimp.vo.from.sys.syscode.SysCodeItemModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.syscode.SysCodeItemPageModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SysCodeItemServiceImpl implements SysCodeItemService {

    @Autowired
    private SysCodeSetRepository sysCodeSetDao;

    @Autowired
    private SysCodeItemRepository sysCodeItemDao;

    @Autowired
    private SysCodeSetService sysCodeSetService;
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

    @Override
    @Transactional(readOnly = true)
    public String getNameEx(int setId, String code) {
        SysCodeItem o = getByCode(setId, code);
        return (o == null) ? "" : ((o.getNameEx() == null) ? "" : o.getNameEx());
    }

    @Override
    @Transactional(readOnly = true)
    public String getNameEx(String setName, String code) {
        if (isOrganizationNameCodeSet(setName)) {
            return getOrganizationName(setName, code);
        }
        Integer setId = sysCodeSetService.getIdByName(setName);
        if (setId == null) {
            return "";
        } else {
            return getNameEx(setId, code);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String getDescription(int setId, String code) {
        SysCodeItem o = getByCode(setId, code);
        return (o == null) ? "" : ((o.getDescription() == null) ? "" : o.getDescription());
    }

    @Override
    @Transactional(readOnly = true)
    public String getDescription(String setName, String code) {
        if (isOrganizationNameCodeSet(setName)) {
            return getOrganizationName(setName, code);
        }
        Integer setId = sysCodeSetService.getIdByName(setName);
        if (setId == null) {
            return "";
        } else {
            return getDescription(setId, code);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public SysCodeItem getByCode(int setId, String code) {
        if (code != null) {
            Integer id = getCode2IdsBySetId(setId).get(code);
            if (id != null) {
                return getById(id);
            }
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public SysCodeItem getByCode(String setName, String code) {
        Integer setId = sysCodeSetService.getIdByName(setName);
        if (setId == null) {
            return null;
        } else {
            return getByCode(setId, code);
        }
    }


    @Override
    @Transactional(readOnly = true)
    public SysCodeItem getById(Serializable id) {
        SysCodeItem o = null;
//        ValueWrapper v = cache.get(id);
//        if (v != null) {
//            o = (SysCodeItem) v.get();
//            if (o != null) {
//                return o;
//            }
//        }
//        o = sysCodeItemDao.getById(id);
//        if (o != null) {
//            cache.put(id, o);
//        }
        return o;
    }

    @Override
    @Transactional(readOnly = true)
    public SysCodeItem getById(Serializable id, boolean lock ) {
//        return baseEntityDao.getById(id,lock);
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public Collection<Integer> getIdsBySetId(int setId) {
        Collection<Integer> l = null;
//        String key = new StringBuilder("SI_").append(setId).toString();
//        ValueWrapper o = cache.get(key);
//        if (o != null) {
//            l = (Collection<Integer>) o.get();
//            if (l != null) {
//                return l;
//            }
//        }
//        l = sysCodeItemDao.getIdsBySetId(setId);
//        cache.put(key, l);
        return l;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Integer> getIdsBySetName(String setName) {
        Integer setId = sysCodeSetService.getIdByName(setName);
        if (setId == null) {
            return Collections.emptyList();
        } else {
            return getIdsBySetId(setId);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public Map<String, Integer> getCode2IdsBySetId(int setId) {
        Map<String, Integer> m = null;
//        String key = new StringBuilder("SM_").append(setId).toString();
//        ValueWrapper o = cache.get(key);
//        if (o != null) {
//            m = (Map<String, Integer>) o.get();
//            if (m != null) {
//                return m;
//            }
//        }
//        m = new HashMap<String, Integer>();
//        for (Integer id : getIdsBySetId(setId)) {
//            SysCodeItem e = getById(id);
//            if (e != null) {
//                m.put(e.getCode(), e.getId());
//            }
//        }
//        cache.put(key, m);
        return m;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Integer> getCode2IdsBySetName(String setName) {
        Integer setId = sysCodeSetService.getIdByName(setName);
        if (setId == null) {
            return Collections.emptyMap();
        } else {
            return getCode2IdsBySetId(setId);
        }
    }

    /**
     * 判断是否机构名称（代码）
     * @param setName 代码集名称
     * @return
     * @author Ni
     * @since JDK 1.7
     */
    private boolean isOrganizationNameCodeSet(String setName) {
        if ("UN".equals(setName) || "UP".equals(setName) || "UN_UNLIMITED".equals(setName)
                || "UP_UNLIMITED".equals(setName)) {
            return true;
        }
        return false;
    }

    /**
     * 获取机构名称（代码）
     * @param setName 代码集名称
     * @param code 代码
     * @return 机构名称
     * @author Ni
     * @since JDK 1.7
     */
    private String getOrganizationName(String setName, String code) {
//        if ("UN".equals(setName)) {
//            if (code == null || "".equals(code)) {
//                return "";
//            } else {
//                return infoMaintainService.getOrganizationName(Arrays.asList(code.split(",")), OrganizationType.UNIT);
//            }
//        } else if ("UN_UNLIMITED".equals(setName)) {
//            if (code == null || "".equals(code)) {
//                return "";
//            } else {
//                return infoMaintainService.getOrganizationName(Arrays.asList(code.split(",")),
//                        OrganizationType.UNIT_UNLIMITED);
//            }
//        } else if ("UP".equals(setName)) {
//            if (code == null || "".equals(code)) {
//                return "";
//            } else {
//                return infoMaintainService.getOrganizationName(Arrays.asList(code.split(",")), OrganizationType.PARTY);
//            }
//        } else if ("UP_UNLIMITED".equals(setName)) {
//            if (code == null || "".equals(code)) {
//                return "";
//            } else {
//                return infoMaintainService.getOrganizationName(Arrays.asList(code.split(",")),
//                        OrganizationType.PARTY_UNLIMITED);
//            }
//        }
        return "";
    }
}
