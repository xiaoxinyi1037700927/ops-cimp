package com.sinosoft.ops.cimp.cache.empty;


import com.sinosoft.ops.cimp.cache.OpsCache;
import com.sinosoft.ops.cimp.cache.config.CacheManagerInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * 空的缓存引擎实现，在测试阶段使用
 */
public class EmptyCacheEngine implements OpsCache {

    /**
     * 缓存引擎名称
     */
    private String name;

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
        name = cacheEngineName;
    }

    /**
     * 初始化缓存引擎
     *
     * @param cacheManagerInfo 缓存配置信息
     * @param cacheEngine      引擎配置信息
     */
    @Override
    public void init(CacheManagerInfo cacheManagerInfo, CacheManagerInfo.CacheEngines.CacheEngine cacheEngine) {
    }

    /**
     * 停止缓存管理器
     */
    @Override
    public void stop() {
    }

    /**
     * 从缓存中读取制定类型的元素
     *
     * @param <T>       缓存对象类型
     * @param cacheName 缓存大类名称
     * @param key       缓存对象存储关键字
     * @return 对应类型的缓存对象
     */
    @Override
    public <T> T get(String cacheName, Object key) {
        return null;
    }

    /**
     * 根据缓存大类返回所有的key-value集合
     *
     * @param cacheName 缓存大类
     */
    @Override
    public Map<Object, Object> getAll(String cacheName) {
        return new HashMap<Object, Object>();
    }

    /**
     * 保存缓存信息
     *
     * @param cacheName  缓存大类名称
     * @param key        缓存存储关键字
     * @param value      缓存值信息
     * @param expiryTime 缓存有效期，0：永久有效， xx：xx秒后缓存失效
     * @return 缓存保存结果， True：缓存保存， False：缓存保存失效
     */
    @Override
    public boolean put(String cacheName, Object key, Object value, long expiryTime) {
        return true;
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
        return true;
    }

    /**
     * 删除大类的所有缓存星系
     *
     * @param cacheName 缓存大类名称
     * @return 缓存删除结果， True：删除成功， False：删除失败
     */
    public boolean remove(String cacheName) {
        return true;
    }

}
