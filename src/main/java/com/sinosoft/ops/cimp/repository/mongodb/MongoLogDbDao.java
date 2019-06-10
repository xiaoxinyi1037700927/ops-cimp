/**
 * @project:     iimp-gradle
 * @title:          MongoLogDbDao.java
 * @copyright: ©2019 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.repository.mongodb;

import com.sinosoft.ops.cimp.exception.BusinessException;

import java.util.Date;
import java.util.UUID;



/**
 * @ClassName: MongoLogDbDao
 * @description: Mongo日志数据库访问接口类
 * @author:        Ni
 * @date:            2019年5月17日 下午3:05:12
 * @version        1.0.0
 * @since            JDK 1.7
 */
public interface MongoLogDbDao {
    /**
     * 保存日志
     * @param userId 用户标识
     * @param operationTime 操作时间
     * @param controllerName 控制器名
     * @param methodName 方法名
     * @param methodParameters 方法参数
     * @param requestParameters 请求参数
     * @return 日志主键
     * @author Ni
     * @date:    2018年5月28日 下午1:38:05
     * @since JDK 1.7
     */
    UUID save(UUID userId, Date operationTime, String controllerName, String methodName, String methodParameters, String requestParameters) throws BusinessException;
}
