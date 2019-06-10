package com.sinosoft.ops.cimp.repository.sheet.impl;

import com.sinosoft.ops.cimp.common.dao.BaseDaoImpl;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignBindDao;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Map;

@Repository("SheetDesignBindDao")
public class SheetDesignBindDaoImpl extends BaseDaoImpl implements SheetDesignBindDao {

	public SheetDesignBindDaoImpl(EntityManagerFactory factory) {
		super(factory);
	}

    @SuppressWarnings({ "deprecation" })
    @Override
	public int updateDesignBind(String sql) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.setFlushMode(FlushMode.MANUAL);
		int result = session.createSQLQuery(sql).executeUpdate();
		session.flush();
		session.getTransaction().commit();
        return result;
	}

	@Override
	public boolean hasDatas(String sql) {
		List list = sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		return list != null && list.size() > 0 ? true : false;
	}
	
	@Override
	public int dataCnt(String sql) {
		List list = sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		return list.size();
	}
	
	@Override
	@Transactional
	public List<Map<String, Object>> findBySQL(String sql) {
    	return sessionFactory.getCurrentSession().createSQLQuery(sql).setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).list();
	}
}
