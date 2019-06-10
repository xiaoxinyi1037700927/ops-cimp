package com.sinosoft.ops.cimp.repository.sheet.impl;


import com.sinosoft.ops.cimp.common.dao.BaseEntityDaoImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignField;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignFieldDao;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * 
 * @ClassName:  SheetDesignFieldDaoImpl
 * @description: SheetDesignField 数据访问实现类
 * @author:        sunch
 * @date:            2018年6月5日 上午11:19:37
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Repository("sheetDesignFieldDao")
public class SheetDesignFieldDaoImpl extends BaseEntityDaoImpl<SheetDesignField> implements SheetDesignFieldDao {

	public SheetDesignFieldDaoImpl(EntityManagerFactory factory) {
		super(factory);
	}

	@Override
	public Integer getMaxOrdinal() {
		String hql = "select coalesce(max(ordinal),0) from SheetDesignField";
		List list = sessionFactory.getCurrentSession().createQuery(hql).getResultList();
		return (Integer) list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<SheetDesignField> getByDesignId(UUID designId) {
		return sessionFactory.getCurrentSession().createQuery("from SheetDesignField s where s.designId = :designId")
		            .setParameter("designId", designId)
		            .list();
	}

//	@SuppressWarnings("unchecked")
//	@Override
//	public SheetDesignField getBingData(UUID designId,String sectionNo) {
//		return (SheetDesignField)sessionFactory.getCurrentSession().createQuery("from SheetDesignField s where s.designId = :designId and sectionNo=:sectionNo")
//				.setParameter("designId", designId)
//				.setParameter("sectionNo", sectionNo).uniqueResult();
//	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public SheetDesignField getBingData(UUID designId,String sectionNo) {
		return (SheetDesignField)sessionFactory.getCurrentSession().createSQLQuery("select * from sheet_design_field s where s.id=(select b.field_id from sheet_design_field_binding b where b.design_id=:designId and b.section_no=:sectionNo)")
				.addEntity(SheetDesignField.class)
				.setParameter("designId", designId)
				.setParameter("sectionNo", sectionNo).uniqueResult();
	}


	@SuppressWarnings("unchecked")
	@Override
	public SheetDesignField findPrevious(UUID id, UUID designId) {
		String hql = "FROM SheetDesignField T1 WHERE T1.status=0 AND T1.ordinal IS NOT NULL AND T1.id IN (SELECT MIN(T3.id) FROM SheetDesignField T3 " +
				"WHERE T3.status=0 AND T3.id IN (SELECT T4.id FROM SheetDesignField T4 WHERE T4.designId=:designId) AND T3.ordinal IS NOT NULL AND " + 
				"T3.ordinal IN (SELECT MAX(T5.ordinal) FROM SheetDesignField T5 WHERE T5.status=0 AND T5.id IN " + 
				"(SELECT T7.id FROM SheetDesignField T7 WHERE designId=:designId) AND T5.id<>:id AND T5.ordinal IS NOT NULL AND " +
				"T5.ordinal<(SELECT T6.ordinal FROM SheetDesignField T6 WHERE T6.id=:id)))";
		List<SheetDesignField> list = sessionFactory.getCurrentSession().createQuery(hql)
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
		String hql = "UPDATE SheetDesignField SET ordinal=:newOrdinal, lastModifiedBy=:userName, lastModifiedTime=SYSDATE WHERE id=:id";
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
	public SheetDesignField findNext(UUID id, UUID designId) {
		String hql = "FROM SheetDesignField T1 WHERE T1.status=0 AND T1.ordinal IS NOT NULL AND T1.id IN (SELECT MIN(T3.id) FROM SheetDesignField T3 " +
				"WHERE T3.status=0 AND T3.id IN (SELECT T4.id FROM SheetDesignField T4 WHERE T4.designId=:designId) AND T3.ordinal IS NOT NULL AND " + 
				"T3.ordinal IN (SELECT MIN(T5.ordinal) FROM SheetDesignField T5 WHERE T5.status=0 AND T5.id IN " + 
				"(SELECT T7.id FROM SheetDesignField T7 WHERE designId=:designId) AND T5.id<>:id AND T5.ordinal IS NOT NULL AND " +
				"T5.ordinal>(SELECT T6.ordinal FROM SheetDesignField T6 WHERE T6.id=:id)))";
		List<SheetDesignField> list = sessionFactory.getCurrentSession().createQuery(hql)
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
	public void deleteByDesignId(UUID designId) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.setFlushMode(FlushMode.MANUAL);
		session.createQuery("delete from SheetDesignField where designId=:designId")
			.setParameter("designId", designId)
			.executeUpdate();
		session.flush();
		session.getTransaction().commit();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void deleteFieldBindingByFieldId(UUID fieldId) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.setFlushMode(FlushMode.MANUAL);
		session.createSQLQuery("delete from sheet_design_field_binding where field_id=:fieldId")
			.setParameter("fieldId", fieldId)
			.executeUpdate();
		session.flush();
		session.getTransaction().commit();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void deleteFieldBindingByDesignId(UUID designId) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.setFlushMode(FlushMode.MANUAL);
		session.createSQLQuery("delete from sheet_design_field_binding where design_id=:designId")
			.setParameter("designId", designId)
			.executeUpdate();
		session.flush();
		session.getTransaction().commit();
	}
}
