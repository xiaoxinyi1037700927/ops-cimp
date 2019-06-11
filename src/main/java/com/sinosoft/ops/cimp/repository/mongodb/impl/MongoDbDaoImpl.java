package com.sinosoft.ops.cimp.repository.mongodb.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.crypto.NoSuchPaddingException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bson.BsonString;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.newskysoft.iimp.mongodb.MongoDbUtils;
import com.newskysoft.iimp.mongodb.dao.ex.CannotFindMongoDbResourceById;
import com.newskysoft.util.CryptoUtil;

@Repository("mongoDbDao")
public class MongoDbDaoImpl implements MongoDbDao {
    private static final byte[] pwdBytes = "0123456789ABCDEF".getBytes();
    
    @Autowired
    private GridFsTemplate mongoGridFsTemplateFileDb;
    @Autowired
    private MongoTemplate mongoTemplateFileDb;

    @Override
    public String uploadFromFile(File file) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        return uploadFromFile(file, null);
    }

    @Override
    public String uploadFromFile(File file, Map<String, Object> extendDoc) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        return uploadFileFromStream(file.getName(), new FileInputStream(file), extendDoc);
    }

    @Override
    public String uploadFileFromStream(String fileName, InputStream is) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        return uploadFileFromStream(fileName, is, null);
    }

    @Override
    public String uploadFileFromStream(String fileName, InputStream is, Map<String, Object> extendDoc)
            throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {

        String mongoDbId = MongoDbUtils.generateId();
        uploadFileFromStream(mongoDbId, fileName, is, extendDoc);

        return mongoDbId;
    }

    @Override
    public void uploadFileFromStream(String mongoDbId, String fileName, InputStream is, Map<String, Object> extDoc)
            throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        File tempFile=null;
        try {
            //加密输入流到临时文件
            tempFile = File.createTempFile(UUID.randomUUID().toString(), null);
            try(FileOutputStream fos = new FileOutputStream(tempFile)){
                CryptoUtil.encryptStreamAes(is, fos, pwdBytes);
            }
                
            String contentType = Files.probeContentType(Paths.get(fileName));
            String fileExtension = com.google.common.io.Files.getFileExtension(fileName);
            GridFSBucket gfsb = GridFSBuckets.create(mongoTemplateFileDb.getDb());
            Document doc = new Document("extension", fileExtension).append("content_type", contentType);
            if (extDoc != null) {
                doc.putAll(extDoc);
            }
            GridFSUploadOptions uploadOptions = new GridFSUploadOptions().metadata(doc);
            
            //将加密文件上传至MongoDb
            try(FileInputStream fis = new FileInputStream(tempFile)){
                gfsb.uploadFromStream(new BsonString(mongoDbId), fileName, fis, uploadOptions);
            }
        } finally {
            if(tempFile!=null) FileUtils.deleteQuietly(tempFile);
            IOUtils.closeQuietly(is);
        }

    }

    @Override
    public GridFSFile downloadFileToStream(String id, OutputStream os)
            throws IOException, CannotFindMongoDbResourceById, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {

        GridFSFile gfsf = null;
        try {
            // 获取文件头信息
            Query queryWhereId = new Query(Criteria.where("_id").is(id));

            gfsf = mongoGridFsTemplateFileDb.findOne(queryWhereId);
            if (gfsf == null)
                throw new CannotFindMongoDbResourceById(queryWhereId);

            // 获取多块二进制数据,写入到输出流
            GridFSBucket gfsb = GridFSBuckets.create(mongoTemplateFileDb.getDb());
            
            File tempFile=null;
            try {
                //下载到临时文件
                tempFile = File.createTempFile(UUID.randomUUID().toString(), null);
                try(FileOutputStream fos = new FileOutputStream(tempFile)){
                    gfsb.downloadToStream(new BsonString(id), fos);
                }
                //解密文件到输出流
                try(FileInputStream fis = new FileInputStream(tempFile)){
                    CryptoUtil.decryptStreamAes (fis, os, pwdBytes);
                }
            } finally {
                if(tempFile!=null) FileUtils.deleteQuietly(tempFile);
            }
        } finally {
            IOUtils.closeQuietly(os);
        }

        return gfsf;
    }

    @Override
    public File dowloadToFile(String id, Path path) throws IOException, CannotFindMongoDbResourceById, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        return dowloadToFile(id, path.toString());
    }

    @Override
    public File dowloadToFile(String id, String path) throws IOException, CannotFindMongoDbResourceById, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        File file = new File(path);
        dowloadToFile(id, file);
        return file;
    }

    @Override
    public void dowloadToFile(String id, File file) throws IOException, CannotFindMongoDbResourceById, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        downloadFileToStream(id, new FileOutputStream(file));
    }

    @Override
    public void deleteFileById(String id) {
        GridFSBucket gfsb = GridFSBuckets.create(mongoTemplateFileDb.getDb());
        gfsb.delete(new BsonString(id));
    }

    @Override
    public GridFSFile findFileById(String id) {
        Query queryWhereId = new Query(Criteria.where("_id").is(id));

        GridFSFile gfsf = mongoGridFsTemplateFileDb.findOne(queryWhereId);

        return gfsf;
    }

    @Override
    public List<GridFSFile> findeFileListById(Collection<String> ids) {
        List<GridFSFile> list = new ArrayList<GridFSFile>();
        Query query = new Query(Criteria.where("_id").in(ids));
        GridFSFindIterable iter = mongoGridFsTemplateFileDb.find(query);
        MongoCursor<GridFSFile> mc = iter.iterator();
        while (mc.hasNext()) {
            GridFSFile gsf = mc.next();
            list.add(gsf);
        }
        return list;
    }

    @Override
    public List<GridFSFile> findFileByConditions(Bson filters) {
        return findFileByConditions(filters, null);
    }

    @Override
    public List<GridFSFile> findFileByConditions(Bson filters, Bson sort) {
        return findFileByConditions(filters, sort, 0);
    }

    @Override
    public List<GridFSFile> findFileByConditions(Bson filters, Bson sort, int limit) {
        return findFileByConditions(filters, sort, limit, 0);
    }

    @Override
    public List<GridFSFile> findFileByConditions(Bson filters, Bson sort, int limit, int skip) {
        List<GridFSFile> gfsfs = new ArrayList<GridFSFile>();
        GridFSBucket gfsb = GridFSBuckets.create(mongoTemplateFileDb.getDb());
        gfsb.find(filters).sort(sort).limit(limit).skip(skip).into(gfsfs);

        return gfsfs;

    }

    @Override
    public void updateFromFile(String id, File file) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        updateFromFile(id, file, null);
    }

    @Override
    public void updateFromFile(String id, File file, Map<String, Object> extendDoc) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        updateFileFromStream(id, file.getName(), new FileInputStream(file), extendDoc);
    }

    @Override
    public void updateFileFromStream(String id, String fileName, InputStream is) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        updateFileFromStream(id, fileName, is, null);
    }

    @Override
    public void updateFileFromStream(String id, String fileName, InputStream is, Map<String, Object> extendDoc)
            throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        try {
            // 删除的
            deleteFileById(id);
        }catch(Exception e) {
            //如果指定标识的数据不存在，忽略所引发的异常
        }

        uploadFileFromStream(fileName, is, extendDoc);
    }
}
