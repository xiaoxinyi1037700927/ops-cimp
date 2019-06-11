package com.sinosoft.ops.cimp.repository.sheet.impl;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDaoImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignSection;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignSectionDao;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository("sheetDesignSectionDao")
public class SheetDesignSectionDaoImpl extends BaseEntityDaoImpl<SheetDesignSection> implements SheetDesignSectionDao {

	public SheetDesignSectionDaoImpl(EntityManagerFactory factory) {
		super(factory);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<SheetDesignSection> getByDesignId(UUID designId) {
		return getByDesignId(designId, null,false);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<SheetDesignSection> getByDesignId(UUID designId,String orderField,boolean desc) {
		String hql = "from SheetDesignSection s where s.designId = :designId";
		if(StringUtils.isNotBlank(orderField)){
			if(desc){
				hql += " order by s."+orderField + " desc";
			}else{
				hql += " order by s."+orderField ;
			}
		}
		return sessionFactory.getCurrentSession().createQuery(hql)
		            .setParameter("designId", designId)
		            .list();
	}

	/**
	 * 获取最大排序号
	 */
	@Override
	public Integer getMaxOrdinal() {
		String hql = "select coalesce(max(ordinal),0) from SheetDesignSection";
		List list = sessionFactory.getCurrentSession().createQuery(hql).getResultList();
		return (Integer) list.get(0);
	}

	@Override
	public String getMaxSectionNoByDesignId(UUID designId) {
		String hql = "select coalesce(max(sectionNo),0) from SheetDesignSection where designId=:designId";
		List list = sessionFactory.getCurrentSession().createQuery(hql).setParameter("designId", designId).getResultList();
		return (String) list.get(0);
	}

	@Override
	public void deleteByDesignId(UUID designId) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.setFlushMode(FlushMode.MANUAL);
		session.createQuery("delete from SheetDesignSection where designId=:designId")
			.setParameter("designId", designId).executeUpdate();
		session.flush();
		session.getTransaction().commit();
	}

	@SuppressWarnings("unchecked")
	@Override
	public SheetDesignSection findPrevious(UUID id, UUID designId) {
		String hql = "FROM SheetDesignSection T1 WHERE T1.status=0 AND T1.ordinal IS NOT NULL AND T1.id IN (SELECT MIN(T3.id) FROM SheetDesignSection T3 " +
				"WHERE T3.status=0 AND T3.id IN (SELECT T4.id FROM SheetDesignSection T4 WHERE T4.designId=:designId) AND T3.ordinal IS NOT NULL AND " + 
				"T3.ordinal IN (SELECT MAX(T5.ordinal) FROM SheetDesignSection T5 WHERE T5.status=0 AND T5.id IN " + 
				"(SELECT T7.id FROM SheetDesignSection T7 WHERE designId=:designId) AND T5.id<>:id AND T5.ordinal IS NOT NULL AND " +
				"T5.ordinal<(SELECT T6.ordinal FROM SheetDesignSection T6 WHERE T6.id=:id)))";
		List<SheetDesignSection> list = sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("id", id)
                .setParameter("designId", designId)
                .list();
		if (list.size() > 0) {
			return list.get(list.size() - 1);
		} else {
			return null;
		}
	}

	@Override
	public int updateOrdinal(UUID preId, int ordinal, UUID userName) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.setFlushMode(FlushMode.MANUAL);
		String hql = "UPDATE SheetDesignSection SET ordinal=:newOrdinal, lastModifiedBy=:userName, lastModifiedTime=SYSDATE WHERE id=:id";
        int cnt = session.createQuery(hql)
                .setParameter("id", preId)
                .setParameter("userName", userName)
                .setParameter("newOrdinal", ordinal)
                .executeUpdate();
		session.flush();
		session.getTransaction().commit();
		return cnt;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SheetDesignSection findNext(UUID id, UUID designId) {
		String hql = "FROM SheetDesignSection T1 WHERE T1.status=0 AND T1.ordinal IS NOT NULL AND T1.id IN (SELECT MIN(T3.id) FROM SheetDesignSection T3 " +
				"WHERE T3.status=0 AND T3.id IN (SELECT T4.id FROM SheetDesignSection T4 WHERE T4.designId=:designId) AND T3.ordinal IS NOT NULL AND " + 
				"T3.ordinal IN (SELECT MIN(T5.ordinal) FROM SheetDesignSection T5 WHERE T5.status=0 AND T5.id IN " + 
				"(SELECT T7.id FROM SheetDesignSection T7 WHERE designId=:designId) AND T5.id<>:id AND T5.ordinal IS NOT NULL AND " +
				"T5.ordinal>(SELECT T6.ordinal FROM SheetDesignSection T6 WHERE T6.id=:id)))";
		List<SheetDesignSection> list = sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("id", id)
                .setParameter("designId", designId)
                .list();
		if (list.size() > 0) {
			return list.get(list.size() - 1);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Integer> getRangeByDesignId(UUID designId) {
		return (Map<String, Integer>) sessionFactory.getCurrentSession().createQuery("select new map(min(startRowNo) as startRowNo,max(endRowNo) as endRowNo,min(startColumnNo) as startColumnNo,max(endColumnNo) as endColumnNo) from SheetDesignSection where designId=:designId")
				.setParameter("designId", designId)
				.uniqueResult();
		
	}
	
 
}
