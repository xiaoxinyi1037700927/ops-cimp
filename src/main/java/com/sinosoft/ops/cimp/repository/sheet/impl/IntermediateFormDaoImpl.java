package com.sinosoft.ops.cimp.repository.sheet.impl;


import com.sinosoft.ops.cimp.common.dao.BaseDaoImpl;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoItem;
import com.sinosoft.ops.cimp.repository.sheet.IntermediateFormDao;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.Collection;
import java.util.Map;

@Repository
public class IntermediateFormDaoImpl extends BaseDaoImpl implements IntermediateFormDao {

	public IntermediateFormDaoImpl(EntityManagerFactory factory) {
		super(factory);
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	@Override
	public Collection<?> queryListBySql(String sql, Object... args) {
		Query query = sessionFactory.getCurrentSession().createNativeQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		int position = 0;
		for (Object object : args) {
			position++;
			query.setParameter(position, object);
			
		}
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<SysInfoItem> getItemByName(String name) {
		return sessionFactory.getCurrentSession().createQuery("from SysInfoItem where name=:name")
				.setParameter("name", name)
				.list();
	}

	@Override
	public Map<String, Object> getIdByIdKey(String idKey) {
		return (Map<String, Object>) sessionFactory.getCurrentSession().createNativeQuery("select id_value from sys_id_generator where id_key=? for update")
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
				.setParameter("idKey", idKey)
				.uniqueResult();
	}

}
