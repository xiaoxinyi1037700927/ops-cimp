package com.sinosoft.ops.cimp.cache;

import com.sinosoft.ops.cimp.cache.config.CacheManagerInfo;
import com.sinosoft.ops.cimp.cache.config.Param;
import com.sinosoft.ops.cimp.cache.listener.CacheListener;
import com.sinosoft.ops.cimp.cache.listener.CacheListenerMap;
import com.sinosoft.ops.cimp.constant.OpsErrorMessage;
import com.sinosoft.ops.cimp.exception.SystemException;
import com.sinosoft.ops.cimp.util.JaxbUtil;
import com.vip.vjtools.vjkit.io.ResourceUtil;
import com.vip.vjtools.vjkit.io.URLResourceUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CacheManager {
    /**
     * 日志输出
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 缓存管理器实例
     */
    private static CacheManager cacheManager = null;

    /**
     * 缓存引擎
     */
    private Map<String, OpsCache> cacheEngines = new HashMap<>();

    /**
     * 缓存内容与引擎对应关系
     */
    private Map<String, CacheManagerInfo.CacheItems.CacheItem> cacheItems = new HashMap<>();

    /**
     * 存放监听信息集合
     */
    private List<CacheManagerInfo.CacheListeners.CacheListener> cacheListenerList = new ArrayList<>();

    /**
     * 监听的具体实例
     */
    private List<CacheListener> cacheListenerImpls = new ArrayList<>();

    /**
     * 配置文件
     */
    private static final String CONFIG_FILE = "classpath:cfgCache.xml";

    /**
     * 多级缓存位置
     */
    private static final String PARAM_LEVEL_CACHE_ENGINE = "cacheEngine";

    /**
     * 返回新的CacheManager实例对象
     *
     * @param cacheManagerInfo 配置信息为Null时返回默认实例对象
     */
    public static CacheManager getInstance(CacheManagerInfo cacheManagerInfo) {
        if (cacheManagerInfo == null) {
            return getInstance();
        } else {
            return new CacheManager(cacheManagerInfo);
        }
    }

    /**
     * 获取指定缓存引擎
     *
     * @param cacheEngineName 缓存引擎配置名称
     * @return 缓存引擎
     */
    public OpsCache getCacheEngines(String cacheEngineName) {
        return this.cacheEngines.get(cacheEngineName);
    }

    /**
     * 创建一个新的实例 CacheManager.
     */
    private CacheManager(CacheManagerInfo cacheManagerInfo) {
        if (logger.isDebugEnabled()) {
            logger.debug("初始化缓存管理器，配置信息为【" + cacheManagerInfo + "】");
        }
        /* 初始化缓存引擎 */
        for (CacheManagerInfo.CacheEngines.CacheEngine cacheEngine : cacheManagerInfo.getCacheEngines().getCacheEngine()) {
            String engineName = cacheEngine.getEngineName();
            String implName = cacheEngine.getImplClass();
            try {
                if (logger.isDebugEnabled()) {
                    logger.debug("初始化缓存引擎开始，【engineName】=【" + engineName + "】，【implName】=【" + implName + "】");
                }
                OpsCache engineImpl = (OpsCache) Class.forName(implName).newInstance();
                engineImpl.setName(engineName);
                engineImpl.init(cacheManagerInfo, cacheEngine);
                cacheEngines.put(engineName, engineImpl);
                if (logger.isDebugEnabled()) {
                    logger.debug("缓存引擎【engineName】=【" + engineName + "】启动成功...");
                }
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                logger.error("缓存引擎【engineName】=【" + engineName + "】启动失败", e);
                throw new SystemException(OpsErrorMessage.MODULE_NAME, e, false, OpsErrorMessage.CACHE_ERROR_100101, engineName, implName);
            }
        }
        /* 初始化缓存内容 */
        for (CacheManagerInfo.CacheItems.CacheItem cacheItem :
                cacheManagerInfo.getCacheItems().getCacheItem()) {
            String engineName = cacheItem.getEngineName();
            String cacheName = cacheItem.getCacheName();
            if (!cacheEngines.containsKey(engineName)) {
                throw new SystemException(OpsErrorMessage.MODULE_NAME, null, false, OpsErrorMessage.CACHE_ERROR_100104, cacheName, engineName);
            } else {
                cacheItems.put(cacheName, cacheItem);
            }
        }
        /* 初始化监听信息*/
        if (logger.isDebugEnabled()) {
            logger.debug("初始化缓存监听器，配置信息为【" + cacheManagerInfo + "】");
        }
        CacheManagerInfo.CacheListeners cacheListeners = cacheManagerInfo.getCacheListeners();
        if (cacheListeners != null) {
            this.cacheListenerList = cacheListeners.getCacheListener();
            if (cacheListenerList != null && cacheListenerList.size() > 0) {
                for (CacheManagerInfo.CacheListeners.CacheListener cacheListener : cacheListenerList) {
                    //得到每个监听对象
                    String listenerName = cacheListener.getListenerName();//监听器名字
                    String implClass = cacheListener.getImplClass();//监听器的全路径
                    try {
                        if (logger.isDebugEnabled()) {
                            logger.debug("初始化监听引擎开始，【listenerName】=【" + listenerName + "】，【implName】=【" + implClass + "】");
                        }
                        //获得具体的监听实例
                        CacheListener iListenerImpl = (CacheListener) Class.forName(implClass).newInstance();
                        //初始化实例对象，Map缓存监听和清理信息的Map
                        iListenerImpl.init(cacheListener);
                        //将具体实例存到List集合
                        this.cacheListenerImpls.add(iListenerImpl);
                        if (logger.isDebugEnabled()) {
                            logger.debug("监听引擎【listenerName】=【" + listenerName + "】启动成功");
                        }
                    } catch (InstantiationException e) {
                        logger.error("监听引擎[listenerName]=" + listenerName + "启动失败", e);
                        throw new SystemException(OpsErrorMessage.MODULE_NAME, e, false, OpsErrorMessage.CACHE_ERROR_100101, listenerName, implClass);
                    } catch (IllegalAccessException e) {
                        logger.error("监听引擎[listenerName]=" + implClass + "启动失败", e);
                        throw new SystemException(OpsErrorMessage.MODULE_NAME, e, false, OpsErrorMessage.CACHE_ERROR_100101, listenerName, implClass);
                    } catch (ClassNotFoundException e) {
                        logger.error("监听引擎[listenerName]=" + listenerName + "启动失败", e);
                        throw new SystemException(OpsErrorMessage.MODULE_NAME, e, false, OpsErrorMessage.CACHE_ERROR_100105, listenerName, implClass);
                    }
                }
            }
        } else {
            logger.warn("监听清理缓存配置文件不存在，缓存清理监听管理器未初始化！");
        }
    }

    /**
     * 获取缓存配置信息
     *
     * @param inConfigFile 配置文件，为NULL时取默认配置文件
     * @return 配置信息
     */
    public static CacheManagerInfo getCacheManagerInfo(String inConfigFile) {
        // 设置配置文件
        String configFile = CONFIG_FILE;
        if (StringUtils.isNotEmpty(inConfigFile)) {
            configFile = inConfigFile;
        }
        CacheManagerInfo cacheManagerInfo;
        //此处注意，SpringBoot和普通JavaEE项目不同，
        // 因为有其自定义类加载器，在加载资源文件时会有不同
        try {
            try (InputStream inputStream = ResourceUtil.asStream(configFile)) {
                cacheManagerInfo = JaxbUtil.unmarshal(CacheManagerInfo.class, inputStream);
            }
        } catch (Exception e) {
            try (InputStream inputStream1 = URLResourceUtil.asStream(configFile)) {
                cacheManagerInfo = JaxbUtil.unmarshal(CacheManagerInfo.class, inputStream1);
                return cacheManagerInfo;
            } catch (IOException e1) {
                throw new SystemException(OpsErrorMessage.MODULE_NAME, e, false, OpsErrorMessage.FILE_IO_ERROR, configFile);
            }
        }
        return cacheManagerInfo;
    }

    /**
     * 根据大类加载缓存
     *
     * @param cacheName 缓存大类
     * @return ICache 缓存实例
     */
    private List<OpsCache> loadCache(final String cacheName) {
        // 处理结果, 元素0存储一级缓存
        List<OpsCache> cacheEngineList = new ArrayList<>(2);
        CacheManagerInfo.CacheItems.CacheItem cacheItem = cacheItems.get(cacheName);
        if (cacheItem == null) {
            throw new SystemException(OpsErrorMessage.MODULE_NAME, null, false, OpsErrorMessage.CACHE_ERROR_100106, cacheName);
        }
        // 获取主缓存引擎
        String cacheType = cacheItem.getEngineName();
        OpsCache iCache = cacheEngines.get(cacheType);
        if (cacheType == null) {
            throw new SystemException(OpsErrorMessage.MODULE_NAME, null, false, OpsErrorMessage.CACHE_ERROR_100104, cacheName, null);
        }
        cacheEngineList.add(iCache);
        // 获取参数中以cacheEngine名称开始的其他缓存
        if (cacheItem.getParams() != null) {
            for (Param param : cacheItem.getParams().getParam()) {
                if (param.getName().startsWith(PARAM_LEVEL_CACHE_ENGINE)) {
                    String levelCacheEngineName = param.getValue();
                    iCache = cacheEngines.get(levelCacheEngineName);
                    cacheEngineList.add(iCache);
                }
            }
        }
        // 返回获取结果
        return cacheEngineList;
    }

    /**
     * @return 缓存管理工具
     */
    public static CacheManager getInstance() {
        if (cacheManager == null) {
            synchronized (CacheManager.class) {
                if (cacheManager == null) {
                    cacheManager = getInstance(getCacheManagerInfo(null));
                }
            }
        }
        return cacheManager;
    }

    /**
     * 从缓存中读取制定类型的元素
     *
     * @param <T>       缓存对象类型
     * @param cacheName 缓存大类名称
     * @param key       缓存对象存储关键字
     * @return 对应类型的缓存对象
     */
    @SuppressWarnings("unchecked")
    public synchronized <T> T get(final String cacheName, final Object key) {
        if (logger.isTraceEnabled()) {
            logger.trace("调用方法【get】开始，入参：【cacheName】=【" + cacheName + "】，【key】=【" + key + "】");
        }
        Object result = null;
        List<OpsCache> cacheList = this.loadCache(cacheName);
        for (int i = 0; i < cacheList.size(); i++) {
            OpsCache cache = cacheList.get(i);
            try {
                result = cache.get(cacheName, key);
                if (result != null) {
                    // 从二级缓存读取到的内容保存到一级缓存
                    if (i > 0) {
                        cacheList.get(0).put(cacheName, key, result, this.getExpireTime(cacheName));
                    }
                    // 读取到缓存内容后直接跳出
                    break;
                }
            } catch (Exception e) {
                logger.warn("读取缓存异常", new SystemException(OpsErrorMessage.MODULE_NAME, e, false, OpsErrorMessage.CACHE_ERROR_100107, cache.getName(), cacheName));
            }
        }
        //---监听
        for (int j = 0; cacheListenerImpls != null && j < cacheListenerImpls.size(); j++) {
            CacheListener cacheListenerImpl = cacheListenerImpls.get(j);
            CacheListenerMap listenerMapVO = new CacheListenerMap();
            listenerMapVO.setCacheName(cacheName);
            listenerMapVO.setKey(key);
            //修改最近访问时间
            cacheListenerImpl.get(listenerMapVO);
        }
        if (logger.isTraceEnabled()) {
            logger.trace("调用方法【get】结束，出参：【result】=【" + result + "】");
        }
        return (T) result;
    }

    /**
     * 根据缓存大类返回 key-value的集合
     *
     * @param cacheName 缓存大类
     * @return Map集合
     */
    @SuppressWarnings("rawtypes")
    public Map getAll(final String cacheName) {
        OpsCache cache = this.loadCache(cacheName).get(0);
        try {
            return cache.getAll(cacheName);
        } catch (Exception e) {
            logger.warn("获取缓存全集异常", new SystemException(OpsErrorMessage.MODULE_NAME, e, false, OpsErrorMessage.CACHE_ERROR_100107, cache.getName(), cacheName));
            return null;
        }
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
    public synchronized boolean put(final String cacheName, final Object key, final Object value,
                                    final long expiryTime) {
        boolean cacheResult = false;
        for (OpsCache cache : this.loadCache(cacheName)) {
            try {
                cacheResult = cache.put(cacheName, key, value, expiryTime);
            } catch (Exception e) {
                logger.warn("保存缓存异常", new SystemException(OpsErrorMessage.MODULE_NAME, e, false, OpsErrorMessage.CACHE_ERROR_100107, cache.getName(), cacheName));
                cacheResult = false;
            }
        }
        if (cacheListenerImpls != null) {
            for (CacheListener cacheListenerImpl : cacheListenerImpls) {
                CacheListenerMap listenerMap = new CacheListenerMap();
                listenerMap.setCacheName(cacheName);
                listenerMap.setKey(key);
                listenerMap.setExpiryTime(expiryTime);
                cacheListenerImpl.put(listenerMap);
            }
        }
        return cacheResult;
    }

    /**
     * 得到配置文件的过期时间
     */
    private Long getExpireTime(final String cacheName) {
        Long lDefaultExpireTime = 0L;//有效时间
        //从配置文件中读取
        CacheManagerInfo.CacheItems.CacheItem cacheItem = cacheItems.get(cacheName);
        //配置文件中默认过期时间
        String defaultExpireTime = cacheItem.getDefaultExpireTime();
        if (StringUtils.isEmpty(defaultExpireTime)) {
            //如果配置文件读不到默认过期时间
            lDefaultExpireTime = 0L;
        } else if (StringUtils.isNotEmpty(defaultExpireTime)) {
            long default_expire_time = Long.parseLong(defaultExpireTime);
            lDefaultExpireTime = default_expire_time * 1000L;
        }
        return lDefaultExpireTime;
    }

    /**
     * 永久保存缓存信息
     *
     * @param cacheName 缓存大类名称
     * @param key       缓存存储关键字
     * @param value     缓存值信息
     * @return 缓存保存结果， True：缓存保存， False：缓存保存失效
     */
    public synchronized boolean put(final String cacheName, final Object key, final Object value) {
        boolean cacheResult = false;
        for (OpsCache cache : this.loadCache(cacheName)) {
            try {
                Long expireTime = getExpireTime(cacheName);//有效时间
                cacheResult = this.put(cacheName, key, value, expireTime);
            } catch (Exception e) {
                logger.warn("保存缓存异常", new SystemException(OpsErrorMessage.MODULE_NAME, e, false, OpsErrorMessage.CACHE_ERROR_100107, cache.getName(), cacheName));
                cacheResult = false;
            }
        }
        return cacheResult;
    }

    /**
     * 删除缓存信息
     *
     * @param cacheName  缓存大类名称
     * @param key        缓存存储关键字
     * @param expiryTime 缓存失效时间 0：立即删除， xx：xx秒后缓存失效
     * @return 缓存删除结果， True：删除成功， False：删除失败
     */
    public synchronized boolean remove(final String cacheName, final Object key, final long expiryTime) {
        boolean removeResult = false;
        for (OpsCache cache : this.loadCache(cacheName)) {
            removeResult = cache.remove(cacheName, key, expiryTime);
            //监听
            this.removeListener(cacheName, key, expiryTime);
        }
        return removeResult;
    }

    /**
     * 删除缓存信息
     *
     * @param cacheName 缓存大类名称
     * @param key       缓存存储关键字
     * @return 缓存删除结果， True：删除成功， False：删除失败
     */
    public synchronized boolean remove(final String cacheName, final Object key) {
        return this.remove(cacheName, key, 0L);
    }

    /**
     * 删除缓存大类信息
     *
     * @param cacheName 缓存大类名称
     * @return 缓存删除结果， True：删除成功， False：删除失败
     */
    public synchronized boolean remove(final String cacheName) {
        boolean removeResult = false;
        for (OpsCache cache : this.loadCache(cacheName)) {
            removeResult = cache.remove(cacheName);
            //监听
            this.removeListener(cacheName, null, null);
        }
        return removeResult;
    }

    /**
     * 移除监听信息
     *
     * @param cacheName 缓存大类名称
     * @param key       缓存存储关键字
     * @return 删除结果， True：删除成功， False：删除失败
     */
    private synchronized boolean removeListener(final String cacheName, final Object key, final Long expiryTime) {
        boolean removeResult = false;
        if (cacheListenerImpls != null) {
            for (CacheListener cacheListenerImpl : cacheListenerImpls) {
                CacheListenerMap listenerMapVO = new CacheListenerMap();
                listenerMapVO.setCacheName(cacheName);
                listenerMapVO.setKey(key);
                listenerMapVO.setExpiryTime(expiryTime);
                removeResult = cacheListenerImpl.remove(listenerMapVO);
            }
        } else {
            logger.warn("监听清理缓存配置文件不存在，缓存清理监听管理器初始化失败！");
        }
        return removeResult;
    }
}
