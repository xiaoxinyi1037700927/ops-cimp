/**
 * @Project:      IIMP
 * @Title:           SysInfoItemDaoImpl.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.repository.infostruct.impl;


import com.sinosoft.ops.cimp.common.dao.BaseEntityDaoImpl;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoItem;
import com.sinosoft.ops.cimp.repository.infostruct.SysInfoItemDao;
import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:  SysInfoItemDaoImpl
 * @Description: 信息项数据访问实现类
 * @Author:        wft
 * @Date:            2017年8月18日 下午4:39:29
 * @Version        1.0.0
 */
@Repository("sysInfoItemDao")
public class SysInfoItemDaoImpl extends BaseEntityDaoImpl<SysInfoItem> implements SysInfoItemDao {

    public SysInfoItemDaoImpl(EntityManagerFactory factory) {
        super(factory);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Integer> getIdsBySetId(int setId) {
        return sessionFactory.getCurrentSession().createQuery("select id from SysInfoItem where status=0 and infoSetId=:setId order by ordinal")
                .setParameter("setId", setId)
                .list();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Collection<SysInfoItem> getBySetId(int setId) {
        StringBuilder sb = new StringBuilder("from SysInfoItem i where i.infoSetId=:setId and i.status=0"); 
        sb.append(" order by i.ordinal");
        return sessionFactory.getCurrentSession().createQuery(sb.toString())
                .setParameter("setId", setId)
                .list();
    }
    @SuppressWarnings("unchecked")
    @Override
    public Collection<SysInfoItem> getBySetIdAndAppId(int setId,int appid) {
    	StringBuilder sb = new StringBuilder("select * from SYS_INFO_ITEM where info_set_id=:setId and status=0 and  id in ( select info_item_id from SYS_APP_INFO_ITEM where app_id=:appid ) order by ordinal"); 
       // sb.append(" order by i.ordinal");
        return sessionFactory.getCurrentSession().createNativeQuery(sb.toString()).addEntity(SysInfoItem.class)
                .setParameter("setId", setId).setParameter("appid", appid).list();
    }
    
    @Override
    public Collection<SysInfoItem> getBySetName(String setName) {
        return getBySetName(setName,false);
    }
    @SuppressWarnings("unchecked")
    @Override
    public Collection<SysInfoItem> getBySetName(String setName, boolean excludeUnnamed) {
        StringBuilder sb = new StringBuilder("from SysInfoItem i where i.status=0 and exists ( select 1 from SysInfoSet s where i.infoSetId=s.id and s.name = :setName )"); 
        if(excludeUnnamed){
            sb.append(" and  i.description > ' '");
        }
        sb.append(" order by i.ordinal");
        return sessionFactory.getCurrentSession().createQuery(sb.toString())
                .setParameter("setName", setName).list();
    }
    
    @Override
    public Collection<SysInfoItem> getByTableName(String tableName) {
        return getByTableName(tableName,false);
    }
    @SuppressWarnings("unchecked")
    @Override
    public Collection<SysInfoItem> getByTableName(String tableName, boolean excludeUnnamed) {
        StringBuilder sb = new StringBuilder("from SysInfoItem i where i.status=0 and exists ( select 1 from SysInfoSet s where i.infoSetId=s.id and s.tableName = :tableName )"); 
        if(excludeUnnamed){
            sb.append(" and  i.description > ' '");
        }
        sb.append(" order by i.ordinal");
        return sessionFactory.getCurrentSession().createQuery(sb.toString())
                .setParameter("tableName", tableName).list();
    }
    
    @Override
    public int getMaxId() {
        Object obj = sessionFactory.getCurrentSession().createQuery("select MAX(id) from SysInfoItem")
                .uniqueResult();
        return obj==null?0:(int)obj;
    }

    @Override
    public SysInfoItem getByColumnName(Integer infoSetId, String columnName) {
        return (SysInfoItem) sessionFactory.getCurrentSession().createQuery("from SysInfoItem where status=0 and infoSetId =:infoSetId and columnName=:columnName")
                .setParameter("infoSetId", infoSetId)
                .setParameter("columnName", columnName)
                .uniqueResult();
    }
    @Override
    public SysInfoItem getByColumnName(String tableName, String columnName) {
        return (SysInfoItem) sessionFactory.getCurrentSession().createQuery("from SysInfoItem where status=0 and infoSetId = ( select id from SysInfoSet where tableName=:tableName )  and columnName=:columnName")
                .setParameter("tableName", tableName)
                .setParameter("columnName", columnName)
                .uniqueResult();
    }  

    @SuppressWarnings("unchecked")
    @Override
    public Collection<SysInfoItem> getBySetId(int setId, Byte type) {
        return sessionFactory.getCurrentSession().createQuery("from SysInfoItem where status=0 and infoSetId =:infoSetId and type=:type order by ordinal")
                .setParameter("infoSetId", setId)
                .setParameter("type", type)
                .list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<SysInfoItem> getBySetName(String setName, Byte type) {
        StringBuilder sb = new StringBuilder("from SysInfoItem i where i.status=0 and exists ( select 1 from SysInfoSet s where i.infoSetId=s.id and s.name = :setName )"); 
        sb.append(" and i.type = :type ");
        sb.append(" order by i.ordinal");
        return sessionFactory.getCurrentSession().createQuery(sb.toString())
                .setParameter("setName", setName)
                .setParameter("type", type)
                .list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<SysInfoItem> getByTableName(String tableName, Byte type) {
        StringBuilder sb = new StringBuilder("from SysInfoItem i where i.status=0 and exists ( select 1 from SysInfoSet s where i.infoSetId=s.id and s.tableName = :tableName )"); 
        sb.append(" and i.type = :type ");
        sb.append(" order by i.ordinal");
        return sessionFactory.getCurrentSession().createQuery(sb.toString())
                .setParameter("tableName", tableName)
                .setParameter("type", type)
                .list();                
    }
	@SuppressWarnings({ "unchecked", "deprecation" })
    @Override
	public List<Map<String, Object>> findBySQL(String sql) {
    	return sessionFactory.getCurrentSession().createNativeQuery(sql).setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).list();
	}
	
    @SuppressWarnings("unchecked")
    @Override
    public Collection<SysInfoItem> getAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from SysInfoItem where status=0 order by infoSetId,ordinal")
                .list();
    }
}
