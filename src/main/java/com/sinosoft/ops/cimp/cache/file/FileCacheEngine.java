package com.sinosoft.ops.cimp.cache.file;

import com.sinosoft.ops.cimp.cache.OpsCache;
import com.sinosoft.ops.cimp.cache.config.CacheManagerInfo;
import com.sinosoft.ops.cimp.cache.config.Param;
import com.sinosoft.ops.cimp.constant.OpsErrorMessage;
import com.sinosoft.ops.cimp.exception.SystemException;
import com.vip.vjtools.vjkit.base.Platforms;
import com.vip.vjtools.vjkit.io.FilePathUtil;
import com.vip.vjtools.vjkit.io.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 文件序列化缓存.
 */
public class FileCacheEngine implements OpsCache {
    /**
     * 日志输出
     */
    private static final Logger logger = LoggerFactory.getLogger(FileCacheEngine.class);

    private static final String CACHE_FILE_FOLDER_NAME = "cacheFolder";

    private static final String DEFAULT_CACHE_FOLDER = "fileCache";

    private String name = null;

    private static final String DEFAULT_FILE_EXT = ".dat";

    private String cacheFolderPath = null;

    private Map<String, String> cacheItemFolder = new HashMap<String, String>();

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
     * @param cacheEngine      引擎配置信息
     */
    @Override
    public void init(CacheManagerInfo cacheManagerInfo, CacheManagerInfo.CacheEngines.CacheEngine cacheEngine) {
        String configCacheFolder = null;
        // 初始化缓存引擎
        List<Param> cacheEngineParams;
        if (cacheEngine.getParams() != null) {
            cacheEngineParams = cacheEngine.getParams().getParam();
        } else {
            cacheEngineParams = new ArrayList<Param>();
        }
        for (Param param : cacheEngineParams) {
            if (CACHE_FILE_FOLDER_NAME.equalsIgnoreCase(param.getName())) {
                configCacheFolder = param.getValue();
                break;
            }
        }
        if (StringUtils.isEmpty(configCacheFolder)) {
            configCacheFolder = DEFAULT_CACHE_FOLDER;
        }

        String fileCacheFolder = FilePathUtil.concat(Platforms.USER_HOME, configCacheFolder);
        cacheFolderPath = configCacheFolder;
        try {
            // 创建目录
            FileUtil.makesureDirExists(fileCacheFolder);
        } catch (IOException e) {
            throw new SystemException(OpsErrorMessage.MODULE_NAME, e, false, OpsErrorMessage.FILE_IO_ERROR, fileCacheFolder);
        }
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
    @SuppressWarnings("unchecked")
    public <T> T get(String cacheName, Object key) {
        if (logger.isDebugEnabled()) {
            logger.debug("调用类【FileCacheEngine】方法【get】开始，入参：【cacheName】=【" + cacheName + "】，【key】=【" + key + "】");
        }
        String filePath = FilePathUtil.concat(Platforms.USER_HOME, cacheFolderPath, cacheName, key.toString(), DEFAULT_FILE_EXT);
        return (T) FileSerializer.deSerialize(filePath);
    }

    /**
     * 根据缓存大类返回所有的key-value集合
     *
     * @param cacheName 缓存大类
     */
    @Override
    public Map<Object, Object> getAll(String cacheName) {
        Map<Object, Object> result = new HashMap<Object, Object>();
        String filePath = FilePathUtil.concat(Platforms.USER_HOME, cacheFolderPath, cacheName);
        File file = new File(filePath);
        if (file.isDirectory()) {
            for (File tempItem : Objects.requireNonNull(file.listFiles())) {
                String key = tempItem.getName().substring(0, tempItem.getName().length() - DEFAULT_FILE_EXT.length());
                result.put(key, FileSerializer.deSerialize(tempItem.getPath()));
            }
        }
        return result;
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
        String filePath = getCacheItemFolderPath(cacheName) + File.separator + key + DEFAULT_FILE_EXT;
        return FileSerializer.serialize(filePath, value);
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
        return put(cacheName, key, value, 0L);
    }

    /**
     * 删除缓存信息
     *
     * @param cacheName  缓存大类名称
     * @param key        缓存存储关键字
     * @param expiryTime 缓存引擎不支持
     * @return 缓存删除结果， True：删除成功， False：删除失败
     */
    @Override
    public boolean remove(String cacheName, Object key, long expiryTime) {
        String filePath = FilePathUtil.concat(getCacheItemFolderPath(cacheName), key.toString(), DEFAULT_FILE_EXT);
        File file = new File(filePath);
        return !file.exists() || file.delete();

    }

    /**
     * 删除大类的所有缓存星系
     *
     * @param cacheName 缓存大类名称
     * @return 缓存删除结果， True：删除成功， False：删除失败
     */
    public boolean remove(String cacheName) {
        String filePath = FilePathUtil.concat(Platforms.USER_HOME, cacheFolderPath, cacheName);
        File file = new File(filePath);
        try {
            FileUtil.deleteDir(file);
        } catch (IOException e) {
            throw new SystemException(OpsErrorMessage.MODULE_NAME, e, false, OpsErrorMessage.FILE_IO_ERROR, filePath);
        }
        return true;
    }

    /**
     * 获取大类存放的文件路径，没有则创建对应的文件夹
     *
     * @param cacheName 缓存大类
     * @return 缓存大类文件存放路径
     */
    private String getCacheItemFolderPath(String cacheName) {
        String result = cacheItemFolder.get(cacheName);
        if (result == null) {
            String filePath = FilePathUtil.concat(Platforms.USER_HOME, cacheFolderPath, cacheName);
            try {
                FileUtil.makesureDirExists(filePath);
            } catch (IOException e) {
                throw new SystemException(OpsErrorMessage.MODULE_NAME, e, false, OpsErrorMessage.FILE_IO_ERROR, filePath);
            }
            cacheItemFolder.put(cacheName, filePath);
        }
        return result;
    }
}
