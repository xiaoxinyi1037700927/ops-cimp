/**
 * @Project:      IIMP
 * @Title:          SheetDesignExpressionDaoImpl.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.repository.sheet.impl;


import com.sinosoft.ops.cimp.common.dao.BaseEntityDaoImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetParameter;
import com.sinosoft.ops.cimp.repository.sheet.SheetParameterDao;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.Collection;
import java.util.UUID;

/**
 * @ClassName:  SheetDesignParameterDaoImpl
 * @description: 表格设计参数访问实现类
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Repository("sheetParameterDao")
public class SheetParameterDaoImpl extends BaseEntityDaoImpl<SheetParameter> implements SheetParameterDao {

    public SheetParameterDaoImpl(EntityManagerFactory factory) {
        super(factory);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<SheetParameter> getBySheetId(UUID sheetId)
    {
        return sessionFactory.getCurrentSession().createQuery(" from SheetParameter where sheetId=:sheetId").setParameter("sheetId",sheetId).list();
    }

	@Override
	public void deleteBySheetId(UUID sheetId) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.setFlushMode(FlushMode.MANUAL);
		session.createQuery(" delete from SheetParameter where sheetId=:sheetId")
		.setParameter("sheetId", sheetId)
		.executeUpdate();
        session.flush();
        session.getTransaction().commit();
	}

}
