/**
 * @Project:      IIMP
 * @Title:          SheetDesignExpressionDaoImpl.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.repository.sheet.impl;


import com.sinosoft.ops.cimp.common.dao.BaseEntityDaoImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignParameter;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignParameterDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName:  SheetDesignParameterDaoImpl
 * @description: 表格设计参数访问实现类
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Repository("sheetDesignParameterDao")
public class SheetDesignParameterDaoImpl  extends BaseEntityDaoImpl<SheetDesignParameter> implements SheetDesignParameterDao {

	public SheetDesignParameterDaoImpl(EntityManagerFactory factory) {
		super(factory);
	}

    @SuppressWarnings("unchecked")
    @Override
    public Collection<SheetDesignParameter> getByDesignId(UUID designId)
    {
        return sessionFactory.getCurrentSession().createQuery(" from SheetDesignParameter where designId=:designId").setParameter("designId",designId).list();
    }

	@SuppressWarnings("unchecked")
	@Override
	public SheetDesignParameter findNext(UUID id, UUID designId) {
		String hql = "FROM SheetDesignParameter T1 WHERE T1.status=0 AND T1.ordinal IS NOT NULL AND T1.id IN (SELECT MIN(T3.id) FROM SheetDesignParameter T3 " +
				"WHERE T3.status=0 AND T3.id IN (SELECT T4.id FROM SheetDesignParameter T4 WHERE T4.designId=:designId) AND T3.ordinal IS NOT NULL AND " + 
				"T3.ordinal IN (SELECT MIN(T5.ordinal) FROM SheetDesignParameter T5 WHERE T5.status=0 AND T5.id IN " + 
				"(SELECT T7.id FROM SheetDesignParameter T7 WHERE designId=:designId) AND T5.id<>:id AND T5.ordinal IS NOT NULL AND " +
				"T5.ordinal>(SELECT T6.ordinal FROM SheetDesignParameter T6 WHERE T6.id=:id)))";
		List<SheetDesignParameter> list = sessionFactory.getCurrentSession().createQuery(hql)
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
		String hql = "UPDATE SheetDesignParameter SET ordinal=:newOrdinal, lastModifiedBy=:userName, lastModifiedTime=SYSDATE WHERE id=:id";
		int cnt = sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("id", preId)
				.setParameter("userName", userName)
				.setParameter("newOrdinal", ordinal)
				.executeUpdate();
		return cnt;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SheetDesignParameter findPrevious(UUID id, UUID designId) {
		String hql = "FROM SheetDesignParameter T1 WHERE T1.status=0 AND T1.ordinal IS NOT NULL AND T1.id IN (SELECT MIN(T3.id) FROM SheetDesignParameter T3 " +
				"WHERE T3.status=0 AND T3.id IN (SELECT T4.id FROM SheetDesignParameter T4 WHERE T4.designId=:designId) AND T3.ordinal IS NOT NULL AND " + 
				"T3.ordinal IN (SELECT MAX(T5.ordinal) FROM SheetDesignParameter T5 WHERE T5.status=0 AND T5.id IN " + 
				"(SELECT T7.id FROM SheetDesignParameter T7 WHERE designId=:designId) AND T5.id<>:id AND T5.ordinal IS NOT NULL AND " +
				"T5.ordinal<(SELECT T6.ordinal FROM SheetDesignParameter T6 WHERE T6.id=:id)))";
		List<SheetDesignParameter> list = sessionFactory.getCurrentSession().createQuery(hql)
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
		sessionFactory.getCurrentSession().createQuery("delete from SheetDesignParameter where designId=:designId")
			.setParameter("designId", designId)
			.executeUpdate();
	}

}
