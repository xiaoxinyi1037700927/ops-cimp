/**
 * @Project:      IIMP
 * @Title:           SysInfoItemServiceImpl.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.service.infostruct.impl;


import com.sinosoft.ops.cimp.common.service.BaseEntityServiceImpl;
import com.sinosoft.ops.cimp.entity.infostruct.BuiltInColumn;
import com.sinosoft.ops.cimp.entity.infostruct.InputType;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoItem;
import com.sinosoft.ops.cimp.exception.CloneException;
import com.sinosoft.ops.cimp.repository.infostruct.SysInfoItemDao;
import com.sinosoft.ops.cimp.service.infostruct.SysInfoItemService;
import com.sinosoft.ops.cimp.service.infostruct.SysInfoSetService;
import com.sinosoft.ops.cimp.service.system.SysCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @ClassName:  SysInfoItemServiceImpl
 * @Description: TODO请描述一下这个类
 * @Author:        wft
 * @Date:            2017年8月18日 下午4:38:33
 * @Version        1.0.0
 */
@Service("sysInfoItemService")
public class SysInfoItemServiceImpl extends BaseEntityServiceImpl<SysInfoItem> implements SysInfoItemService {
    private static final Logger logger = LoggerFactory.getLogger(SysInfoItemServiceImpl.class);
    /*** 缓存名称 */
    public static final String CACHE_NAME = "infoItemCache";
    private static final AtomicBoolean preInitialized = new AtomicBoolean(false);
    
    @Autowired
    private SysInfoItemDao sysInfoItemDao;
    @Autowired
    private SysInfoSetService sysInfoSetService;
    
    private final SysCacheService sysCacheService;
    private final Cache cache;
    
    @Autowired
    public SysInfoItemServiceImpl(SysCacheService sysCacheService) {
        this.sysCacheService = sysCacheService;
        this.cache = sysCacheService.getByName(CACHE_NAME);
    }
    
    @Override
    @Transactional
    public Serializable create(SysInfoItem entity){
        Serializable id = sysInfoItemDao.save(entity);
        sysCacheService.reset(CACHE_NAME);
        return id;
    }

    @Override
    @Transactional(readOnly=true)
    public void preInitialize() {
        if(preInitialized.compareAndSet(false,true)) {
            cache.clear();
            Collection<SysInfoItem> l = sysInfoItemDao.getAll();
            for(SysInfoItem o:l) {
                cache.put(o.getId(), o);
            }
            logger.info("初始化缓存"+cache.getName()+"完成!(共"+l.size()+"项)");
        }
    }

    @Override
    @Transactional
    public void update(SysInfoItem entity){
        sysInfoItemDao.update(entity);
        sysCacheService.reset(CACHE_NAME);
    }
    
    @Override
    @Transactional
    public void delete(SysInfoItem entity){
        sysInfoItemDao.delete(entity);
        sysCacheService.reset(CACHE_NAME);
    }
    
    @Override
    @Transactional
    public void deleteById(Serializable id){
        sysInfoItemDao.deleteById(id);
        sysCacheService.reset(CACHE_NAME);
    }
    
    @Override
    @Transactional
    public SysInfoItem merge(SysInfoItem entity){
        sysCacheService.reset(CACHE_NAME);
        return sysInfoItemDao.merge(entity);
    }
    
    @Override
    @Transactional(readOnly=true)
    public SysInfoItem getById(Serializable id) {
        SysInfoItem o = null;
        ValueWrapper v = cache.get(id);
        if(v!=null) {
            o = (SysInfoItem)v.get();
            if(o!=null) {
                return o;
            }
        }
        o = sysInfoItemDao.getById(id);
        if(o!=null) {
            cache.put(id, o);
        }
        return o;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly=true)
    public Collection<Integer> getIdsBySetId(int setId) {
        Collection<Integer> l = null;
        String key = new StringBuilder("SI_").append(setId).toString();
        ValueWrapper o = cache.get(key);
        if(o!=null) {
            l = (Collection<Integer>) o.get();
            if(l!=null) {
                return l;
            }
        }
        l = sysInfoItemDao.getIdsBySetId(setId);
        cache.put(key, l);
        return l;
    }
    @Override
    @Transactional(readOnly=true)
    public Collection<SysInfoItem> getBySetId(int setId) {
        return getBySetId(setId,false);
    }
    @Override
    @Transactional(readOnly=true)
    public Collection<SysInfoItem> getBySetId(int setId, boolean excludeUnnamed) {
        Collection<SysInfoItem> l = new LinkedList<SysInfoItem>();
        for(Integer id:getIdsBySetId(setId)) {
            SysInfoItem o = getById(id);
            if(o!=null) {
                if(excludeUnnamed){
                    if(o.getNameCn()==null||"".equals(o.getNameCn())){
                        continue;
                    }
                }
                l.add(o);
            }
        }
        return l;
    }
    
