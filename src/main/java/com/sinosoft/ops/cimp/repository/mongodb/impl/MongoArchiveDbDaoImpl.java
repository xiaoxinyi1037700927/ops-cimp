/**
 * @project:     iimp-gradle
 * @title:          MongoArchiveDbDaoImpl.java
 * @copyright: ©2019 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.repository.mongodb.impl;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.sinosoft.ops.cimp.repository.mongodb.MongoArchiveDbDao;
import com.sinosoft.ops.cimp.util.MongoDbUtils;
import org.apache.commons.io.FileUtils;
import org.bson.BsonString;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;
import com.sinosoft.ops.cimp.repository.mongodb.ex.*;
import com.sinosoft.ops.cimp.util.CryptoUtil;

import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * @ClassName: MongoArchiveDbDaoImpl
 * @description: Mongo档案数据访问实现类
 * @author:        Ni
 * @date:            2019年5月17日 下午3:44:16
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Repository("mongoArchiveDbDao")
public class MongoArchiveDbDaoImpl implements MongoArchiveDbDao {
    private static final byte[] pwdBytes = "UZOGbRygA2S1@$Ru".getBytes();

    @Autowired
    private GridFsTemplate mongoGridFsTemplateArchiveDb;
    @Autowired
    private MongoTemplate mongoTemplateArchiveDb;
    
    @Override
    public String uploadFile(File file, String fileMessageDigest) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        String id=MongoDbUtils.generateId();
        File tempFile=null;
        try {
            //加密文件到临时文件
            tempFile = File.createTempFile(UUID.randomUUID().toString(), null);
            try(FileOutputStream fos = new FileOutputStream(tempFile);
                    FileInputStream fis = new FileInputStream(file)){
                CryptoUtil.encryptStreamAes(fis, fos, pwdBytes);
            }
            String fileName=file.getAbsolutePath();
            String contentType = Files.probeContentType(Paths.get(fileName));
            String fileExtension = com.google.common.io.Files.getFileExtension(fileName);
            Document document = new Document("extension", fileExtension)
                    .append("content_type", contentType)
                    .append("realmd5", fileMessageDigest);
            GridFSUploadOptions uploadOptions = new GridFSUploadOptions().metadata(document);
            
            //上传加密文件至MongoDb
            GridFSBucket bucket = GridFSBuckets.create(mongoTemplateArchiveDb.getDb());
            try(FileInputStream fis = new FileInputStream(tempFile)){
                bucket.uploadFromStream(new BsonString(id), fileName, fis, uploadOptions);
            }
        } finally {
            if(tempFile!=null) FileUtils.deleteQuietly(tempFile);
        }
        return id;
    }

    @Override
    public void downloadFileToStream(String id, OutputStream os)
            throws DownloadResourceFromMongoDbError, CannotFindMongoDbResourceById, IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        // 获取文件头信息
        Query q = new Query(Criteria.where("_id").is(id));
        GridFSFile gfsf = mongoGridFsTemplateArchiveDb.findOne(q);
        if (gfsf == null) {
            throw new CannotFindMongoDbResourceById(q);
        }
        File tempFile=null;
        try {
            //下载到临时文件
            GridFSBucket bucket = GridFSBuckets.create(mongoTemplateArchiveDb.getDb());
            tempFile = File.createTempFile(UUID.randomUUID().toString(), null);
            try(FileOutputStream fos = new FileOutputStream(tempFile)){
                bucket.downloadToStream(new BsonString(id), fos);
            }
            //解密文件到输出流
            try(FileInputStream fis = new FileInputStream(tempFile)){
                CryptoUtil.decryptStreamAes (fis, os, pwdBytes);
            }
        } finally {
            if(tempFile!=null) FileUtils.deleteQuietly(tempFile);
        }
    }

    @Override
    public void deleteById(String id) {
        GridFSBucket bucket = GridFSBuckets.create(mongoTemplateArchiveDb.getDb());
        bucket.delete(new BsonString(id));
    }
}
