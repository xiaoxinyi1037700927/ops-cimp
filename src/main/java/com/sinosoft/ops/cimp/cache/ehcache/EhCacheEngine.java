package com.sinosoft.ops.cimp.cache.ehcache;

import com.sinosoft.ops.cimp.cache.OpsCache;
import com.sinosoft.ops.cimp.cache.config.CacheManagerInfo;
import com.sinosoft.ops.cimp.cache.config.Param;
import com.sinosoft.ops.cimp.constant.OpsErrorMessage;
import com.sinosoft.ops.cimp.exception.SystemException;
import com.vip.vjtools.vjkit.io.FilePathUtil;
import com.vip.vjtools.vjkit.io.ResourceUtil;
import com.vip.vjtools.vjkit.io.URLResourceUtil;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.ConfigurationFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EhCache缓存实现类
 */
public class EhCacheEngine implements OpsCache {

    private static final Logger logger = LoggerFactory.getLogger(EhCacheEngine.class);

    /**
     * 缓存配置文件
     */
    private static final String DEFAULT_CACHE_CONFIG_FILE = "classpath:ehcache.xml";

    /**
     * 传入配置文件名称参数
     */
    private static final String PARAM_CONFIG_FILE_NAME = "configFile";

    /**
     * 缓存对象使用的模板
     */
    private static final String PARAM_CACHE_ITEM_TEMPLATE_NAME = "cacheTemplateName";

    /**
     * 本地非堆栈内存大小
     */
    private static final String PARAM_MAX_BYTES_LOCAL_OFFER_HEAP = "maxBytesLocalOffHeap";

    /**
     * 本地堆栈内存大小
     */
    private static final String PARAM_MAX_BYTES_LOCAL_HEAP = "maxBytesLocalHeap";

    /**
     * 本地堆可以存储的最大缓存数量
     */
    private static final String PARAM_MAX_ENTRIES_LOCAL_HEAP = "maxEntriesLocalHeap";

    /**
     * Ehcache默认模板名称
     */
    private static final String DEFAULT_CACHE_TEMPLATE_NAME = "default";

    /**
     * EhCache引擎
     */
    private net.sf.ehcache.CacheManager manager = null;

    /**
     * 缓存名称
     */
    private String name = null;

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
        String configFileName = null;
        // 初始化缓存引擎
        List<Param> cacheEngineParams;
        if (inCacheEngine.getParams() != null) {
            cacheEngineParams = inCacheEngine.getParams().getParam();
        } else {
            cacheEngineParams = new ArrayList<>();
        }
        for (Param param : cacheEngineParams) {
            if (PARAM_CONFIG_FILE_NAME.equalsIgnoreCase(param.getName())) {
                configFileName = param.getValue();
                break;
            }
        }

        if (StringUtils.isEmpty(configFileName)) {
            configFileName = DEFAULT_CACHE_CONFIG_FILE;
        }
        // 从classpath中找寻配置文件并创建
        // 注意若使用SpringBoot框架则获取到类加载器可能会是SpringBoot的所以加载不到文件
        // 但开发测试的时候类加载器是jdk的所以又可以加载到文件，这里已经修复这个问题
        URL configurationFileURL;
        try {
            configurationFileURL = ResourceUtil.asUrl(configFileName);
        } catch (Exception e) {
            try {
                configurationFileURL = URLResourceUtil.asFile(configFileName).toURI().toURL();
            } catch (IOException e1) {
                throw new SystemException(OpsErrorMessage.MODULE_NAME, e, false, OpsErrorMessage.FILE_IO_ERROR, configFileName);
            }
        }

