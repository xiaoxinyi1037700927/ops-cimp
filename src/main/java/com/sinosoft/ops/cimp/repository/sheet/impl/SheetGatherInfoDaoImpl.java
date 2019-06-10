package com.sinosoft.ops.cimp.repository.sheet.impl;


import com.sinosoft.ops.cimp.common.dao.BaseEntityDaoImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetGatherInfo;
import com.sinosoft.ops.cimp.repository.sheet.SheetGatherInfoDao;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.UUID;

@Repository
public class SheetGatherInfoDaoImpl extends BaseEntityDaoImpl<SheetGatherInfo> implements SheetGatherInfoDao {

	public SheetGatherInfoDaoImpl(EntityManagerFactory factory) {
		super(factory);
	}

	@Override
	public void deleteBySheetId(UUID sheetId) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.setFlushMode(FlushMode.MANUAL);
		session.createQuery("delete from SheetGatherInfo where sheetId=:sheetId").
			setParameter("sheetId", sheetId).executeUpdate();
		session.flush();
		session.getTransaction().commit();
	}

	@Override
	public List<UUID> getGatherIdsBySheetId(UUID sheetId) {
		List list = sessionFactory.getCurrentSession().createQuery("select gatherSheetId from SheetGatherInfo where sheetId=:sheetId")
			.setParameter("sheetId", sheetId).list();
		return list;
	}

}
