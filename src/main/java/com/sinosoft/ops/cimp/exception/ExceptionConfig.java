package com.sinosoft.ops.cimp.exception;

import com.google.common.base.Charsets;
import com.sinosoft.ops.cimp.cache.CacheManager;
import com.sinosoft.ops.cimp.constant.OpsErrorMessage;
import com.vip.vjtools.vjkit.base.ObjectUtil;
import com.vip.vjtools.vjkit.base.PropertiesUtil;
import com.vip.vjtools.vjkit.io.URLResourceUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.Properties;

public class ExceptionConfig {

    private static final String PROPERTIES_FILE_CACHE = "SYS_CONFIG_PROPERTIES_CACHE";
    private static final String ERROR_MESSAGE = "message";
    private static final String PREFIX = "classpath:";
    private static final String PROPERTIES_EXTENSION = ".properties";
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionConfig.class);

    /**
     * 格式化错误信息
     *
     * @param modelName 错误文件名称
     * @param code      错误代码
     * @param args      错误信息参数
     * @return 格式化替换后的错误信息描述
     */
    public static String mm(String modelName, String code, String... args) {
        return mm(modelName, true, code, args);
    }

    /**
     * 格式化错误信息
     *
     * @param moduleName 错误文件名称
     * @param code       错误代码
     * @param args       错误信息参数
     * @return 格式化替换后的错误信息描述
     */
    public static String mm(String moduleName, boolean cacheMessage, String code, String... args) {
        String message;
        if (StringUtils.isEmpty(moduleName)) {
            String propertiesFileName = PREFIX + ERROR_MESSAGE + PROPERTIES_EXTENSION;
            message = readString(propertiesFileName, code, cacheMessage);
        } else {
            String propertiesFileName = PREFIX + moduleName + "-" + ERROR_MESSAGE + PROPERTIES_EXTENSION;
            message = readString(propertiesFileName, code, cacheMessage);
        }
        if (message == null) {
            return MessageFormat.format(OpsErrorMessage.NOT_FOUND_MESSAGE, moduleName);
        }
        if (args != null) {
            Object[] params = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                params[i] = ObjectUtil.toPrettyString(args[i]);
            }
            message = MessageFormat.format(message, params);
        }
        return message;
    }

    /**
     * 根据消息模板格式化输出日志
     *
     * @param message 错误信息,{d}进行变量占位
     * @param args    变量参数
     * @return 输出替换后的结果
     */
    public static String formartMessage(String message, Object... args) {
        return MessageFormat.format(message, args);
    }


    /////////////////扩展PropertiesUtil方法//////////////////

    public static String readString(String fileName, String propertyName, boolean cacheFlag) {
        Properties properties = loadFromFile(fileName, cacheFlag);
        if (properties == null || StringUtils.isEmpty(properties.getProperty(propertyName))) {
            properties = loadFromFile(fileName, false);
        }
        return PropertiesUtil.getString(properties, propertyName, "");
    }


    /////////// 加载Properties////////

    /**
     * 缓存加载的配置文件
     */
    public static Properties loadFromFile(String generalPath, boolean cacheFlag) {
        Properties properties;
        if (cacheFlag) {
            Properties cacheProperties = CacheManager.getInstance().get(PROPERTIES_FILE_CACHE, generalPath);
            if (Objects.isNull(cacheProperties)) {
                Properties loadProperties = loadFromFile(generalPath);
                properties = loadProperties;
                CacheManager.getInstance().put(PROPERTIES_FILE_CACHE, generalPath, loadProperties);
            } else {
                properties = cacheProperties;
            }
        } else {
            properties = loadFromFile(generalPath);
        }
        return properties;
    }

    public static Properties loadFromFile(String generalPath) {
        Properties p = new Properties();
        try (Reader reader = new InputStreamReader(URLResourceUtil.asStream(generalPath), Charsets.UTF_8)) {
            p.load(reader);
        } catch (IOException e) {
            LOGGER.warn("Load property from " + generalPath + " failed", e);
        }
        return p;
    }
}
