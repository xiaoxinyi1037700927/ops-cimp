/**
 * @project: IIMP
 * @title: BaseEntityDao.java
 * @copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.common.dao;


import com.sinosoft.ops.cimp.common.model.PageableQueryParameter;
import com.sinosoft.ops.cimp.common.model.PageableQueryResult;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * @param <T>
 * @version 1.0.0
 * @ClassName: BaseEntityDao
 * @description: 实体数据访问基接口
 * @author: Nil
 * @date: 2017年8月1日 上午10:35:11
 */
public interface BaseEntityDao<T> extends BaseDao {
    /**
     * 保存
     *
     * @param entity 实体
     * @return 主键
     */
    Serializable save(T entity);

    /**
     * 保存或更新
     *
     * @param entity 实体
     */
    void saveOrUpdate(T entity);

    /**
     * 更新
     *
     * @param entity 实体
     */
    void update(T entity);

    /**
     * 删除
     *
     * @param entity 实体
     */
    void delete(T entity);

    /**
     * 根据Id删除
     *
     * @param id 标识
     */
    void deleteById(Serializable id);

    /**
     * 根据Id获取
     *
     * @param id 标识
     * @return 实体
     */
    T getById(Serializable id);

    /**
     * 根据Id获取
     *
     * @param id   标识
     * @param lock 是否锁定
     * @return 实体
     */
    T getById(Serializable id, boolean lock);

    /**
     * 合并
     *
     * @param entity 实体
     * @return 实体
     */
    T merge(T entity);

    /**
     * 查找全部
     *
     * @return 实体集
     */
    Collection<T> findAll();

    /**
     * 根据样例查找
     *
     * @param entity 实体
     * @return 实体集
     */
    @Deprecated
    Collection<T> findByExample(T entity);

    /**
     * 根据样例查找,忽略NULL/ZERO值
     *
     * @param entity 实体
     * @return 实体集
     */
    @Deprecated
    Collection<T> findByNotNullOrZeroExample(T entity);

    /**
     * 根据参数分页查询
     *
     * @param pageNo     页号
     * @param pageSize   每页数据行数
     * @param parameters 查询参数
     * @return 分页查询结果
     */
    PageableQueryResult findByPage(final int pageNo, final int pageSize, final Map<String, Object> parameters);

    /**
     * 根据参数分页查询
     *
     * @param queryParameter 查询参数
     * @return 分页查询结果
     */
    PageableQueryResult findByPage(final PageableQueryParameter queryParameter);

    /**
     * 取得下一个排序序号
     *
     * @return 取得下一个排序序号
     */
    Integer getNextOrdinal();

    /**
     * 特殊情况下sql取得UUID
     *
     * @return UUID.tostring
     */
    String toUUIDStringByRaw(Object obj);
}