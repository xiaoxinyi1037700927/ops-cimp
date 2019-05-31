package com.sinosoft.ops.cimp.service.archive.impl;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.gridfs.GridFSDBFile;
import com.sinosoft.ops.cimp.repository.archive.MongoDbDao;
import com.sinosoft.ops.cimp.repository.archive.ex.CannotFindMongoDbResourceById;
import com.sinosoft.ops.cimp.repository.archive.ex.DownloadResourceFromMongoDbError;
import com.sinosoft.ops.cimp.repository.archive.ex.UploadResourceToMongoDbError;
import com.sinosoft.ops.cimp.service.archive.MongoDbService;
import com.sinosoft.ops.cimp.service.archive.ex.UpdateMongoDbResourceError;
import com.sinosoft.ops.cimp.util.CryptoUtil;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("mongoDbService")
public class MongoDbServiceImpl implements MongoDbService {
    private static final Logger logger = LoggerFactory.getLogger(MongoDbServiceImpl.class);

    private static final byte[] pwdBytes = "0123456789ABCDEF".getBytes();

    @Resource
    private MongoDbDao mongoDbDao;

    public String uploadFileEncryptWithAES(File file) throws UploadResourceToMongoDbError {
        return uploadFileEncryptWithAES(file, null);
    }

    @Override
    public String uploadFileEncryptWithAES(File file, Map<String, Object> extendDoc)
            throws UploadResourceToMongoDbError {
        String mongoId = null;

        try {

            FileInputStream fis = new FileInputStream(file);
            mongoId = uploadFileFromStreamEncryptWithAES(file.getName(), fis, extendDoc);

        } catch (IOException e) {

            logger.error("创建文件流失败", e);
            throw new UploadResourceToMongoDbError(
                    String.format("create file input stream error. file path:%s", file.getPath()));
        }
        return mongoId;
    }

    @Override
    public String uploadFileFromStreamEncryptWithAES(String fileName, InputStream is)
            throws UploadResourceToMongoDbError {
        return uploadFileFromStreamEncryptWithAES(fileName, is, null);
    }

    @Override
    public String uploadFileFromStreamEncryptWithAES(String fileName, InputStream is,
                                                     Map<String, Object> extendDoc) throws UploadResourceToMongoDbError {

        String mongoDbId = mongoDbDao.genMongoDbId();
        uploadFileFromStreamEncryptWithAES(mongoDbId, fileName, is, extendDoc);

        return mongoDbId;
    }


    @Override
    public void uploadFileFromStreamEncryptAES(String id, String fileName, InputStream is){

        ByteArrayInputStream bais = null;
        Map<String, Object> extendDoc=null;

        try {
            System.out.println("isis==" + is);
            byte[] bytes = IOUtils.toByteArray(is);
            byte[] encryptedBytes = CryptoUtil.encryptAes(bytes, pwdBytes);//使用加密工具将流加密
            bais = new ByteArrayInputStream(encryptedBytes);
            // 原始md5
            MessageDigest md = MessageDigest.getInstance("MD5");//MessageDigest类用于为应用程序提供信息摘要算法的功能，如 MD5 或 SHA 算法。简单点说就是用于生成散列码。信息摘要是安全的单向哈希函数，它接收任意大小的数据，输出固定长度的哈希值。
            byte[] thedigest = md.digest(bytes);
            String realmd5 = String.valueOf(Hex.encodeHex(thedigest)); //hex 16进制编码解码工具   test("白");//编码前【白】编码后【E799BD】解码后【白】

            if (extendDoc == null || extendDoc.isEmpty()) {
                extendDoc = new HashMap<String, Object>();
            }
            extendDoc.put("realmd5", realmd5);

            mongoDbDao.uploadFileFromStream(id, fileName, bais, extendDoc);

        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
                | IllegalBlockSizeException | BadPaddingException e) {
            logger.error("上传文件加密失败", e);
        } finally {
            IOUtils.closeQuietly(bais);
            IOUtils.closeQuietly(is);
        }
    }

    @Override
    public String genMongoDbId() {
        return mongoDbDao.genMongoDbId();
    }

    @Override
    public void uploadFileFromStreamEncryptWithAES(String id, String fileName, InputStream is,
                                                   Map<String, Object> extendDoc) throws UploadResourceToMongoDbError {
        ByteArrayInputStream bais = null;

        try {
            System.out.println("isis==" + is);
            byte[] bytes = IOUtils.toByteArray(is);
            byte[] encryptedBytes = CryptoUtil.encryptAes(bytes, pwdBytes);//使用加密工具将流加密
            bais = new ByteArrayInputStream(encryptedBytes);
            // 原始md5
            MessageDigest md = MessageDigest.getInstance("MD5");//MessageDigest类用于为应用程序提供信息摘要算法的功能，如 MD5 或 SHA 算法。简单点说就是用于生成散列码。信息摘要是安全的单向哈希函数，它接收任意大小的数据，输出固定长度的哈希值。
            byte[] thedigest = md.digest(bytes);
            String realmd5 = String.valueOf(Hex.encodeHex(thedigest)); //hex 16进制编码解码工具   test("白");//编码前【白】编码后【E799BD】解码后【白】

            if (extendDoc == null || extendDoc.isEmpty()) {
                extendDoc = new HashMap<String, Object>();
            }
            extendDoc.put("realmd5", realmd5);

            mongoDbDao.uploadFileFromStream(id, fileName, bais, extendDoc);

        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
                | IllegalBlockSizeException | BadPaddingException e) {
            logger.error("上传文件加密失败", e);
            throw new UploadResourceToMongoDbError();
        } finally {
            IOUtils.closeQuietly(bais);
            IOUtils.closeQuietly(is);
        }
    }

