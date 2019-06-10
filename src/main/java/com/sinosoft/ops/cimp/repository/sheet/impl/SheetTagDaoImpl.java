package com.sinosoft.ops.cimp.repository.sheet.impl;


import com.sinosoft.ops.cimp.common.dao.BaseEntityDaoImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetTag;
import com.sinosoft.ops.cimp.repository.sheet.SheetTagDao;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.Collection;
import java.util.UUID;

@Repository
public class SheetTagDaoImpl extends BaseEntityDaoImpl<SheetTag> implements SheetTagDao {

	public SheetTagDaoImpl(EntityManagerFactory factory) {
		super(factory);
	}

	@Override
	public Integer deleteBySheetId(UUID sheetId) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.setFlushMode(FlushMode.MANUAL);
		int result = session.createQuery("delete from SheetTag where sheetId=:sheetId")
			.setParameter("sheetId", sheetId)
			.executeUpdate();
		session.flush();
		session.getTransaction().commit();
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<SheetTag> getBySheetId(UUID sheetId) {
		return sessionFactory.getCurrentSession().createQuery("from SheetTag where sheetId=:sheetId")
				.setParameter("sheetId", sheetId)
				.list();
	}
	
	

}
