/**
 * @Project:      IIMP
 * @Title:           SysInfoSetServiceImpl.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.service.infostruct.impl;


import com.sinosoft.ops.cimp.common.service.BaseEntityServiceImpl;
import com.sinosoft.ops.cimp.entity.infostruct.InfoSetType;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoItem;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoSet;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoSetCategory;
import com.sinosoft.ops.cimp.exception.CloneException;
import com.sinosoft.ops.cimp.repository.infostruct.SysInfoSetCategoryDao;
import com.sinosoft.ops.cimp.repository.infostruct.SysInfoSetDao;
import com.sinosoft.ops.cimp.service.infostruct.SysInfoItemService;
import com.sinosoft.ops.cimp.service.infostruct.SysInfoSetService;
import com.sinosoft.ops.cimp.service.system.SysCacheService;
import org.apache.commons.lang3.StringUtils;
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


/**
 * @ClassName:  SysInfoSetServiceImpl
 * @Description: 信息集服务实现类
 * @Author:        wft
 * @Date:            2017年8月18日 下午4:37:51
 * @Version        1.0.0
 */

@Service("sysInfoSetService")
public class SysInfoSetServiceImpl extends BaseEntityServiceImpl<SysInfoSet> implements SysInfoSetService {
    private static final Logger logger = LoggerFactory.getLogger(SysInfoSetServiceImpl.class);
    /*** 缓存名称 */
    public static final String CACHE_NAME = "infoSetCache";
    private static final AtomicBoolean preInitialized = new AtomicBoolean(false);
        
    @Autowired
	private SysInfoSetDao sysInfoSetDao;
	@Autowired
	private SysInfoItemService sysInfoItemService;
	@Autowired
	private SysInfoSetCategoryDao sysInfoSetCategoryDao;
	
    private final SysCacheService sysCacheService;
    private final Cache cache;
    
    @Autowired
    public SysInfoSetServiceImpl(SysCacheService sysCacheService) {
        this.sysCacheService = sysCacheService;
        this.cache = sysCacheService.getByName(CACHE_NAME);
    }
    
    @Override
    @Transactional
    public Serializable create(SysInfoSet entity){
        Serializable id = sysInfoSetDao.save(entity);
        sysCacheService.reset(CACHE_NAME);
        return id;
    }

    @Override
    @Transactional(readOnly=true)
    public void preInitialize() {
        if(preInitialized.compareAndSet(false,true)) {
            cache.clear();
            Collection<SysInfoSet> l=sysInfoSetDao.getAll();
            for (SysInfoSet o : l) {
                cache.put(o.getId(), o);
                cache.put(o.getName(), o.getId());
                cache.put(o.getTableName(), o.getId());
            }
            logger.info("初始化缓存"+cache.getName()+"完成!(共"+l.size()+"项)");
        }
    }

    @Override
    @Transactional
    public void update(SysInfoSet entity){
        sysInfoSetDao.update(entity);
        sysCacheService.reset(CACHE_NAME);
    }
    
    @Override
    @Transactional
    public void delete(SysInfoSet entity){
        sysInfoSetDao.delete(entity);
        sysCacheService.reset(CACHE_NAME);
    }
    
    @Override
    @Transactional
    public void deleteById(Serializable id){
        sysInfoSetDao.deleteById(id);
        sysCacheService.reset(CACHE_NAME);
    }
    
    @Override
    @Transactional(readOnly=true)
    public SysInfoSet getById(Serializable id){
        SysInfoSet o = null;
        ValueWrapper v = cache.get(id);
        if(v!=null) {
            o = (SysInfoSet)v.get();
            if(o!=null) {
                return o;
            }
        }
        o = sysInfoSetDao.getById(id);
        if(o!=null) {
            cache.put(id, o);
        }
        return o;
    }
    
    @Override
    @Transactional
    public SysInfoSet merge(SysInfoSet entity){
        sysCacheService.reset(CACHE_NAME);
        return sysInfoSetDao.merge(entity);
    }
    
    @Override
    @Transactional(readOnly=true)
    public Integer getIdByName(String name) {
        Integer o = null;
        ValueWrapper v = cache.get(name);
        if(v!=null) {
            o = (Integer)v.get();
            if(o!=null) {
                return o;
            }
        }
        o = sysInfoSetDao.getIdByName(name);
        if(o!=null) {
            cache.put(name, o);
        }
        return o;
    }
    @Override
    @Transactional(readOnly=true)
    public Integer getIdByTableName(String tableName) {
        Integer o = null;
        ValueWrapper v = cache.get(tableName);
        if(v!=null) {
            o = (Integer)v.get();
            if(o!=null) {
                return o;
            }
        }
        o = sysInfoSetDao.getIdByTableName(tableName);
        if(o!=null) {
            cache.put(tableName, o);
        }
        return o;
    }
    
