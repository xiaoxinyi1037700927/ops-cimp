package com.sinosoft.ops.cimp.repository.mongodb;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.sinosoft.ops.cimp.repository.mongodb.ex.CannotFindMongoDbResourceById;
import org.bson.conversions.Bson;

import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface MongoDbDao {

    String uploadFromFile(File file) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException;

    String uploadFromFile(File file, Map<String, Object> extendDoc) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException;

    String uploadFileFromStream(String fileName, InputStream is) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException;

    String uploadFileFromStream(String fileName, InputStream is, Map<String, Object> extendDoc) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException;

    void uploadFileFromStream(String id, String fileName, InputStream is, Map<String, Object> extDoc)
            throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException;

    GridFSFile downloadFileToStream(String id, OutputStream os) throws IOException, CannotFindMongoDbResourceById, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException;

    File dowloadToFile(String id, Path path) throws FileNotFoundException, IOException, CannotFindMongoDbResourceById, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException;

    File dowloadToFile(String id, String path) throws FileNotFoundException, IOException, CannotFindMongoDbResourceById, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException;

    void dowloadToFile(String id, File file) throws FileNotFoundException, IOException, CannotFindMongoDbResourceById, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException;

    void deleteFileById(String id);

    GridFSFile findFileById(String id);

    List<GridFSFile> findFileByConditions(Bson filters);

    List<GridFSFile> findFileByConditions(Bson filters, Bson sort);

    List<GridFSFile> findFileByConditions(Bson filters, Bson sort, int limit);

    List<GridFSFile> findFileByConditions(Bson filters, Bson sort, int limit, int skip);

    void updateFromFile(String id, File file) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException;

    void updateFromFile(String id, File file, Map<String, Object> extendDoc) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException;

    void updateFileFromStream(String id, String fileName, InputStream is) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException;

    void updateFileFromStream(String id, String fileName, InputStream is, Map<String, Object> extendDoc)
            throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException;

    List<GridFSFile> findeFileListById(Collection<String> ids);
}
