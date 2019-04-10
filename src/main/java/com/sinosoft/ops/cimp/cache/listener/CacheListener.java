package com.sinosoft.ops.cimp.cache.listener;


import com.sinosoft.ops.cimp.cache.config.CacheManagerInfo;

/**
 * 监听缓存引擎的接口
 */
public interface CacheListener {

    /**
     * 初始化需要缓存监听cacheMap和清理的clearMap
     */
    void init(CacheManagerInfo.CacheListeners.CacheListener cacheListenerCfg);

    /**
     * 记录缓存时间
     *
     * @param listenerMapVO 监听缓存VO对象
     */
    boolean get(CacheListenerMap listenerMapVO);

    /**
     * 存储监听缓存信息到cacheMap
     */
    void put(CacheListenerMap listenerMapVO);

    /**
     * 获得监听名字
     */
    String getName();

    /**
     * 停止监听，Map为空
     */
    void stop();

    /**
     * 移除失效缓存操作
     */
    boolean remove(CacheListenerMap listenerMapVO);
}
