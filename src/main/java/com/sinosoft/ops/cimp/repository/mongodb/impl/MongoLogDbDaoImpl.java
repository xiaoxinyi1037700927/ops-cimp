/**
 * @project:     iimp-gradle
 * @title:          MongoLogDbDaoImpl.java
 * @copyright: ©2019 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.repository.mongodb.impl;

import com.mongodb.MongoException;
import com.sinosoft.ops.cimp.constant.OpsErrorMessage;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.repository.mongodb.MongoLogDbDao;
import org.bson.Document;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.UUID;

/**
 * @ClassName: MongoLogDbDaoImpl
 * @description: Mongo日志数据访问实现类
 * @author:        Ni
 * @date:            2019年5月17日 下午3:26:25
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Repository("mongoLogDbDao")
public class MongoLogDbDaoImpl implements MongoLogDbDao {
    //private static final byte[] pwdBytes = "FO@jUP8r3cOBX0H$".getBytes();
    
    @Autowired
    private MongoTemplate mongoTemplateLogDb;

    @Override
    public UUID save(UUID userId, Date operationTime, String controllerName, String methodName, String methodParameters,
            String requestParameters) throws BusinessException {
        UUID id=UUID.randomUUID();
        LocalDateTime ldt = LocalDateTime.fromDateFields(operationTime);
        
        try {
            mongoTemplateLogDb.getDb().getCollection(String.valueOf(ldt.getYear()))//使用年份作为日志数据集合名称
                .insertOne(new Document()
                    .append("_id", id)
                    .append("userId", userId.toString())
                    .append("operationTime",ldt.toString("yyyy-MM-dd HH:mm:ss.SSS"))
                    .append("controllerName",controllerName)
                    .append("methodName",methodName)
                    .append("methodParameters",methodParameters)
                    .append("requestParameters",requestParameters)
                    );
        }catch(MongoException e) {
            throw new BusinessException(OpsErrorMessage.MODULE_NAME,OpsErrorMessage.ERROR_MESSAGE,"存入Mongo日志库失败");
        }
        return id;
    }    
}
