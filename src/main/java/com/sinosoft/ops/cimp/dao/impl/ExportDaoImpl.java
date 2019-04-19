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

@Repository
public class ExportDaoImpl implements ExportDao {

    @Autowired
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> findBySQL(String sql) {
        return entityManager.createNativeQuery(sql)
                .unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> findBySQL(String sql, Map<String, Object> params) {
        Query query = entityManager.createNativeQuery(sql);
        setParamemters(query, params);
        return query.unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .getResultList();
    }

    private void setParamemters(Query query, Map<String, Object> params) {
        if (null != params) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
    }

}