        Configuration configuration = ConfigurationFactory.parseConfiguration(configurationFileURL);
        String path = configuration.getDiskStoreConfiguration().getPath();
        path = FilePathUtil.simplifyPath(path);
        configuration.getDiskStoreConfiguration().setPath(path);
        if (logger.isDebugEnabled()) {
            logger.debug("EhCache缓存文件目录配置为：【" + path + "】");
        }
        manager = net.sf.ehcache.CacheManager.create(configuration);
        if (logger.isDebugEnabled()) {
            logger.debug("EhCache启动成功，启动配置文件为：【" + configurationFileURL + "】");
        }
        /* 获取使用Ehcache引擎的配置信息 */
        List<String> thisEngineList = new ArrayList<>();
        for (CacheManagerInfo.CacheEngines.CacheEngine cacheEngine : cacheManagerInfo.getCacheEngines().getCacheEngine()) {
            String implName = cacheEngine.getImplClass();
            if (EhCacheEngine.class.getName().equals(implName)) {
                thisEngineList.add(cacheEngine.getEngineName());
            }
        }
        for (CacheManagerInfo.CacheItems.CacheItem cacheItem : cacheManagerInfo.getCacheItems().getCacheItem()) {
            String cacheName = cacheItem.getCacheName();
            String cacheType = cacheItem.getEngineName();
            // 构建使用本引擎的缓存管理器
            if (inCacheEngine.getEngineName().equals(cacheType)) {
                List<Param> cacheItemParams;
                if (cacheItem.getParams() != null) {
                    cacheItemParams = cacheItem.getParams().getParam();
                } else {
                    cacheItemParams = new ArrayList<Param>();
                }
                String cacheTemplateName = null;
                String maxBytesLocalOffHeap = null;
                String maxBytesLocalHeap = null;
                Long maxEntriesLocalHeap = null;
                // 获取缓存项目使用缓存模板参数
                for (Param param : cacheItemParams) {
                    if (PARAM_CACHE_ITEM_TEMPLATE_NAME.equalsIgnoreCase(param.getName())) {
                        cacheTemplateName = param.getValue();
                    } else if (PARAM_MAX_BYTES_LOCAL_HEAP.equalsIgnoreCase(param.getName())) {
                        maxBytesLocalHeap = param.getValue();
                    } else if (PARAM_MAX_BYTES_LOCAL_OFFER_HEAP.equalsIgnoreCase(param.getName())) {
                        maxBytesLocalOffHeap = param.getValue();
                    } else if (PARAM_MAX_ENTRIES_LOCAL_HEAP.equalsIgnoreCase(param.getName())) {
                        maxEntriesLocalHeap = Long.valueOf(param.getValue());
                    }
                }

                // 模板不存在创建模板
                if (!manager.cacheExists(cacheName)) {
                    CacheConfiguration config;
                    if (StringUtils.isEmpty(cacheTemplateName)) {
                        config = manager.getConfiguration().getDefaultCacheConfiguration();
                    } else {
                        Cache customCacheTemplate = manager.getCache(cacheTemplateName);
                        config = customCacheTemplate.getCacheConfiguration();
                    }
                    // 设置使用的最大堆内存
                    if (StringUtils.isNotEmpty(maxBytesLocalHeap)) {
                        config.setMaxBytesLocalHeap(maxBytesLocalHeap);
                    }
                    // 设置使用的最大非堆内存
                    if (StringUtils.isNotEmpty(maxBytesLocalOffHeap)) {
                        config.setMaxBytesLocalOffHeap(maxBytesLocalOffHeap);
                    }
                    // 设置最大的本地缓存对象梳理
                    if (maxEntriesLocalHeap != null) {
                        config.setMaxEntriesLocalHeap(maxEntriesLocalHeap);
                    }
                    // 设置配置信息
                    config.setName(cacheName);
                    Cache customCache = new Cache(config);
                    manager.addCache(customCache);
                }
            }
        }
    }


    /**
     * 根据 cacheName查找 manager找是否存在 cache，如果存在 就返回，不存在则先创建 后返回
     *
     * @param cacheName 缓存名称
     * @return Cache 缓存实例
     */
    public Cache getCache(String cacheName) {
        Cache cache = manager.getCache(cacheName);
        if (cache != null) {
            // 缓存大类已经存在，直接返回
            return cache;
        } else {
            synchronized (EhCacheEngine.class) {
                // 同步控制
                cache = manager.getCache(cacheName);
                if (cache == null) {
                    // 在同步锁控制下进一步判断缓存是否加载
                    manager.addCache(cacheName);
                    return manager.getCache(cacheName);
                } else {
                    return cache;
                }
            }
        }
    }

    /**
     * 从缓存获取值
     *
     * @param cacheName 缓存名称
     * @param key       缓存键
     * @param <T>       泛型
     * @return T 返回对象实例
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String cacheName, Object key) {
        if (key == null) {
            return null;
        }
        Element element = this.getCache(cacheName).get(key);
        return (T) (element == null ? null : element.getObjectValue());
    }

    private List getKeys(String cacheName) {
        return this.getCache(cacheName).getKeys();
    }

    /**
     * 根据大类 获取所有的key-value集合
     *
     * @param cacheName 缓存名称
     * @return 以键值对存储的所有缓存
     */
    public Map<Object, Object> getAll(String cacheName) {
        if (cacheName == null) {
            return null;
        }
        List list = getKeys(cacheName);
        if (list == null || list.size() == 0) {
            return null;
        }
        Map map = this.getCache(cacheName).getAll(list);
        Map<Object, Object> retMap = new HashMap<Object, Object>(map.size());
        for (Object o : map.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            Element element = (Element) entry.getValue();
            retMap.put(entry.getKey(), element.getObjectValue());
        }
        return retMap;
    }

    /**
     * @param cacheName 缓存名称
     * @param key       缓存键
     * @param value     缓存值
     * @return boolean
     */
    public boolean put(String cacheName, Object key, Object value) {
        return this.put(cacheName, key, value, 0);
    }

    /**
     * @param cacheName  缓存名称
     * @param key        缓存键
     * @param value      缓存值
     * @param expiryTime 缓存时间
     * @return boolean
     */
    public boolean put(String cacheName, Object key, Object value, long expiryTime) {
        if (key == null) {
            return false;
        }
        Cache cache = this.getCache(cacheName);
        Element element = new Element(key, value);
        if (expiryTime > 0) {
            element.setTimeToLive((int) expiryTime);
        }
        cache.put(element);
        return true;
    }

    /**
     * @param cacheName  缓存名称
     * @param key        缓存键
     * @param expiryTime 缓存时间
     * @return boolean
     */
    public boolean remove(String cacheName, Object key, long expiryTime) {
        if (key == null) {
            return false;
        }
        Cache cache = this.getCache(cacheName);
        cache.remove(key);
        return true;
    }

    /**
     * 停止缓存
     */
    public void stop() {
        manager.shutdown();
        if (logger.isDebugEnabled()) {
            logger.info("EhCache关闭成功!");
        }
    }

    public boolean remove(String cacheName) {
        manager.getCache(cacheName).removeAll();
        return true;
    }
}