    @Override
    @Transactional(readOnly=true)
    public SysInfoSet getByName(String name) {
        Integer id = getIdByName(name);
        if(id!=null) {
            return getById(id);
        }
        return null;
    }
    @Override
    @Transactional(readOnly=true)
    public SysInfoSet getByTableName(String tableName) {
        Integer id = getIdByTableName(tableName);
        if(id!=null) {
            return getById(id);
        }
        return null;
    }
    
    @Override
    @Transactional(readOnly=true)
    public boolean isValid(int id) {
        return this.getById(id)!=null;
    }
    @Override
    @Transactional(readOnly=true)
    public boolean isValid(Collection<Integer> ids) {
        for(int id:ids){
            if(this.getById(id)==null){
                return false;
            }
        }
        return true;
    }    
    
    @Override
    @Transactional(readOnly=true)
    public boolean isValidName(String name) {
        return this.getByName(name)!=null;
    }
    @Override
    @Transactional(readOnly=true)
    public boolean isValidName(Collection<String> names) {
        for(String name:names){
            if(this.getByName(name)==null){
                return false;
            }
        }
        return true;
    }

    @Override
    @Transactional(readOnly=true)
    public boolean isValidTableName(String tableName) {
        return this.getByTableName(tableName)!=null;
    }
    @Override
    @Transactional(readOnly=true)
    public boolean isValidTableName(Collection<String> tableNames) {
        for(String tableName:tableNames){
            if(this.getByTableName(tableName)==null){
                return false;
            }
        }
        return true;
    }
    
    @Override
    @Transactional(readOnly=true)
    public Collection<SysInfoSet> getByIds(Collection<Integer> ids) {
        Collection<SysInfoSet> l = new ArrayList<SysInfoSet>(ids.size());
        for(Integer id:ids){
            SysInfoSet infoSet=getById(id);
            if(infoSet!=null){
                l.add(infoSet);
            }
        }
        return l;
    }
    @Override
    @Transactional(readOnly=true)
    public Collection<SysInfoSet> getByNames(Collection<String> names) {
        Collection<SysInfoSet> infoSets = new ArrayList<SysInfoSet>(names.size());
        for(String name:names){
            SysInfoSet infoSet=getByName(name);
            if(infoSet!=null){
                infoSets.add(infoSet);
            }
        }
        return infoSets;
    }
    @Override
    @Transactional(readOnly=true)
    public Collection<SysInfoSet> getByTableNames(Collection<String> tableNames) {
        Collection<SysInfoSet> infoSets = new ArrayList<SysInfoSet>(tableNames.size());
        for(String tableName:tableNames){
            SysInfoSet infoSet=getByTableName(tableName);
            if(infoSet!=null){
                infoSets.add(infoSet);
            }
        }
        return infoSets;
    }
    
    @Override
    @Transactional(readOnly=true)
    public int getMaxId() {
        return sysInfoSetDao.getMaxId();
    }
    
    @Override
    @Transactional(readOnly=true)
    public Collection<SysInfoSet> getAllOrderByNameCn() {
        return sysInfoSetDao.getAllOrderByNameCn();
    }
    
