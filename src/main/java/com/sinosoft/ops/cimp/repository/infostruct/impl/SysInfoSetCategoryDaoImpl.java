package com.sinosoft.ops.cimp.repository.infostruct.impl;


import com.sinosoft.ops.cimp.common.dao.BaseEntityDaoImpl;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoSetCategory;
import com.sinosoft.ops.cimp.repository.infostruct.SysInfoSetCategoryDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.Collection;

@Repository("sysInfoSetCategoryDao")
public class SysInfoSetCategoryDaoImpl extends BaseEntityDaoImpl<SysInfoSetCategory> implements SysInfoSetCategoryDao {

	public SysInfoSetCategoryDaoImpl(EntityManagerFactory factory) {
		super(factory);
	}

	@SuppressWarnings("unchecked")
    @Override
	public Collection<SysInfoSetCategory> getAll() {
		return sessionFactory.getCurrentSession().createQuery("from SysInfoSetCategory where status=0 order by id").list();
	}

}
