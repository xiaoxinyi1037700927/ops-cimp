/**
 * @project:     iimp-gradle
 * @title:          MongoArchiveDbDao.java
 * @copyright: ©2019 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.repository.mongodb;

import com.sinosoft.ops.cimp.repository.mongodb.ex.CannotFindMongoDbResourceById;
import com.sinosoft.ops.cimp.repository.mongodb.ex.DownloadResourceFromMongoDbError;

import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName: MongoArchiveDbDao
 * @description: Mongo档案数据库访问接口类
 * @author:        Ni
 * @date:            2019年5月17日 下午3:38:53
 * @version        1.0.0
 * @since            JDK 1.7
 */
public interface MongoArchiveDbDao {
    /** 
     * 将文件上传至MongoDb
     * @param file 文件
     * @param fileMessageDigest 文件数字摘要
     * @return 标识
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @author Ni
     * @date:    2019年5月17日 下午9:07:36
     * @since JDK 1.7
     */
    String uploadFile(File file, String fileMessageDigest) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException;
    
    /**
     * 下载指定标识的文件到输出流
     * @param id MongoDB 唯一主键
     * @param os 输出流
     * @throws DownloadResourceFromMongoDbError 下载异常，详细堆栈见系统日志
     * @throws CannotFindMongoDbResourceById MongoDB 主键不存在
     * @throws IOException 
     * @throws NoSuchPaddingException 
     * @throws NoSuchAlgorithmException 
     * @throws InvalidKeyException 
     */
    void downloadFileToStream(String id, OutputStream os) throws DownloadResourceFromMongoDbError, CannotFindMongoDbResourceById, IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException;
    
    /**
     * MongoDB 唯一主键删除文件
     * 
     * @param id MongoDB 唯一主键
     */
    void deleteById(String id);
}
