/**
 * @Project:      IIMP
 * @Title:          SheetDesignCategoryDaoImpl.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.repository.sheet.impl;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDaoImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignCategory;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignDesignCategory;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignCategoryDao;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName:  SheetDesignCategoryDaoImpl
 * @description: 表格分类数据访问实现类
 * @author:        Ni
 * @date:            2018年5月25日 下午4:07:17
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Repository("sheetDesignCategoryDao")
public class SheetDesignCategoryDaoImpl extends BaseEntityDaoImpl<SheetDesignCategory> implements SheetDesignCategoryDao {

	public SheetDesignCategoryDaoImpl(EntityManagerFactory factory) {
		super(factory);
	}

	@SuppressWarnings("unchecked")
	public Collection<SheetDesignCategory> getChildren(UUID id) {
		Collection<SheetDesignCategory> list = null;
		if (id != null) {
			list = sessionFactory.getCurrentSession().createQuery("from SheetDesignCategory where status=0 and parentId=:id")
                .setParameter("id", id)
                .list();
		} else {
			list = sessionFactory.getCurrentSession().createQuery("from SheetDesignCategory where status=0 and parentId is null")
	                .list();
		}
		if (list == null) {
			list = new ArrayList<SheetDesignCategory>();
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public Collection<SheetDesignCategory> getRoot() {
        return sessionFactory.getCurrentSession().createQuery("from SheetDesignCategory where status=0 and parentId is null")
                .list();
	}

    @SuppressWarnings("unchecked")
    @Override
    public Collection<SheetDesignCategory> getByType(Byte type) {
        return sessionFactory.getCurrentSession().createQuery("from SheetDesignCategory where type =:type order by ordinal")
                .setParameter("type", type)
                .list();
    }

	@Override
	public UUID save(SheetDesignCategory sheetDesignCategory) {
		return (UUID)super.save(sheetDesignCategory);
	}

	@Override
	public boolean updateName(UUID id, String name, UUID userName) {
		String hql = "UPDATE SheetDesignCategory SET name=:name, lastModifiedBy=:userName, lastModifiedTime=SYSDATE WHERE id=:id";
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.setFlushMode(FlushMode.MANUAL);
        int cnt = session.createQuery(hql)
                .setParameter("id", id)
                .setParameter("name", name)
                .setParameter("userName", userName)
                .executeUpdate();
		session.flush();
		session.getTransaction().commit();
		return cnt > 0 ? true : false;
	}

	@Override
	public boolean addOrdinals(int upOrdinal, UUID parentId, UUID userName) {
		String parentCon = "parentId=:parentId ";
		if (parentId == null) {
			parentCon = "(parentId IS NULL OR parentId='') ";
		}
		String hql = "UPDATE SheetDesignCategory SET ordinal=ordinal+1, lastModifiedBy=:userName, lastModifiedTime=SYSDATE WHERE " + 
				parentCon + "AND ordinal IS NOT NULL AND ordinal>:upOrdinal";
		int cnt = -1;

		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.setFlushMode(FlushMode.MANUAL);
		if (parentId == null) {
			cnt = session.createQuery(hql)
	                .setParameter("upOrdinal", upOrdinal)
	                .setParameter("userName", userName)
	                .executeUpdate();
		} else {
			cnt = session.createQuery(hql)
                .setParameter("parentId", parentId)
                .setParameter("upOrdinal", upOrdinal)
                .setParameter("userName", userName)
                .executeUpdate();
		}
		session.flush();
		session.getTransaction().commit();
		return cnt > 0 ? true : false;
	}

    @SuppressWarnings("unchecked")
	@Override
	public SheetDesignCategory findPrevious(UUID id) {
		String hql = "FROM SheetDesignCategory T1 WHERE T1.status=0 AND T1.ordinal IS NOT NULL AND T1.id IN (SELECT MIN(T3.id) FROM SheetDesignCategory T3 " +
				"WHERE T3.status=0 AND ((T3.parentId IN (SELECT T4.parentId FROM SheetDesignCategory T4 WHERE T4.id=:id)) " + 
				"OR (T3.parentId IS NULL AND EXISTS (SELECT 1 FROM SheetDesignCategory T9 WHERE T9.id=:id AND T9.parentId IS NULL))) " +
				"AND T3.ordinal IS NOT NULL AND " + 
				"T3.ordinal IN (SELECT MAX(T5.ordinal) FROM SheetDesignCategory T5 WHERE T5.status=0 AND ((T5.parentId IN " + 
				"(SELECT T7.parentId FROM SheetDesignCategory T7 WHERE id=:id)) " + 
				"OR (T5.parentId IS NULL AND EXISTS (SELECT 1 FROM SheetDesignCategory T8 WHERE T8.id=:id AND T8.parentId IS NULL))) " +
				"AND T5.id<>:id AND T5.ordinal IS NOT NULL AND " +
				"T5.ordinal<(SELECT T6.ordinal FROM SheetDesignCategory T6 WHERE T6.id=:id)))";
		List<SheetDesignCategory> list = sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("id", id)
                .list();
		if (list.size() > 0) {
			return list.get(list.size() - 1);
		} else {
			return null;
		}
	}

	@Override
	public int updateOrdinal(UUID id, int newOrdinal, UUID userName) {
		String hql = "UPDATE SheetDesignCategory SET ordinal=:newOrdinal, lastModifiedBy=:userName, lastModifiedTime=SYSDATE WHERE id=:id";
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.setFlushMode(FlushMode.MANUAL);
		int cnt = session.createQuery(hql)
                .setParameter("id", id)
                .setParameter("userName", userName)
                .setParameter("newOrdinal", newOrdinal)
                .executeUpdate();
		session.flush();
		session.getTransaction().commit();
		return cnt;
	}

    @SuppressWarnings("unchecked")
	@Override
	public SheetDesignCategory findNext(UUID id) {
		String hql = "FROM SheetDesignCategory T1 WHERE T1.status=0 AND T1.ordinal IS NOT NULL AND T1.id IN (SELECT MIN(T3.id) FROM SheetDesignCategory T3 " +
				"WHERE T3.status=0 AND ((T3.parentId IN (SELECT T4.parentId FROM SheetDesignCategory T4 WHERE T4.id=:id)) " + 
				"OR (T3.parentId IS NULL AND EXISTS (SELECT 1 FROM SheetDesignCategory T9 WHERE T9.id=:id AND T9.parentId IS NULL))) " +
				"AND T3.ordinal IS NOT NULL AND " + 
				"T3.ordinal IN (SELECT MIN(T5.ordinal) FROM SheetDesignCategory T5 WHERE T5.status=0 AND ((T5.parentId IN " + 
				"(SELECT T7.parentId FROM SheetDesignCategory T7 WHERE id=:id)) " + 
				"OR (T5.parentId IS NULL AND EXISTS (SELECT 1 FROM SheetDesignCategory T8 WHERE T8.id=:id AND T8.parentId IS NULL))) " +
				"AND T5.id<>:id AND T5.ordinal IS NOT NULL AND " +
				"T5.ordinal>(SELECT T6.ordinal FROM SheetDesignCategory T6 WHERE T6.id=:id)))";
		List<SheetDesignCategory> list = sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("id", id)
                .list();
		if (list.size() > 0) {
			return list.get(list.size() - 1);
		} else {
			return null;
		}
	}

    @SuppressWarnings("unchecked")
	@Override
	public Collection<SheetDesignDesignCategory> findDesignByCategory(UUID categoryId) {
		String hql = "FROM SheetDesignDesignCategory WHERE categoryId=:categoryId AND status=0";
		Collection<SheetDesignDesignCategory> list = sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("categoryId", categoryId)
                .list();
		return list;
	}

    @SuppressWarnings("unchecked")
	@Override
	public int minusOrdinals(UUID id, UUID parentId, UUID userName) {
		String parentCon = "T1.parentId=:parentId ";
		if (parentId == null) {
			parentCon = "(T1.parentId IS NULL OR T1.parentId='') ";
		}
		String hql = "UPDATE SheetDesignCategory T1 SET T1.ordinal=T1.ordinal-1, T1.lastModifiedBy=:userName, T1.lastModifiedTime=SYSDATE " +
				"WHERE T1.ordinal IS NOT NULL AND " + parentCon +
				"AND T1.ordinal>(SELECT T3.ordinal FROM SheetDesignCategory T3 WHERE T3.id=:id)";
		int cnt = -1;
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.setFlushMode(FlushMode.MANUAL);
		if (parentId == null) {
        	cnt = session.createQuery(hql)
                .setParameter("id", id)
                .setParameter("userName", userName)
                .executeUpdate();
		} else {
        	cnt = session.createQuery(hql)
                    .setParameter("id", id)
                    .setParameter("userName", userName)
                    .setParameter("parentId", parentId)
                    .executeUpdate();
		}
		session.flush();
		session.getTransaction().commit();
		return cnt;
	}

    @SuppressWarnings("unchecked")
	@Override
	public Collection<SheetDesignCategory> findAllByOrdinal() {
		String hql = "from SheetDesignCategory where status=0 order by ordinal";
		Collection<SheetDesignCategory> list = sessionFactory.getCurrentSession().createQuery(hql)
                .list();
		return list;
	}
}
