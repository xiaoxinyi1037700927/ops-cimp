/**
 * @project: IIMP
 * @title: BaseEntityDaoImpl.java
 * @copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.common.dao;

import com.sinosoft.ops.cimp.common.model.PageableQueryParameter;
import com.sinosoft.ops.cimp.common.model.PageableQueryResult;
import org.hibernate.LockOptions;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Example.NotNullOrZeroPropertySelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;


/**
 * @version 1.0.0
 * @ClassName: BaseEntityDaoImpl
 * @description: 实体数据访问基类
 * @author: Nil
 * @date: 2017年9月25日 下午2:11:54
 */
@Repository("baseEntityDao")
public class BaseEntityDaoImpl<T extends Serializable> extends BaseDaoImpl implements BaseEntityDao<T> {
    private static final Logger logger = LoggerFactory.getLogger(BaseEntityDaoImpl.class);

    /*** 泛型实体类 */
    protected Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public BaseEntityDaoImpl() {
        String typeName = this.getClass().getGenericSuperclass().toString();
        if (typeName.endsWith(">")) {
            try {
                this.entityClass = (Class<T>) Class.forName(typeName.split("[<>]")[1]);
            } catch (ClassNotFoundException e) {
                logger.error("解析实体对象类名失败！" + typeName, e);
            }
        }
        //ParameterizedType pt=(ParameterizedType)this.getClass().getGenericSuperclass();
        //this.clazz=(Class<T>)pt.getActualTypeArguments()[0];
    }

    @Override
    public Serializable save(T entity) {
        return sessionFactory.getCurrentSession().save(entity);
    }

    @Override
    public void saveOrUpdate(T entity) {
        sessionFactory.getCurrentSession().saveOrUpdate(entity);
    }

    @Override
    public void update(T entity) {
        sessionFactory.getCurrentSession().update(entity);
    }

    @Override
    public void delete(T entity) {
        sessionFactory.getCurrentSession().delete(entity);
    }

    @Override
    public void deleteById(Serializable id) {
        sessionFactory.getCurrentSession().delete(this.getById(id));
    }

    @Override
    public T getById(Serializable id) {
        return getById(id, false);
    }

    @Override
    public T getById(Serializable id, boolean lock) {
        if (lock) {
            return (T) sessionFactory.getCurrentSession().get(entityClass, id, LockOptions.UPGRADE);
        } else {
            return (T) sessionFactory.getCurrentSession().get(entityClass, id);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public T merge(T entity) {
        return (T) sessionFactory.getCurrentSession().merge(entity);
    }

    @Override
    public Collection<T> findAll() {
        CriteriaQuery<T> criteria = sessionFactory.getCriteriaBuilder().createQuery(entityClass);
        Root<T> root = criteria.from(entityClass);
        criteria.select(root);
        return sessionFactory.getCurrentSession().createQuery(criteria).getResultList();
    }

    @Deprecated
    @Override
    @SuppressWarnings("unchecked")
    public Collection<T> findByExample(T entity) {
        return sessionFactory.getCurrentSession()
                .createCriteria(entity.getClass())
                .add(Example.create(entity))
                .list();
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    public Collection<T> findByNotNullOrZeroExample(T entity) {
        return sessionFactory.getCurrentSession().createCriteria(entity.getClass())
                .add(Example.create(entity).setPropertySelector(NotNullOrZeroPropertySelector.INSTANCE))
                .list();
    }

    /**
     * 根据页号和每页数据行数计算起始记录
     *
     * @param pageNo   页号
     * @param pageSize 每页数据行数
     * @return 起始记录
     */
    protected int calculateFirstResult(final int pageNo, final int pageSize) {
        return (pageNo - 1) * pageSize;
    }

    /**
     * 根据泛型实体类生成HQL
     *
     * @param clazz  实体类
     * @param params 参数集
     * @return HQL
     */
    protected String buildHql(final Class<?> clazz, final Map<String, Object> params) {
        StringBuilder hqlBuilder = new StringBuilder("from ").append(clazz.getSimpleName());
        if (params != null && !params.isEmpty()) {
            hqlBuilder.append(" where ( 1 = 1 )  ");
            for (Map.Entry<String, ?> entry : params.entrySet()) {
                hqlBuilder.append(" and ");
                hqlBuilder.append(entry.getKey());
                hqlBuilder.append(" = :");
                hqlBuilder.append(entry.getKey());
            }
        }
        return hqlBuilder.toString();
    }

    @Override
    public PageableQueryResult findByPage(int pageNo, int pageSize, Map<String, Object> parameters) {
        PageableQueryResult result = new PageableQueryResult(pageNo, pageSize);
        String hql = buildHql(this.entityClass, parameters);
        String totalCountHql = new StringBuilder("select count(*) ").append(hql).toString();
        result.setTotalCount(this.getTotalCountWithHql(totalCountHql, parameters));
        result.setData((Collection<?>) this.findByPageWithHql(calculateFirstResult(pageNo, pageSize), pageSize, hql, parameters));
        return result;
    }

    @Override
    public Integer getNextOrdinal() {
        String hql = "select coalesce(max(ordinal),0)+1 from " + this.entityClass.getSimpleName();
        return (Integer) sessionFactory.getCurrentSession().createQuery(hql).uniqueResult();
    }

    @Override
    public PageableQueryResult findByPage(PageableQueryParameter queryParameter) {
        PageableQueryResult result = new PageableQueryResult(queryParameter.getPageNo(), queryParameter.getPageSize());
        String hql = buildHql(this.entityClass, queryParameter.getParameters());
        if (queryParameter.getTotalCount() == -1) {
            String totalCountHql = new StringBuilder("select count(*) ").append(hql).toString();
            result.setTotalCount(this.getTotalCountWithHql(totalCountHql, queryParameter.getParameters()));
        }
        result.setData((Collection<?>) this.findByPageWithHql(calculateFirstResult(queryParameter.getPageNo(), queryParameter.getPageSize()), queryParameter.getPageSize(), hql, queryParameter.getParameters()));
        return result;
    }

    @Override
    public String toUUIDStringByRaw(Object obj) {
        if (!(obj instanceof byte[])) {
            return "";
        }
        byte[] bytes = (byte[]) obj;
        //        System.out.println("bytes.length=" + bytes.length);//16
        StringBuffer raw = new StringBuffer();
        String hex = "";
        for (int i = 0; i < bytes.length; i++) {
            hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            raw.append(hex.toLowerCase());
            //ac4b59cd-1fa2-4416-aa0b-4b6a629afca7
            if (i == 3 || i == 5 || i == 7 || i == 9) {
                raw.append("-");
            }
        }
        return raw.toString();
    }
}