    @Override
    @Transactional(readOnly=true)
    public Collection<SysInfoSet> getByType(InfoSetType type) {
        return sysInfoSetDao.getByType(type.getValue());
    }
	@Override
	@Transactional(readOnly=true)
	public Collection<SysInfoSetCategory> getInfoSetTreeByType(InfoSetType type) {
		Collection<SysInfoSetCategory> categoryRoot = new LinkedList<SysInfoSetCategory>();
		Map<Integer,SysInfoSetCategory> map = new HashMap<Integer,SysInfoSetCategory>();
		Collection<SysInfoSet> sets = sysInfoSetDao.getByType(type.getValue());
		Collection<SysInfoSetCategory> categorys = sysInfoSetCategoryDao.getAll();
		
		for(SysInfoSetCategory entity:categorys){
			entity.setLeaf(false);
			map.put(entity.getId(), entity);
		}
		for(SysInfoSet entity:sets){
			Integer key= entity.getCategoryId();
			if(key == null){
				continue;
			}
			entity.setLeaf(true);
			SysInfoSetCategory category=map.get(key);
			if(category!=null) {
			    map.get(key).getChildren().add(entity);
			}
		}
		for(Integer key:map.keySet()){
			SysInfoSetCategory infoSetCategory = map.get(key);
			categoryRoot.add(infoSetCategory);
		}
		return categoryRoot;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly=true)
    public Collection<Integer> getIdsByGroupId(Integer groupId){
	    Collection<Integer> l = null;
        String key = new StringBuilder("GI_").append(groupId).toString();
        ValueWrapper o = cache.get(key);
        if(o!=null) {
            l = (Collection<Integer>) o.get();
            if(l!=null) {
                return l;
            }
        }
        l = sysInfoSetDao.getIdsByGroupId(groupId);
        cache.put(key, l);
        return l;
	}
    @Override
    @Transactional(readOnly=true)
    public Collection<SysInfoSet> getByGroupId(Integer groupId) {
        List<SysInfoSet> l = new LinkedList<SysInfoSet>();
        for(Integer id:getIdsByGroupId(groupId)) {
            SysInfoSet o = getById(id);
            if(o!=null) {
                l.add(o);
            }
        }
        Collections.sort(l,new Comparator<SysInfoSet>(){  
            @Override  
            public int compare(SysInfoSet b1, SysInfoSet b2) {  
                return (b1.getOrdinal()==null)?-1:(b2.getOrdinal()==null)?1:b1.getOrdinal().compareTo(b2.getOrdinal());  
            }
        });
        return l;
    }
    @Override
    @Transactional(readOnly=true)
    public SysInfoSet getGroupMainSet(Integer groupId) {
        for(SysInfoSet o:this.getByGroupId(groupId)){
            if(o.getGroupMain()){
                return o;
            }
        }
        return null;
    }
    @Override
    @Transactional(readOnly=true)
    public Collection<SysInfoSet> getGroupSubSet(Integer groupId) {
        List<SysInfoSet> list = new LinkedList<SysInfoSet>();
        for(SysInfoSet o:this.getByGroupId(groupId)){
            if(!o.getGroupMain()){
                list.add(o);
            }
        }
        return list;
    }
	@Override
	 @Transactional(readOnly=true)
	public String findbysetid(int parseInt) {
	    SysInfoSet o = this.getById(parseInt);
		return (o==null)?"":o.getDescription();
	}
	@Override
	@Transactional(readOnly=true)
	public SysInfoItem findInfoItemby(Integer id) {
		// TODO Auto-generated method stub
		return sysInfoSetDao.findInfoItemby(id);
	}
	
    @Override
    @Transactional(readOnly=true)
    public String getName(int id) {
        SysInfoSet o = this.getById(id);
        return (o==null)?null:o.getName();
    }
    @Override
    @Transactional(readOnly=true)
    public String getNameCn(int id) {
        SysInfoSet o = this.getById(id);
        return (o==null)?null:o.getNameCn();
    }
    @Override
    @Transactional(readOnly=true)
    public String getTableName(int id){
        SysInfoSet o = this.getById(id);
        return (o==null)?null:o.getTableName();
    }
    @Override
    @Transactional(readOnly=true)
    public String getDescription(int id) {
        SysInfoSet o = this.getById(id);
        return (o==null)?null:o.getDescription();
    }
    
    @Override
	@Transactional(readOnly = true)
	public Collection<SysInfoSetCategory> getInfoSetTreeColByType(InfoSetType type) {

		Collection<SysInfoSetCategory> rootcollection = new LinkedList<SysInfoSetCategory>();
		Map<Integer, SysInfoSetCategory> map = new HashMap<Integer, SysInfoSetCategory>();
		Collection<SysInfoSet> ifscollection = sysInfoSetDao.getByType(type.getValue());
		Collection<SysInfoSetCategory> ifsccollection = sysInfoSetCategoryDao.getAll();

		for (SysInfoSetCategory entity : ifsccollection) {
			entity.setLeaf(false);
			map.put(entity.getId(), entity);
		}
		for (SysInfoSet entity : ifscollection) {
			Integer key = entity.getCategoryId();
			if (key == null) {
				continue;
			}
			entity.setLeaf(false);
			// 根据表名获取字段
			Integer setId = entity.getId();
			String tbl = entity.getNameCn();
			Collection<SysInfoItem> colList = sysInfoItemService.getBySetId(setId);
			if (colList != null) {
				Collection<SysInfoSet> colChildren = new ArrayList<SysInfoSet>();
				for (SysInfoItem item : colList) {
					String zh = item.getNameCn();
					if (StringUtils.isNotEmpty(zh) && zh.length() > 0) {
						Integer colId = item.getId();
						//
						SysInfoSet set = new SysInfoSet();
						set.setId(colId);
						set.setNameCn(zh);
						set.setName(tbl + "." + zh);

						colChildren.add(set);
					}
				}
				entity.setChildren(colChildren);
			}

			map.get(key).getChildren().add(entity);
		}
		for (Integer key : map.keySet()) {
			SysInfoSetCategory infoSetCategory = map.get(key);
			rootcollection.add(infoSetCategory);
		}

		return rootcollection;
	}
    
    @Override
    public void sort(List<? extends SysInfoSet> list) {
        Collections.sort(list,new Comparator<SysInfoSet>(){  
            @Override  
            public int compare(SysInfoSet b1, SysInfoSet b2) {  
                return (b1.getOrdinal()==null)?-1:((b2.getOrdinal()==null)?1:b1.getOrdinal().compareTo(b2.getOrdinal()));  
            }
        });
    }
    
