/**
 * @project: IIMP
 * @title: BaseDao.java
 * @copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.common.dao;

import java.util.Collection;
import java.util.Map;

/**
 * @version 1.0.0
 * @ClassName: BaseDao
 * @description: 数据访问基接口
 * @author: Nil
 * @date: 2017年8月1日 上午10:34:36
 */
public interface BaseDao {
    /**
     * 根据HQL分页查询
     *
     * @param firstResult 起始记录
     * @param maxResults  返回的最大记录条数
     * @param hql         查询HQL语句
     * @param params      参数列表
     * @return 实体集
     */
    Collection<?> findByPageWithHql(final int firstResult, final int maxResults, final String hql, final Map<String, Object> params);

    /**
     * 根据HQL分页查询
     *
     * @param firstResult 起始记录
     * @param maxResults  返回的最大记录条数
     * @param hql         查询HQL语句
     * @param objects     参数列表
     * @return 结果集
     */
    Collection<?> findByPageWithHql(final int firstResult, final int maxResults, final String hql, final Object... objects);

    /**
     * 根据SQL分页查询
     *
     * @param firstResult 起始记录
     * @param maxResults  返回的最大记录条数
     * @param sql         查询SQL语句
     * @param params      参数列表
     * @return 实体集
     */
    Collection<?> findByPageWithSql(final int firstResult, final int maxResults, final String sql, final Map<String, Object> params);

    /**
     * 根据SQL分页查询
     *
     * @param firstResult 起始记录
     * @param maxResults  返回的最大记录条数
     * @param sql         查询SQL语句
     * @param objects     参数列表
     * @return 结果集
     */
    Collection<?> findByPageWithSql(final int firstResult, final int maxResults, final String sql, final Object... objects);

    /**
     * 获取记录总数
     *
     * @param totalCountHql 总记录数查询HQL
     * @param params        参数列表
     * @return 记录总数
     */
    long getTotalCountWithHql(final String totalCountHql, final Map<String, Object> params);

    /**
     * 获取记录总数
     *
     * @param totalCountHql 总记录数查询HQL
     * @param objects       参数列表
     * @return 记录总数
     */
    long getTotalCountWithHql(final String totalCountHql, final Object... objects);

    /**
     * 获取记录总数
     *
     * @param totalCountSql 总记录数查询SQL
     * @param params        参数列表
     * @return 记录总数
     */
    long getTotalCountWithSql(final String totalCountSql, final Map<String, Object> params);

    /**
     * 获取记录总数
     *
     * @param totalCountSql 总记录数查询SQL
     * @param objects       参数列表
     * @return 记录总数
     */
    long getTotalCountWithSql(final String totalCountSql, final Object... objects);

    /**
     * 执行更新SQL
     *
     * @param updateSql 包括INSERT、UPDATE、DELETE；CREATE TABLE 、DROP TABLE等类型的SQL
     * @param params    参数列表
     * @return 受影响的行数
     */
    int executeUpdateSql(String updateSql, final Map<String, Object> params);

    /**
     * 执行更新SQL
     *
     * @param updateSql 包括INSERT、UPDATE、DELETE；CREATE TABLE 、DROP TABLE等类型的SQL
     * @param objects   参数列表
     * @return 受影响的行数
     */
    int executeUpdateSql(String updateSql, final Object... objects);

    /**
     * 执行更新HQL
     *
     * @param updateHql 包括INSERT、UPDATE、DELETE；CREATE TABLE 、DROP TABLE等类型的HQL
     * @param params    参数列表
     * @return 受影响的行数
     */
    int executeUpdateHql(String updateHql, final Map<String, Object> params);

    /**
     * 执行更新HQL
     *
     * @param updateHql 包括INSERT、UPDATE、DELETE；CREATE TABLE 、DROP TABLE等类型的HQL
     * @param objects   参数列表
     * @return 受影响的行数
     */
    int executeUpdateHql(String updateHql, final Object... objects);
}
