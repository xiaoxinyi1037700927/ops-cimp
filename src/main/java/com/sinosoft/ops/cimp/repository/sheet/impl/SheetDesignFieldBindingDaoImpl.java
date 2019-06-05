package com.sinosoft.ops.cimp.repository.sheet.impl;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDaoImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignFieldBinding;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignFieldBindingDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.Collection;
import java.util.UUID;



@Repository
public class SheetDesignFieldBindingDaoImpl extends BaseEntityDaoImpl<SheetDesignFieldBinding> implements SheetDesignFieldBindingDao {

	public SheetDesignFieldBindingDaoImpl(EntityManagerFactory factory) {
		super(factory);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<SheetDesignFieldBinding> getByFieldId(UUID fieldId) {
		return sessionFactory.getCurrentSession().createQuery("from SheetDesignFieldBinding where fieldId=:fieldId")
				.setParameter("fieldId", fieldId)
				.list();
		
	}

	@Override
	public void deleteByDesignId(UUID designId) {
		sessionFactory.getCurrentSession().createQuery("delete from SheetDesignFieldBinding where designId=:designId")
			.setParameter("designId", designId)
			.executeUpdate();
	}

}
