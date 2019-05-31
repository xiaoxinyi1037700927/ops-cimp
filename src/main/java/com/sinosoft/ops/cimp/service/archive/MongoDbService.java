package com.sinosoft.ops.cimp.service.archive;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.sinosoft.ops.cimp.repository.archive.ex.CannotFindMongoDbResourceById;
import com.sinosoft.ops.cimp.repository.archive.ex.DownloadResourceFromMongoDbError;
import com.sinosoft.ops.cimp.repository.archive.ex.UploadResourceToMongoDbError;
import com.sinosoft.ops.cimp.service.archive.ex.UpdateMongoDbResourceError;
import org.bson.conversions.Bson;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @version 0.0.1
 * @classname: MongoDbService
 * @description: 访问MongoDb服务接口:1.定义了文件上传加密、解密等接口
 * @author: PIG3ON1990
 * @date: 2017年10月27日 下午7:25:33
 */
public interface MongoDbService {

    /**
     * 上传文件到MongoDB并使用AES算法加密
     *
     * @param file 要上传的文件
     * @return MongoDB 主键 UUID.randomUUID()+文件后缀名 eg：3b9a313b-4139-451c-8401-a0a26ce27eab.zip
     */
    String uploadFileEncryptWithAES(File file) throws UploadResourceToMongoDbError;

    /**
     * 上传文件到MongoDB并使用AES算法加密
     *
     * @param file      要上传的文件
     * @param extendDoc 扩展文档Key Value
     * @return MongoDB 主键 UUID.randomUUID()+文件后缀名 eg：3b9a313b-4139-451c-8401-a0a26ce27eab.zip
     * @throws UploadResourceToMongoDbError 上传失败异常
     */
    String uploadFileEncryptWithAES(File file, Map<String, Object> extendDoc)
            throws UploadResourceToMongoDbError;


    String uploadFileFromStreamEncryptWithAES(String fileName, InputStream is)
            throws UploadResourceToMongoDbError;

    String uploadFileFromStreamEncryptWithAES(String fileName, InputStream is,
                                              Map<String, Object> extendDoc) throws UploadResourceToMongoDbError;

    void uploadFileFromStreamEncryptWithAES(String id, String fileName, InputStream is,
                                            Map<String, Object> extendDoc) throws UploadResourceToMongoDbError;

    /**
     * 下载文件到输出流,并使用AES算法解密
     *
     * @param id MongoDB 唯一主键
     * @throws DownloadResourceFromMongoDbError 下载异常，详细堆栈见系统日志
     * @throws CannotFindMongoDbResourceById    MongoDB 主键不存在
     */
    byte[] downloadFileToStreamDecryptWithAES(String id)
            throws DownloadResourceFromMongoDbError, CannotFindMongoDbResourceById;

    /**
     * 下载到指定文件路径,并使用AES算法解密
     *
     * @param id   MongoDB 唯一主键
     * @param path 指定文件路径
     * @return 文件对象
     * @throws DownloadResourceFromMongoDbError 下载异常，详细堆栈见系统日志
     * @throws CannotFindMongoDbResourceById    MongoDB 主键不存在
     */
    File downloadToFileDecryptWithAES(String id, String path)
            throws DownloadResourceFromMongoDbError, CannotFindMongoDbResourceById;

    /**
     * 下载到指定文件路径,并使用AES算法解密
     *
     * @param id   MongoDB 唯一主键
     * @param path 指定文件路径
     * @return 文件对象
     * @throws DownloadResourceFromMongoDbError 下载异常，详细堆栈见系统日志
     * @throws CannotFindMongoDbResourceById    MongoDB 主键不存在
     */
    File downloadToFileDecryptWithAES(String id, Path path)
            throws DownloadResourceFromMongoDbError, CannotFindMongoDbResourceById;

    /**
     * 下载到指定文件路径,并使用AES算法解密
     *
     * @param id   MongoDB 唯一主键
     * @return 文件对象
     * @throws DownloadResourceFromMongoDbError 下载异常，详细堆栈见系统日志
     * @throws CannotFindMongoDbResourceById    MongoDB 主键不存在
     */
    byte[] downloadToFileDecryptWithAES(String id)
            throws DownloadResourceFromMongoDbError, CannotFindMongoDbResourceById;
    /**
     * 下载到指定文件,并使用AES算法解密
     *
     * @param id   MongoDB 唯一主键
     * @param file 文件对象
     * @throws DownloadResourceFromMongoDbError 下载异常，详细堆栈见系统日志
     * @throws CannotFindMongoDbResourceById    MongoDB 主键不存在
     */
    void downloadToFileDecryptWithAES(String id, File file)
            throws DownloadResourceFromMongoDbError, CannotFindMongoDbResourceById;

    /**
     * MongoDB 唯一主键删除文件
     *
     * @param id MongoDB 唯一主键
     */
    void deleteFileById(String id);

    /**
     * MongoDB 唯一ID
     *
     * @param id
     * @return 匹配到的文件
     */
    GridFSFile findFileById(String id);

    /**
     * 根据filters查询文件元数据集合
     *
     * @param filters Bson对象的查询条件
     * @return
     */
    List<GridFSFile> findFileByConditions(Bson filters);

    /**
     * 根据filters查询文件元数据集合，并根据sort排序。
     *
     * @param filters Bson对象的查询条件
     * @param sort    Bson对象的排序规则
     */
    List<GridFSFile> findFileByConditions(Bson filters, Bson sort);

    /**
     * 根据filters查询文件元数据集合，并根据sort排序。
     *
     * @param filters Bson对象的查询条件
     * @param sort    Bson对象的排序规则
     * @param limit   查询限制条数
     */
    List<GridFSFile> findFileByConditions(Bson filters, Bson sort, int limit);

    /**
     * 根据filters查询文件元数据集合，并根据sort排序。
     *
     * @param filters Bson对象的查询条件
     * @param sort    Bson对象的排序规则
     * @param limit   查询限制条数
     * @param skip    跳过数据条数
     */
    List<GridFSFile> findFileByConditions(Bson filters, Bson sort, int limit, int skip);


    void updateFromFileWithAES(String id, File file) throws UpdateMongoDbResourceError;

    void updateFromFileWithAES(String id, File file, Map<String, Object> extendDoc)
            throws UpdateMongoDbResourceError;

    void updateFileFromStreamWithAES(String id, String fileName, InputStream is)
            throws UpdateMongoDbResourceError;

    void updateFileFromStreamWithAES(String id, String fileName, InputStream is,
                                     Map<String, Object> extendDoc) throws UpdateMongoDbResourceError;

    List<GridFSFile> findeFileListById(Collection<String> ids);

    void uploadFileFromStreamEncryptAES(String s, String s1, InputStream inputStream);

    String genMongoDbId();
}
