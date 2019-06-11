/**
 * @Project:      IIMP
 * @Title:          SheetDesignExpressionDaoImpl.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.repository.sheet.impl;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDaoImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignExpression;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignExpressionDao;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName:  SheetDesignExpressionDaoImpl
 * @description: 表格设计表达式数据访问实现类
 * @author:        Wa
 * @date:            2018年5月29日 下午6:10:44
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Repository("sheetDesignExpressionDao")
public class SheetDesignExpressionDaoImpl  extends BaseEntityDaoImpl<SheetDesignExpression> implements SheetDesignExpressionDao {

	public SheetDesignExpressionDaoImpl(EntityManagerFactory factory) {
		super(factory);
	}

    @Override
    public Collection<SheetDesignExpression> getByDesignId(UUID designId) {
        return (List<SheetDesignExpression>)sessionFactory.getCurrentSession().createQuery("from SheetDesignExpression where designId = :designId order by ordinal")
                .setParameter("designId", designId)
                .list();//多条返回list
   
	}
    
    @SuppressWarnings("unchecked")
	@Override
	public Collection<SheetDesignExpression> getCheckFormulaByDesignId(UUID designId) {
    	return sessionFactory.getCurrentSession().createQuery("from SheetDesignExpression where (type=1 or type=3) and designId = :designId order by ordinal")
                .setParameter("designId", designId)
                .list();//多条返回list
	}
    
    @SuppressWarnings("unchecked")
	@Override
	public Collection<SheetDesignExpression> getCaculationFormulaByDesignId(UUID designId) {
    	return sessionFactory.getCurrentSession().createQuery("from SheetDesignExpression where (type=0 or type=2) and designId = :designId order by ordinal")
                .setParameter("designId", designId)
                .list();//多条返回list
	}
    
    @SuppressWarnings("unchecked")
	@Override
	public Collection<SheetDesignExpression> getFillUnitFormulaByDesignId(UUID designId) {
    	return sessionFactory.getCurrentSession().createQuery("from SheetDesignExpression where type=4 and designId = :designId order by ordinal")
                .setParameter("designId", designId)
                .list();//多条返回list
	}
	
    @Override
    public int deleteByDesignId(UUID designId) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.setFlushMode(FlushMode.MANUAL);
        int result = session.createQuery("delete from SheetDesignExpression where designId=:designId")
                .setParameter("designId", designId)
                .executeUpdate();
		session.flush();
		session.getTransaction().commit();
		return result;
    }
    

	@Override
	public void updateByDesignId(SheetDesignExpression sheetDesignExpression) {
		
		update(sheetDesignExpression);
	}

	@Override
	public UUID saveDesignExpression(SheetDesignExpression sheetDesignExpression) {
		
		return (UUID)save(sheetDesignExpression);
	}

	@SuppressWarnings("unchecked")
	@Override
	public SheetDesignExpression findPrevious(UUID id, UUID designId) {
		String hql = "FROM SheetDesignExpression T1 WHERE T1.status=0 AND T1.ordinal IS NOT NULL AND T1.id IN (SELECT MIN(T3.id) FROM SheetDesignExpression T3 " +
				"WHERE T3.status=0 AND T3.id IN (SELECT T4.id FROM SheetDesignExpression T4 WHERE T4.designId=:designId) AND T3.ordinal IS NOT NULL AND " + 
				"T3.ordinal IN (SELECT MAX(T5.ordinal) FROM SheetDesignExpression T5 WHERE T5.status=0 AND T5.id IN " + 
				"(SELECT T7.id FROM SheetDesignExpression T7 WHERE designId=:designId) AND T5.id<>:id AND T5.ordinal IS NOT NULL AND " +
				"T5.ordinal<(SELECT T6.ordinal FROM SheetDesignExpression T6 WHERE T6.id=:id)))";
		List<SheetDesignExpression> list = sessionFactory.getCurrentSession().createQuery(hql)
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
	public int updateOrdinal(UUID nextId, int ordinal, UUID userName) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.setFlushMode(FlushMode.MANUAL);
		String hql = "UPDATE SheetDesignExpression SET ordinal=:newOrdinal, lastModifiedBy=:userName, lastModifiedTime=SYSDATE WHERE id=:id";
        int cnt = session.createQuery(hql)
                .setParameter("id", nextId)
                .setParameter("userName", userName)
                .setParameter("newOrdinal", ordinal)
                .executeUpdate();
		session.flush();
		session.getTransaction().commit();
		return cnt;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SheetDesignExpression findNext(UUID id, UUID designId) {
		String hql = "FROM SheetDesignExpression T1 WHERE T1.status=0 AND T1.ordinal IS NOT NULL AND T1.id IN (SELECT MIN(T3.id) FROM SheetDesignExpression T3 " +
				"WHERE T3.status=0 AND T3.id IN (SELECT T4.id FROM SheetDesignExpression T4 WHERE T4.designId=:designId) AND T3.ordinal IS NOT NULL AND " + 
				"T3.ordinal IN (SELECT MIN(T5.ordinal) FROM SheetDesignExpression T5 WHERE T5.status=0 AND T5.id IN " + 
				"(SELECT T7.id FROM SheetDesignExpression T7 WHERE designId=:designId) AND T5.id<>:id AND T5.ordinal IS NOT NULL AND " +
				"T5.ordinal>(SELECT T6.ordinal FROM SheetDesignExpression T6 WHERE T6.id=:id)))";
		List<SheetDesignExpression> list = sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("id", id)
                .setParameter("designId", designId)
                .list();
		if (list.size() > 0) {
			return list.get(list.size() - 1);
		} else {
			return null;
		}
	}


}
