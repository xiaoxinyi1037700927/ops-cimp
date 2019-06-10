/**
 * @project:     iimp-gradle
 * @title:          MongoDbUtils.java
 * @copyright: ©2019 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.util;

import java.util.UUID;

/**
 * @ClassName: MongoDbUtils
 * @description: MongoDb支持类
 * @author:        Ni
 * @date:            2019年5月17日 下午8:51:02
 * @version        1.0.0
 * @since            JDK 1.7
 */
public class MongoDbUtils {
    /** 
     * 生成MongoDb标识
     * @return 标识
     */
    public static String generateId() {
        return String.format("%s", UUID.randomUUID());
    }
}
