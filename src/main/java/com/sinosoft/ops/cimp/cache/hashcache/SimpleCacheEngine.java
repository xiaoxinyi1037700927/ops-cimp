package com.sinosoft.ops.cimp.cache.hashcache;


import com.sinosoft.ops.cimp.cache.OpsCache;
import com.sinosoft.ops.cimp.cache.config.CacheManagerInfo;
import com.sinosoft.ops.cimp.constant.OpsErrorMessage;
import com.sinosoft.ops.cimp.exception.SystemException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 通过MemoryCached实现Cache功能
 */
public class SimpleCacheEngine implements OpsCache {
    /**
     * 缓存引擎
     */
    private Map<String, Map<Object, Object>> cacheEngine;

    /**
     * 缓存名称
     */
    private String name = null;

    /**
     * 初始化缓存引擎
     */
    public SimpleCacheEngine() {
        cacheEngine = Collections.synchronizedMap(new HashMap<String, Map<Object, Object>>());
    }

    /**
     * 获取缓存名称
     *
     * @return 缓存引擎名称
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * 设置缓存引擎名称
     *
     * @param cacheEngineName 缓存引擎名称
     */
    @Override
    public void setName(String cacheEngineName) {
        this.name = cacheEngineName;
    }

    /**
     * 初始化缓存引擎
     *
     * @param cacheManagerInfo 缓存配置信息
     * @param inCacheEngine    引擎配置信息
     */
    @Override
    public void init(CacheManagerInfo cacheManagerInfo, CacheManagerInfo.CacheEngines.CacheEngine inCacheEngine) {
        cacheEngine = Collections.synchronizedMap(new HashMap<String, Map<Object, Object>>());
    }

    /**
     * 停止缓存引擎
     */
    public void stop() {
        cacheEngine = null;
    }

    /**
     * 获取缓存信息
     *
     * @param cacheName 缓存大类
     * @return 缓存大类信息
     */
    private Map<Object, Object> getCache(String cacheName) {
        if (cacheEngine == null) {
            throw new SystemException(OpsErrorMessage.MODULE_NAME, null, false, OpsErrorMessage.CACHE_ERROR_100103);
        }
        return cacheEngine.computeIfAbsent(cacheName, k -> new HashMap<>());
    }

    /**
     * 从缓存中获取缓存信息
     *
     * @param cacheName 缓存大类名称
     * @param key       缓存信息key
     * @return 缓存对象
     */
    @SuppressWarnings("unchecked")
    public Object get(String cacheName, Object key) {
        Object resultObj;
        if (null == key) {
            return null;
        }
        // 从memoryCached中取得缓存数据
        resultObj = getCache(cacheName).get(key.toString());
        // 返回处理结果
        return resultObj;
    }

    /**
     * 获取缓存信息
     *
     * @param <T>       缓存对象类型
     * @param cacheName 缓存大类名称
     * @param key       缓存存储关键字
     * @param clazz     缓存对象类型
     * @return T类型的缓存对象或则null
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String cacheName, Object key, Class<T> clazz) {
        Object resultObj;
        // key为空返回
        if (null == key) {
            return null;
        }
        // 从memoryCached中取得缓存数据
        resultObj = getCache(cacheName).get(key.toString());
        // 返回处理结果
        return (T) resultObj;
    }

    /**
     * 根据大类返回所有key-value集合
     */
    public Map<Object, Object> getAll(String cacheName) {
        if (null == cacheName) {
            return null;
        }
        return getCache(cacheName);
    }

    /**
     * 保存缓存信息
     *
     * @param cacheName  缓存大类名称
     * @param key        缓存存储关键字
     * @param value      缓存值信息
     * @param expiryTime 缓存有效期，0：永久有效，不支持有效期设置
     * @return 缓存保存结果， True：缓存保存， False：缓存保存失效
     */
    public boolean put(String cacheName, Object key, Object value, long expiryTime) {
        return put(cacheName, key, value);
    }

    /**
     * 永久保存缓存信息
     *
     * @param cacheName 缓存大类名称
     * @param key       缓存存储关键字
     * @param value     缓存值信息
     * @return 缓存保存结果， True：缓存保存， False：缓存保存失效
     */
    public boolean put(String cacheName, Object key, Object value) {
        if (null == key) {
            return false;
        }
        getCache(cacheName).put(key.toString(), value);
        return true;
    }

    /**
     * 删除缓存信息
     *
     * @param cacheName  缓存大类名称
     * @param key        缓存存储关键字
     * @param expiryTime 缓存引擎不支持
     * @return 缓存删除结果， True：删除成功， False：删除失败
     */
    public boolean remove(String cacheName, Object key, long expiryTime) {
        if (null == key) {
            return false;
        }
        getCache(cacheName).remove(key.toString());
        return true;
    }


    public boolean remove(String cacheName) {
        cacheEngine.remove(cacheName);
        return true;
    }
}