    @Override
    public void sortByName(List<? extends SysInfoSet> list) {
        Collections.sort(list,new Comparator<SysInfoSet>(){  
            @Override  
            public int compare(SysInfoSet b1, SysInfoSet b2) {  
                return (b1.getName()==null)?-1:((b2.getName()==null)?1:b1.getName().toLowerCase().compareTo(b2.getName().toLowerCase()));  
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly=true)
    public Collection<Integer> getAllIds() {
        Collection<Integer> l = null;
        String key = "IS_ALL";
        ValueWrapper o = cache.get(key);
        if(o!=null) {
            l = (Collection<Integer>) o.get();
            if(l!=null) {
                return l;
            }
        }
        l = sysInfoSetDao.getAllIds();
        cache.put(key, l);
        return l;
    }
    @Override
    @Transactional(readOnly=true)
    public Collection<SysInfoSet> getAll() {
        Collection<SysInfoSet> l = new LinkedList<SysInfoSet>();
        for(Integer id:getAllIds()) {
            SysInfoSet o = getById(id);
            if(o!=null) {
                l.add(o);
            }
        }
        return l;
    }

    @Override
    @Transactional(readOnly=true)
    public Collection<SysInfoSet> getByInsertableIds(Collection<Integer> ids) throws CloneException {
        Collection<SysInfoSet> l = new ArrayList<SysInfoSet>(ids.size());
        for(Integer id:ids){
            SysInfoSet o=this.getById(id);
            if(o!=null) {
                SysInfoSet e = (SysInfoSet) cloneObject(o);
                e.setInsertable(true);
                e.setDeletable(false);
                e.setUpdatable(false);
                e.setSelectable(true);
                l.add(e);
            }
        }
        return l;
    }
    @Override
    @Transactional(readOnly=true)
    public Collection<SysInfoSet> getByDeletableIds(Collection<Integer> ids) throws CloneException {
        Collection<SysInfoSet> l = new ArrayList<SysInfoSet>(ids.size());
        for(Integer id:ids){
            SysInfoSet o=this.getById(id);
            if(o!=null) {
                SysInfoSet e = (SysInfoSet) cloneObject(o);
                e.setInsertable(false);
                e.setDeletable(true);
                e.setUpdatable(false);
                e.setSelectable(true);
                l.add(e);
            }
        }
        return l;
    }
    @Override
    @Transactional(readOnly=true)
    public Collection<SysInfoSet> getByUpdatableIds(Collection<Integer> ids) throws CloneException {
        Collection<SysInfoSet> l = new ArrayList<SysInfoSet>(ids.size());
        for(Integer id:ids){
            SysInfoSet o=this.getById(id);
            if(o!=null) {
                SysInfoSet e = (SysInfoSet) cloneObject(o);
                e.setInsertable(false);
                e.setDeletable(false);
                e.setUpdatable(true);
                e.setSelectable(true);
                l.add(e);
            }
        }
        return l;
    }
    @Override
    @Transactional(readOnly=true)
    public Collection<SysInfoSet> getBySelectableIds(Collection<Integer> ids) throws CloneException {
        Collection<SysInfoSet> l = new ArrayList<SysInfoSet>(ids.size());
        for(Integer id:ids){
            SysInfoSet o=this.getById(id);
            if(o!=null) {
                SysInfoSet e = (SysInfoSet) cloneObject(o);
                e.setInsertable(false);
                e.setDeletable(false);
                e.setUpdatable(false);
                e.setSelectable(true);
                l.add(e);
            }
        }
        return l;
    }
    @Override
    @Transactional(readOnly=true)
    public Collection<SysInfoSet> getByAccessibleIds(Collection<Integer> insertableIds,
            Collection<Integer> deletableIds, Collection<Integer> updatableIds, Collection<Integer> selectableIds)
            throws CloneException {
        Set<Integer> ids=new HashSet<Integer>();
        ids.addAll(insertableIds);
        ids.addAll(deletableIds);
        ids.addAll(updatableIds);
        ids.addAll(selectableIds);
        Collection<SysInfoSet> l = new ArrayList<SysInfoSet>();
        for(Integer id:ids){
            SysInfoSet o=this.getById(id);
            if(o!=null) {
                SysInfoSet e = (SysInfoSet) cloneObject(o);
                e.setInsertable(insertableIds.contains(o.getId()));
                e.setDeletable(deletableIds.contains(o.getId()));
                e.setUpdatable(updatableIds.contains(o.getId()));
                e.setSelectable(selectableIds.contains(o.getId()));
                l.add(e);
            }
        }
        return l;
    }
}
