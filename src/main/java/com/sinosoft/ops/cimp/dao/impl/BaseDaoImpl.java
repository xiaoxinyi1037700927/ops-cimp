/**
 * @project: IIMP
 * @title: BaseDaoImpl.java
 * @copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.dao.impl;

import com.sinosoft.ops.cimp.dao.BaseDao;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.SessionFactory;
import org.hibernate.type.descriptor.java.UUIDTypeDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @classname: BaseDaoImpl
 * @description: 数据访问基实现类
 * @author: Nil
 * @date: 2017年9月25日 下午1:45:25
 * @version 1.0.0
 */
@Repository("baseDao")
public class BaseDaoImpl implements BaseDao {
    private static final Logger logger = LoggerFactory.getLogger(BaseDaoImpl.class);

    @Autowired
    @Qualifier("sessionFactory")
    protected SessionFactory sessionFactory = null;

    /**
     * 将查询结果集封装成别名和值的映射表集合
     * @param results 查询结果
     * @param aliases 别名数组
     * @return 封装成别名和值的映射表集合
     */
    protected Collection<Map<String, Object>> transformQueryResult(ScrollableResults results, String[] aliases) {
        Collection<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
        while (results.next()) {
            Map<String, Object> map = new HashMap<String, Object>(aliases.length);
            for (int i = 0; i < aliases.length; i++) {
                Object o = results.get(i);
                if (o instanceof byte[]) {
                    byte[] ba = (byte[]) o;
                    if (ba.length == 16) {//UUID，使用hibernate的UUID解析器处理
                        try {
                            map.put(aliases[i], UUIDTypeDescriptor.ToBytesTransformer.INSTANCE.parse(o).toString().toUpperCase());
                        } catch (Exception e) {
                            map.put(aliases[i], null);
                            logger.error("UUID解析失败！", e);
                        }
                    } else {
                        map.put(aliases[i], (byte[]) o);
                    }
                } else {
                    map.put(aliases[i], o);
                }
            }
            list.add(map);
        }
        return list;
    }

    @Override
    public Collection<?> findByPageWithHql(int firstResult, int maxResults, String hql, Map<String, Object> params) {
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        if (params != null && !params.isEmpty()) {
            prepareQueryParam(query, params);
        }
        return query.setFirstResult(firstResult)
                .setMaxResults(maxResults)
                .list();
    }

    @Override
    public Collection<?> findByPageWithHql(int firstResult, int maxResults, String hql, Object... objects) {
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        if (objects != null && objects.length > 0) {
            prepareQueryParam(query, objects);
        }
        return query.setFirstResult(firstResult)
                .setMaxResults(maxResults)
                .list();
    }

    @Override
    public Collection<?> findByPageWithSql(int firstResult, int maxResults, String sql, Map<String, Object> params) {
        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
        if (params != null && !params.isEmpty()) {
            prepareQueryParam(query, params);
        }
        return query.setFirstResult(firstResult)
                .setMaxResults(maxResults)
                .list();
    }

    @Override
    public Collection<?> findByPageWithSql(int firstResult, int maxResults, String sql, Object... objects) {
        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
        if (objects != null && objects.length > 0) {
            prepareQueryParam(query, objects);
        }
        return query.setFirstResult(firstResult)
                .setMaxResults(maxResults)
                .list();
    }

    @Override
    public long getTotalCountWithHql(String totalCountHql, Map<String, Object> params) {
        Query query = sessionFactory.getCurrentSession().createQuery(totalCountHql);
        if (params != null && !params.isEmpty()) {
            prepareQueryParam(query, params);
        }
        return (long) query.uniqueResult();
    }

    @Override
    public long getTotalCountWithHql(String totalCountHql, Object... objects) {
        Query query = sessionFactory.getCurrentSession().createQuery(totalCountHql);
        if (objects != null && objects.length > 0) {
            prepareQueryParam(query, objects);
        }
        return (long) query.uniqueResult();
    }

    @Override
    public long getTotalCountWithSql(final String totalCountSql, final Map<String, Object> params) {
        Query query = sessionFactory.getCurrentSession().createSQLQuery(totalCountSql);
        if (params != null && !params.isEmpty()) {
            prepareQueryParam(query, params);
        }
        return (long) query.uniqueResult();
    }

    @Override
    public long getTotalCountWithSql(final String totalCountSql, final Object... objects) {
        Query query = sessionFactory.getCurrentSession().createSQLQuery(totalCountSql);
        if (objects != null && objects.length > 0) {
            prepareQueryParam(query, objects);
        }
        return (long) query.uniqueResult();
    }

    @Override
    public int executeUpdateHql(String hql, final Map<String, Object> params) {
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        if (params != null && !params.isEmpty()) {
            prepareQueryParam(query, params);
        }
        return query.executeUpdate();
    }

    @Override
    public int executeUpdateHql(String hql, Object... objects) {
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        if (objects != null && objects.length > 0) {
            prepareQueryParam(query, objects);
        }
        return query.executeUpdate();
    }

    @Override
    public int executeUpdateSql(String sql, Map<String, Object> params) {
        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
        if (params != null && !params.isEmpty()) {
            prepareQueryParam(query, params);
        }
        return query.executeUpdate();
    }

    @Override
    public int executeUpdateSql(String sql, final Object... objects) {
        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
        if (objects != null && objects.length > 0) {
            prepareQueryParam(query, objects);
        }
        return query.executeUpdate();
    }

    /**
     * 为查询对象准备查询参数
     * @param query 查询对象
     * @param objects 参数数组
     */
    protected void prepareQueryParam(Query query, final Object... objects) {
        for (int i = 0; i < objects.length; i++) {
            query.setParameter(i, objects[i]);
        }
    }

    /**
     * 为查询对象准备查询参数
     * @param query 查询对象
     * @param params 参数集
     */
    protected void prepareQueryParam(Query query, final Map<String, Object> params) {
        for (String key : params.keySet()) {
            Object value = params.get(key);
            if (params.get(key) instanceof Collection<?>) {
                query.setParameterList(key, (Collection<?>) params.get(key));
            } else if (value instanceof Object[]) {
                query.setParameterList(key, (Object[]) value);
            } else {
                query.setParameter(key, value);
            }
        }
    }
}