    @Override
    @Transactional(readOnly=true)
    public Collection<SysInfoItem> getBySetName(String setName) {
        return getBySetName(setName,false);
    }
    @Override
    @Transactional(readOnly=true)
    public Collection<SysInfoItem> getBySetName(String setName, boolean excludeUnnamed) {
        Integer setId = sysInfoSetService.getIdByName(setName);
        if(setId!=null) {
            return getBySetId(setId, excludeUnnamed);
        }
        return Collections.emptyList();
    }
    
    @Override
    @Transactional(readOnly=true)
    public Collection<SysInfoItem> getByTableName(String tableName) {
        return getByTableName(tableName,false);
    }
    @Override
    @Transactional(readOnly=true)
    public Collection<SysInfoItem> getByTableName(String tableName, boolean excludeUnnamed) {
        Integer setId = sysInfoSetService.getIdByTableName(tableName);
        if(setId!=null) {
            return getBySetId(setId, excludeUnnamed);
        }
        return Collections.emptyList();
    }    

    @Override
    @Transactional(readOnly=true)
    public int getMaxId() {
        return sysInfoItemDao.getMaxId();
    }
    
    @Override
    @Transactional(readOnly=true)
    public boolean isValid(int id) {
        return (getById(id)==null)?false:true;
    }
    
    @Override
    @Transactional(readOnly=true)
    public boolean isValid(int setId, String name) {
        return getBySetId(setId).parallelStream()
                .anyMatch(o->o.getName().equalsIgnoreCase(name));
    }
    
    @Override
    @Transactional(readOnly=true)
    public boolean isValidColumn(int setId, String columnName) {
        return getBySetId(setId).parallelStream()
                .anyMatch(o->o.getColumnName().equalsIgnoreCase(columnName));
    }
    
    @Override
    @Transactional(readOnly=true)
    public Collection<SysInfoItem> getPrimaryKeysBySetId(int setId) {
        return getBySetId(setId).parallelStream()
                .filter(o->o.getPrimaryKey())
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly=true)
    public Collection<SysInfoItem> getNotNullBySetId(int setId) {
        return getBySetId(setId).parallelStream()
                .filter(o->!o.getNullable())
                .collect(Collectors.toList());
    }
	
    @Override
    @Transactional(readOnly=true)
    public SysInfoItem getByColumnName(int setId, String columnName) {
        Optional<SysInfoItem> r = getBySetId(setId).parallelStream()
                .filter(o->o.getColumnName().equalsIgnoreCase(columnName))
                .findFirst();
        return r.isPresent()?r.get():null;
    }
    @Override
    @Transactional(readOnly=true)
    public SysInfoItem getByColumnName(String tableName, String columnName) {
        Integer setId = sysInfoSetService.getIdByTableName(tableName);
        if(setId!=null) {
            return getByColumnName(setId, columnName);
        }
        return null;
    }
    @Override
    @Transactional(readOnly=true)
    public SysInfoItem getByName(int setId, String name) {
        Optional<SysInfoItem> r = getBySetId(setId).parallelStream()
                .filter(o->o.getName().equalsIgnoreCase(name))
                .findFirst();
        return r.isPresent()?r.get():null;
    }
    @Override
    @Transactional(readOnly=true)
    public SysInfoItem getByName(String tableName, String name) {
        Integer setId = sysInfoSetService.getIdByTableName(tableName);
        if(setId!=null) {
            return getByName(setId, name);
        }
        return null;
    }
    @Override
    @Transactional(readOnly=true)
    public Collection<SysInfoItem> getBySetId(int setId, Byte type) {
        return getBySetId(setId).parallelStream()
                .filter(o->(o.getType()==type))
                .collect(Collectors.toList());
    }
    @Override
    @Transactional(readOnly=true)
    public Collection<SysInfoItem> getBySetName(String setName, Byte type) {
        Integer setId = sysInfoSetService.getIdByName(setName);
        if(setId!=null) {
            return getBySetId(setId, type);
        }
        return Collections.emptyList();
    }
    @Override
    @Transactional(readOnly=true)
    public Collection<SysInfoItem> getByTableName(String tableName, Byte type) {
        Integer setId = sysInfoSetService.getIdByTableName(tableName);
        if(setId!=null) {
            return getBySetId(setId, type);
        }
        return Collections.emptyList();
    }

	@Override
	@Transactional(readOnly=true)
	public List<Map<String, Object>> findBySQL(String sql) {
		return sysInfoItemDao.findBySQL(sql);
	}
	
