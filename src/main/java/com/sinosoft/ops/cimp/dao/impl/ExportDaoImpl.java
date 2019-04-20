package com.sinosoft.ops.cimp.dao.impl;

import com.sinosoft.ops.cimp.dao.ExportDao;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.object.SqlQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
@Repository
public class ExportDaoImpl implements ExportDao {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Map<String, Object>> findBySQL(String sql) {
        return entityManager.createNativeQuery(sql)
                .unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .getResultList();
    }

    @Override
    public List<Map<String, Object>> findBySQL(String sql, Object... args) {
        Query query = entityManager.createNativeQuery(sql)
                .unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        if (null != args) {
            for (int i = 0; i < args.length; ++i) {
                query.setParameter(i, args[i]);
            }
        }

        return query.getResultList();
    }

}
