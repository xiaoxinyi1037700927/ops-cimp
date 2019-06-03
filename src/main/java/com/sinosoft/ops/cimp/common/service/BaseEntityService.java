/**
 * @project: IIMP
 * @title: BaseEntityService.java
 * @copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.common.service;

import com.sinosoft.ops.cimp.common.model.PageableQueryParameter;
import com.sinosoft.ops.cimp.common.model.PageableQueryResult;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * @version 1.0.0
 * @ClassName: BaseEntityService
 * @description: 实体服务基接口
 * @author: Nil
 * @date: 2017年9月25日 下午2:25:50
 */
public interface BaseEntityService<T> extends BaseService {
    /**
     * 创建
     *
     * @param entity 实体
     * @return 主键
     */
    Serializable create(T entity);

    /**
     * 创建全部
     *
     * @param entities 实体集
     */
    void createAll(Collection<T> entities);

    /**
     * 创建或更新
     *
     * @param entity 实体
     */
    void createOrUpdate(T entity);

    /**
     * 创建或更新全部
     *
     * @param entities 实体集
     */
    void createOrUpdateAll(Collection<T> entities);

    /**
     * 更新
     *
     * @param entity 实体
     */
    void update(T entity);

    /**
     * 更新全部
     *
     * @param entities 实体集
     */
    void updateAll(Collection<T> entities);

    /**
     * 合并
     *
     * @param entity 实体
     * @return 实体
     */
    T merge(T entity);

    /**
     * 合并全部
     *
     * @param entities 实体集
     * @return 实体集
     */
    Collection<T> mergeAll(Collection<T> entities);

    /**
     * 删除
     *
     * @param entity 实体
     */
    void delete(T entity);

    /**
     * 删除全部
     *
     * @param entities 实体集
     */
    void deleteAll(Collection<T> entities);

    /**
     * 根据Id删除
     *
     * @param id 标识
     */
    void deleteById(Serializable id);

    /**
     * 根据标识集删除
     *
     * @param ids 标识集
     */
    void deleteByIds(Collection<? extends Serializable> ids);

    /**
     * 根据标识获取
     *
     * @param id 标识
     * @return 实体
     */
    T getById(Serializable id);

    /**
     * 根据标识获取
     *
     * @param id   标识
     * @param lock 是否锁定
     * @return 实体
     */
    T getById(Serializable id, boolean lock);

    /**
     * 查找全部
     */
    @Deprecated
    Collection<T> findAll();

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
     * 根据样例查找,忽略NULL/ZERO值
     *
     * @param entity 实体
     * @return 实体集
     */
    @Deprecated
    Collection<T> findByNotNullOrZeroExample(T entity);

    /**
     * 取得下一个排序序号
     *
     * @return 取得下一个排序序号
     */
    Integer getNextOrdinal();
}
