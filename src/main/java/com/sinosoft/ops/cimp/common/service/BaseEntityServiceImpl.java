/**
 * @project: IIMP
 * @title: BaseEntityServiceImpl.java
 * @copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.common.service;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDao;
import com.sinosoft.ops.cimp.common.model.PageableQueryParameter;
import com.sinosoft.ops.cimp.common.model.PageableQueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * @version 1.0.0
 * @ClassName: BaseEntityServiceImpl
 * @description: 实体服务基实现类
 * @author: Nil
 * @date: 2017年8月1日 上午10:35:39
 */
@Service("baseEntityService")
public class BaseEntityServiceImpl<T extends Serializable> extends BaseServiceImpl implements BaseEntityService<T> {
    private static final Logger logger = LoggerFactory.getLogger(BaseEntityServiceImpl.class);
    /*** 泛型实体类 */
    protected Class<T> entityClass;

    @Autowired
    private BaseEntityDao<T> baseEntityDao;

    @SuppressWarnings("unchecked")
    public BaseEntityServiceImpl() {
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
    @Transactional
    public Serializable create(T entity) {
        return baseEntityDao.save(entity);
    }

    @Override
    @Transactional
    public void createAll(Collection<T> entities) {
        for (T entity : entities) {
            baseEntityDao.save(entity);
        }
    }

    @Override
    @Transactional
    public void createOrUpdate(T entity) {
        baseEntityDao.saveOrUpdate(entity);
    }

    @Override
    @Transactional
    public void createOrUpdateAll(Collection<T> entities) {
        for (T entity : entities) {
            baseEntityDao.saveOrUpdate(entity);
        }
    }

    @Override
    @Transactional
    public void update(T entity) {
        baseEntityDao.update(entity);
    }

    @Override
    @Transactional
    public void updateAll(Collection<T> entities) {
        for (T entity : entities) {
            baseEntityDao.update(entity);
        }
    }

    @Override
    @Transactional
    public T merge(T entity) {
        return baseEntityDao.merge(entity);
    }

    @Override
    @Transactional
    public Collection<T> mergeAll(Collection<T> entities) {
        Collection<T> collection = new ArrayList<T>(entities.size());
        for (T entity : entities) {
            collection.add(baseEntityDao.merge(entity));
        }
        return collection;
    }

    @Override
    @Transactional
    public void delete(T entity) {
        baseEntityDao.delete(entity);
    }

    @Override
    @Transactional
    public void deleteAll(Collection<T> entities) {
        for (T entity : entities) {
            baseEntityDao.delete(entity);
        }
    }

    @Override
    @Transactional
    public void deleteById(Serializable id) {
        baseEntityDao.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByIds(Collection<? extends Serializable> ids) {
        for (Serializable id : ids) {
            baseEntityDao.deleteById(id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public T getById(Serializable id) {
        return baseEntityDao.getById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public T getById(Serializable id, boolean lock) {
        return baseEntityDao.getById(id, lock);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<T> findAll() {
        return baseEntityDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public PageableQueryResult findByPage(int pageNo, int pageSize, Map<String, Object> parameters) {
        return baseEntityDao.findByPage(pageNo, pageSize, parameters);
    }

    @Override
    @Transactional(readOnly = true)
    public PageableQueryResult findByPage(PageableQueryParameter queryParameter) {
        return baseEntityDao.findByPage(queryParameter);
    }

    @Deprecated
    @Override
    @Transactional(readOnly = true)
    public Collection<T> findByNotNullOrZeroExample(T entity) {
        return baseEntityDao.findByNotNullOrZeroExample(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getNextOrdinal() {
        return baseEntityDao.getNextOrdinal();
    }
}
