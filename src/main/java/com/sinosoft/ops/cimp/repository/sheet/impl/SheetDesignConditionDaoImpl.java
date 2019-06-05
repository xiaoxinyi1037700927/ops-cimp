package com.sinosoft.ops.cimp.repository.sheet.impl;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDaoImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignCondition;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignConditionDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository("sheetDesignConditionDao")
public class SheetDesignConditionDaoImpl  extends BaseEntityDaoImpl<SheetDesignCondition> implements SheetDesignConditionDao {

	public SheetDesignConditionDaoImpl(EntityManagerFactory factory) {
		super(factory);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SheetDesignCondition> getByDesignId(UUID designId) {
		return sessionFactory.getCurrentSession().createQuery("select new SheetDesignCondition(a.id,a.designId, a.conditionId,b.conditionName,b.sql,a.endColumnNo,a.endRowNo,a.startColumnNo,a.startRowNo,a.sectionNo) from SheetDesignCondition a,SheetCondition b where a.conditionId=b.id and  a.designId= :designId order by b.ordinal")
	            .setParameter("designId", designId)
				.list();
	}
	
	@Override
	public List<SheetDesignCondition> getHasCategoryByDesignId(UUID designId) {
		return sessionFactory.getCurrentSession().createQuery("select new SheetDesignCondition(a.id,a.designId, a.conditionId,b.conditionName,b.sql,a.endColumnNo,a.endRowNo,a.startColumnNo,a.startRowNo,a.sectionNo) from SheetDesignCondition a,SheetCondition b where a.conditionId=b.id and  a.designId= :designId and b.categoryId != null order by b.ordinal")
	            .setParameter("designId", designId)
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<SheetDesignCondition> getByConditionId(UUID conditionId) {
		return sessionFactory.getCurrentSession().createQuery("from SheetDesignCondition s where s.conditionId = :conditionId")
	            .setParameter("conditionId", conditionId)
	            .list();
	}


	@SuppressWarnings("unchecked")
	@Override
	public SheetDesignCondition getBingData(UUID designId,String sectionNo) {
		return (SheetDesignCondition) sessionFactory.getCurrentSession().createQuery("from SheetDesignCondition where designId = :designId and sectionNo=:sectionNo")
				.setParameter("designId", designId)
				.setParameter("sectionNo", sectionNo).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public SheetDesignCondition findNext(UUID id, UUID designId) {
		String hql = "FROM SheetDesignCondition T1 WHERE T1.status=0 AND T1.ordinal IS NOT NULL AND T1.id IN (SELECT MIN(T3.id) FROM SheetDesignCondition T3 " +
				"WHERE T3.status=0 AND T3.id IN (SELECT T4.id FROM SheetDesignCondition T4 WHERE T4.designId=:designId) AND T3.ordinal IS NOT NULL AND " + 
				"T3.ordinal IN (SELECT MIN(T5.ordinal) FROM SheetDesignCondition T5 WHERE T5.status=0 AND T5.id IN " + 
				"(SELECT T7.id FROM SheetDesignCondition T7 WHERE designId=:designId) AND T5.id<>:id AND T5.ordinal IS NOT NULL AND " +
				"T5.ordinal>(SELECT T6.ordinal FROM SheetDesignCondition T6 WHERE T6.id=:id)))";
		List<SheetDesignCondition> list = sessionFactory.getCurrentSession().createQuery(hql)
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
	public SheetDesignCondition findPrevious(UUID id, UUID designId) {
		String hql = "FROM SheetDesignCondition T1 WHERE T1.status=0 AND T1.ordinal IS NOT NULL AND T1.id IN (SELECT MIN(T3.id) FROM SheetDesignCondition T3 " +
				"WHERE T3.status=0 AND T3.id IN (SELECT T4.id FROM SheetDesignCondition T4 WHERE T4.designId=:designId) AND T3.ordinal IS NOT NULL AND " + 
				"T3.ordinal IN (SELECT MAX(T5.ordinal) FROM SheetDesignCondition T5 WHERE T5.status=0 AND T5.id IN " + 
				"(SELECT T7.id FROM SheetDesignCondition T7 WHERE designId=:designId) AND T5.id<>:id AND T5.ordinal IS NOT NULL AND " +
				"T5.ordinal<(SELECT T6.ordinal FROM SheetDesignCondition T6 WHERE T6.id=:id)))";
		List<SheetDesignCondition> list = sessionFactory.getCurrentSession().createQuery(hql)
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
		String hql = "UPDATE SheetDesignCondition SET ordinal=:newOrdinal, lastModifiedBy=:userName, lastModifiedTime=SYSDATE WHERE id=:id";
        int cnt = sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("id", preId)
                .setParameter("userName", userName)
                .setParameter("newOrdinal", ordinal)
                .executeUpdate();
		return cnt;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<SheetDesignCondition> getByDesignIdDistinct(UUID designId) {
		return sessionFactory.getCurrentSession().createQuery("select distinct new SheetDesignCondition(a.designId, a.conditionId,b.conditionName) from SheetDesignCondition a,SheetCondition b where a.conditionId=b.id and  a.designId= :designId")
	            .setParameter("designId", designId)
				.list();
	}

	@Override
	public int deleteByDesignIdAndConditionId(UUID designId, UUID conditionId) {
		return sessionFactory.getCurrentSession().createQuery("delete from SheetDesignCondition where designId=:designId and conditionId=:conditionId")
				.setParameter("designId", designId)
				.setParameter("conditionId", conditionId)
				.executeUpdate();
	}

	@Override
	public void deleteByDesignId(UUID designId) {
		sessionFactory.getCurrentSession().createQuery("delete from SheetDesignCondition where designId=:designId")
			.setParameter("designId", designId)
			.executeUpdate();
	}


}