    @Override
    public byte[] downloadFileToStreamDecryptWithAES(String id)
            throws DownloadResourceFromMongoDbError, CannotFindMongoDbResourceById {

        ByteArrayOutputStream baos = null;

        try {

            baos = new ByteArrayOutputStream();
            GridFSDBFile gridFSDBFile = mongoDbDao.downloadFileToStream(id, baos);
            InputStream inputStream = gridFSDBFile.getInputStream();
            byte [] bytes=IOUtils.toByteArray(inputStream);
            byte[] decryptedBytes = CryptoUtil.decryptAes(bytes, pwdBytes);
            // TODO MD5 验证
            return decryptedBytes;
        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
                | IllegalBlockSizeException | BadPaddingException e) {
            logger.error("下载文件解密失败", e);
            throw new DownloadResourceFromMongoDbError();
        } finally {
            IOUtils.closeQuietly(baos);
        }
    }

    @Override
    public File downloadToFileDecryptWithAES(String id, String path)
            throws DownloadResourceFromMongoDbError, CannotFindMongoDbResourceById {

        File file = new File(path);
        downloadToFileDecryptWithAES(id, file);
        return file;
    }

    @Override
    public File downloadToFileDecryptWithAES(String id, Path path)
            throws DownloadResourceFromMongoDbError, CannotFindMongoDbResourceById {
        File file = path.toFile();
        downloadToFileDecryptWithAES(id, file);
        return file;
    }

    @Override
    public byte[] downloadToFileDecryptWithAES(String id)
            throws DownloadResourceFromMongoDbError, CannotFindMongoDbResourceById {
            byte[] bytes = downloadFileToStreamDecryptWithAES(id);
            return bytes;

    }

    @Override
    public void downloadToFileDecryptWithAES(String id, File file) throws DownloadResourceFromMongoDbError, CannotFindMongoDbResourceById {

    }

    @Override
    public void deleteFileById(String id) {
        mongoDbDao.deleteFileById(id);
    }

    @Override
    public GridFSFile findFileById(String id) {
        return mongoDbDao.findFileById(id);
    }

    @Override
    public List<GridFSFile> findeFileListById(Collection<String> ids) {
        return mongoDbDao.findeFileListById(ids);
    }

    @Override
    public List<GridFSFile> findFileByConditions(Bson filters) {
        return mongoDbDao.findFileByConditions(filters);
    }

    @Override
    public List<GridFSFile> findFileByConditions(Bson filters, Bson sort) {
        return mongoDbDao.findFileByConditions(filters, sort);
    }

    @Override
    public List<GridFSFile> findFileByConditions(Bson filters, Bson sort, int limit) {
        return mongoDbDao.findFileByConditions(filters, sort, limit);
    }

    @Override
    public List<GridFSFile> findFileByConditions(Bson filters, Bson sort, int limit, int skip) {
        return mongoDbDao.findFileByConditions(filters, sort, limit, skip);
    }


    @Override
    public void updateFromFileWithAES(String id, File file) throws UpdateMongoDbResourceError {
        updateFromFileWithAES(id, file, null);

    }

    @Override
    public void updateFromFileWithAES(String id, File file, Map<String, Object> extendDoc)
            throws UpdateMongoDbResourceError {
        try {
            updateFileFromStreamWithAES(id, file.getName(), new FileInputStream(file), extendDoc);
        } catch (FileNotFoundException e) {
            logger.error("创建文件输入流失败", e);
            throw new UpdateMongoDbResourceError(
                    String.format("create file input stream error. file path:%s", file.getPath()));
        }

    }

    @Override
    public void updateFileFromStreamWithAES(String id, String fileName, InputStream is)
            throws UpdateMongoDbResourceError {
        updateFileFromStreamWithAES(id, fileName, is, null);

    }

    @Override
    public void updateFileFromStreamWithAES(String id, String fileName, InputStream is,
                                            Map<String, Object> extendDoc) throws UpdateMongoDbResourceError {
        try {

            // 查出以前扩展信息
            GridFSFile gfsf = mongoDbDao.findFileById(id);
            Document doc = null;
            if (extendDoc != null) {
                doc = gfsf.getMetadata();
                doc.putAll(extendDoc);
            }

            // 删除的
            mongoDbDao.deleteFileById(id);
            uploadFileFromStreamEncryptWithAES(id, fileName, is, doc);

        } catch (UploadResourceToMongoDbError e) {
            logger.error("更新MongoDB资源异常", e);
            throw new UpdateMongoDbResourceError();
        }
    }

}
