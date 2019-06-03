/**
 * @Project: IIMP
 * @Title: Config.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.common.model;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.ReloadingFileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: Config
 * @Description: 配置信息
 * @Author: Nil
 * @Date: 2017年8月21日 下午2:17:12
 * @Version 1.0.0
 */
public class Config {
    private static final Logger logger = LoggerFactory.getLogger(Config.class);

    /**
     * 配置文件名
     */
    public static final String CONFIG_FILENAME = "config.properties";
    /**
     * 配置对象
     */
    private static Configuration configuration = null;

    static {
        try {
            configuration = new ReloadingFileBasedConfigurationBuilder<FileBasedConfiguration>((Class<? extends FileBasedConfiguration>) PropertiesConfiguration.class)
                    .configure((new Parameters()).properties()
                            .setFileName(CONFIG_FILENAME)).getConfiguration();
            logger.info("加载系统配置“" + CONFIG_FILENAME + "”成功！");
        } catch (ConfigurationException e) {
            logger.error("加载系统配置“" + CONFIG_FILENAME + "”失败！", e);
        }
    }

    /**
     * 获取配置对象
     *
     * @return 配置对象
     */
    public static Configuration getConfiguration() {
        return configuration;
    }
}
