package com.sinosoft.ops.cimp.cache;

import com.sinosoft.ops.cimp.cache.config.CacheManagerInfo;

import java.util.Map;

/**
 * 缓存引擎执行接口
 */
public interface OpsCache {
    /**
     * 获取缓存名称
     *
     * @return 缓存引擎名称
     */
    String getName();

    /**
     * 设置缓存引擎名称
     *
     * @param cacheEngineName 缓存引擎名称
     */
    void setName(String cacheEngineName);

    /**
     * 初始化缓存引擎
     *
     * @param cacheManagerInfo 缓存配置信息
     * @param cacheEngine      引擎配置信息
     */
    void init(CacheManagerInfo cacheManagerInfo, CacheManagerInfo.CacheEngines.CacheEngine cacheEngine);

    /**
     * 停止缓存管理器
     */
    void stop();

    /**
     * 从缓存中读取制定类型的元素
     *
     * @param <T>       缓存对象类型
     * @param cacheName 缓存大类名称
     * @param key       缓存对象存储关键字
     * @return 对应类型的缓存对象
     */
    <T> T get(final String cacheName, final Object key);

    /**
     * 根据缓存大类返回所有的key-value集合
     *
     * @param cacheName 缓存大类
     */
    Map<Object, Object> getAll(final String cacheName);

    /**
     * 保存缓存信息
     *
     * @param cacheName  缓存大类名称
     * @param key        缓存存储关键字
     * @param value      缓存值信息
     * @param expiryTime 缓存有效期，0：永久有效， xx：xx秒后缓存失效
     * @return 缓存保存结果， True：缓存保存， False：缓存保存失效
     */
    boolean put(final String cacheName, final Object key, final Object value, final long expiryTime);

    /**
     * 删除缓存信息
     *
     * @param cacheName  缓存大类名称
     * @param key        缓存存储关键字
     * @param expiryTime 缓存引擎可能不支持
     * @return 缓存删除结果， True：删除成功， False：删除失败
     */
    boolean remove(final String cacheName, final Object key, final long expiryTime);

    /**
     * 移除缓存
     *
     * @param cacheName 缓存大类
     */
    boolean remove(final String cacheName);
}