    @Override
    @Transactional(readOnly=true)
    public Map<Integer, SysInfoItem> getAllMap() {
        Map<Integer, SysInfoItem> m = new HashMap<Integer, SysInfoItem>();
        for(Integer setId:sysInfoSetService.getAllIds()) {
            getBySetId(setId).stream().forEach(o->m.put(o.getId(), o));
        }
        return m;
    }
    @Override
    @Transactional(readOnly=true)
    public Collection<SysInfoItem> getOrderBySetId(int setId) {
        return getBySetId(setId).parallelStream()
                .filter(o->(o.getSortOrder()!=null&&o.getSortOrder()>0))
                .collect(Collectors.toList());
    }
    @Override
    @Transactional(readOnly=true)
    public Collection<SysInfoItem> getBuiltIn(int setId) {
        Collection<SysInfoItem> collection = new LinkedList<SysInfoItem>();
        for(BuiltInColumn e: BuiltInColumn.values()){
            SysInfoItem o = getByColumnName(setId,e.getValue());
            if(o!=null){
                collection.add(o);
            }
        }
        return collection;
    }
    
    @Override
    @Transactional(readOnly=true)
    public Collection<SysInfoItem> getByInputType(int setId, InputType inputType) {
        return getBySetId(setId).parallelStream()
                .filter(o->inputType.getValue().equals(o.getInputType()))
                .collect(Collectors.toList());
    }
    
    @Override
    public void sort(List<? extends SysInfoItem> list) {
        Collections.sort(list,new Comparator<SysInfoItem>(){  
            @Override  
            public int compare(SysInfoItem b1, SysInfoItem b2) {  
                return (b1.getOrdinal()==null)?-1:((b2.getOrdinal()==null)?1:b1.getOrdinal().compareTo(b2.getOrdinal()));  
            }
        });
    }
    
    @Override
    public void sortByName(List<? extends SysInfoItem> list) {
        Collections.sort(list,new Comparator<SysInfoItem>(){  
            @Override  
            public int compare(SysInfoItem b1, SysInfoItem b2) {  
                return (b1.getName()==null)?-1:((b2.getName()==null)?1:b1.getName().toLowerCase().compareTo(b2.getName().toLowerCase()));  
            }
        });
    }
    
    @Override
    @Transactional(readOnly=true)
    public Collection<SysInfoItem> getByIds(Collection<Integer> ids){
        Collection<SysInfoItem> l = new ArrayList<SysInfoItem>(ids.size());
        for(Integer id:ids){
            SysInfoItem o=this.getById(id);
            if(o!=null) {
                l.add(o);
            }
        }
        return l;
    }
    
    @Override
    @Transactional(readOnly=true)
    public Collection<Integer> getUniqueGroupIds(int setId) {
        Set<Integer> s = new LinkedHashSet<Integer>();
        getBySetId(setId).parallelStream()
            .filter(o->(o.getUniqueGroupId()!=null&&o.getUniqueGroupId()>0))
            .collect(Collectors.toList())
            .stream().forEach(x->s.add(x.getUniqueGroupId()));
        return s;
    }
    @Override
    @Transactional(readOnly=true)
    public Collection<SysInfoItem> getByUniqueGroupId(int setId,int uniqueGroupId) {
        return getBySetId(setId).parallelStream()
                .filter(o -> (o.getUniqueGroupId() != null && o.getUniqueGroupId() == uniqueGroupId))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly=true)
    public String getColumnName(int id) {
        SysInfoItem o=this.getById(id);
        if(null!=o) {
            return o.getColumnName();
        }
        return null;
    }

    @Override
    @Transactional(readOnly=true)
    public Collection<SysInfoItem> getByReadableIds(Collection<Integer> ids) throws CloneException {
        Collection<SysInfoItem> l = new ArrayList<SysInfoItem>(ids.size());
        for(Integer id:ids){
            SysInfoItem o=this.getById(id);
            if(o!=null) {
                SysInfoItem e = (SysInfoItem) cloneObject(o);
                e.setReadable(true);
                e.setWritable(false);
                l.add(e);
            }
        }
        return l;
    }
    @Override
    @Transactional(readOnly=true)
    public Collection<SysInfoItem> getByWritableIds(Collection<Integer> ids) throws CloneException {
        Collection<SysInfoItem> l = new ArrayList<SysInfoItem>(ids.size());
        for(Integer id:ids){
            SysInfoItem o=this.getById(id);
            if(o!=null) {
                SysInfoItem e = (SysInfoItem) cloneObject(o);
                e.setReadable(true);
                e.setWritable(true);
                l.add(e);
            }
        }
        return l;
    }
    @Override
    @Transactional(readOnly=true)
    public Collection<SysInfoItem> getByAccessibleIds(Collection<Integer> readableIds,
            Collection<Integer> writableIds) throws CloneException {
        Set<Integer> idSet = new HashSet<Integer>();
        Collection<SysInfoItem> l = new ArrayList<SysInfoItem>(readableIds.size()+writableIds.size());
        for(SysInfoItem o:getByWritableIds(writableIds)) {
            l.add(o);
            idSet.add(o.getId());
        }
        for(SysInfoItem o:getByReadableIds(readableIds)) {
            if(idSet.contains(o.getId())) {//排重
                continue;
            }
            l.add(o);
            idSet.add(o.getId());
        }
        return l;
    }
}
