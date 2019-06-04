package com.sinosoft.ops.cimp.dao.sheet.impl;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDaoImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetDataSourceCategory;
import com.sinosoft.ops.cimp.dao.sheet.SheetDataSourceCategoryDao;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository("sheetDataSourceCategory")
public class SheetDataSourceCategoryDaoImpl extends BaseEntityDaoImpl<SheetDataSourceCategory> implements SheetDataSourceCategoryDao {

	@Override
	public Collection<SheetDataSourceCategory> findAllChildren() {
		// TODO Auto-generated method stub
		return sessionFactory.getCurrentSession().createQuery(" from SheetDataSourceCategory").list();
	}

	@Override
	public Collection<SheetDataSourceCategory> findByParentId(Integer parentid) {
		return sessionFactory.getCurrentSession().createQuery(" from SheetDataSourceCategory where parentId =:parentid  order by ordinal").setParameter("parentid",parentid).list();
	}

	@Override
	public Collection<SheetDataSourceCategory> getRootData() {
		return sessionFactory.getCurrentSession().createQuery(" from SheetDataSourceCategory where parentId is null order by ordinal").list();
	}

	@Override
	public String getMaxId() {
		return sessionFactory.getCurrentSession().createSQLQuery("select nvl(max(id),0)+1 from Sheet_Data_Source_Category").uniqueResult().toString();
	}

	@Override
	public Integer getFisrtId() {
		return (Integer)sessionFactory.getCurrentSession().createQuery("select id from SheetDataSourceCategory order by ordinal").setMaxResults(1).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public SheetDataSourceCategory findPrevious(Integer id) {
		String hql = "FROM SheetDataSourceCategory T1 WHERE T1.status=0 AND T1.ordinal IS NOT NULL AND T1.id IN (SELECT MIN(T3.id) FROM SheetDataSourceCategory T3 " +
				"WHERE T3.status=0 AND ((T3.parentId IN (SELECT T4.parentId FROM SheetDataSourceCategory T4 WHERE T4.id=:id)) " + 
				"OR (T3.parentId IS NULL AND EXISTS (SELECT 1 FROM SheetDataSourceCategory T9 WHERE T9.id=:id AND T9.parentId IS NULL))) " +
				"AND T3.ordinal IS NOT NULL AND " + 
				"T3.ordinal IN (SELECT MAX(T5.ordinal) FROM SheetDataSourceCategory T5 WHERE T5.status=0 AND ((T5.parentId IN " + 
				"(SELECT T7.parentId FROM SheetDataSourceCategory T7 WHERE id=:id)) " + 
				"OR (T5.parentId IS NULL AND EXISTS (SELECT 1 FROM SheetDataSourceCategory T8 WHERE T8.id=:id AND T8.parentId IS NULL))) " +
				"AND T5.id<>:id AND T5.ordinal IS NOT NULL AND " +
				"T5.ordinal<(SELECT T6.ordinal FROM SheetDataSourceCategory T6 WHERE T6.id=:id)))";
		List<SheetDataSourceCategory> list = sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("id", id)
                .list();
		if (list.size() > 0) {
			return list.get(list.size() - 1);
		} else {
			return null;
		}
	}

	@Override
	public int updateOrdinal(Integer preId, int ordinal, UUID userName) {
		String hql = "UPDATE SheetDataSourceCategory SET ordinal=:newOrdinal, lastModifiedBy=:userName, lastModifiedTime=SYSDATE WHERE id=:id";
        int cnt = sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("id", preId)
                .setParameter("userName", userName)
                .setParameter("newOrdinal", ordinal)
                .executeUpdate();
		return cnt;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SheetDataSourceCategory findNext(Integer id) {
		String hql = "FROM SheetDataSourceCategory T1 WHERE T1.status=0 AND T1.ordinal IS NOT NULL AND T1.id IN (SELECT MIN(T3.id) FROM SheetDataSourceCategory T3 " +
				"WHERE T3.status=0 AND ((T3.parentId IN (SELECT T4.parentId FROM SheetDataSourceCategory T4 WHERE T4.id=:id)) " + 
				"OR (T3.parentId IS NULL AND EXISTS (SELECT 1 FROM SheetDataSourceCategory T9 WHERE T9.id=:id AND T9.parentId IS NULL))) " +
				"AND T3.ordinal IS NOT NULL AND " + 
				"T3.ordinal IN (SELECT MIN(T5.ordinal) FROM SheetDataSourceCategory T5 WHERE T5.status=0 AND ((T5.parentId IN " + 
				"(SELECT T7.parentId FROM SheetDataSourceCategory T7 WHERE id=:id)) " + 
				"OR (T5.parentId IS NULL AND EXISTS (SELECT 1 FROM SheetDataSourceCategory T8 WHERE T8.id=:id AND T8.parentId IS NULL))) " +
				"AND T5.id<>:id AND T5.ordinal IS NOT NULL AND " +
				"T5.ordinal>(SELECT T6.ordinal FROM SheetDataSourceCategory T6 WHERE T6.id=:id)))";
		List<SheetDataSourceCategory> list = sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("id", id)
                .list();
		if (list.size() > 0) {
			return list.get(list.size() - 1);
		} else {
			return null;
		}
	}
}
