package com.sinosoft.ops.cimp.repository.archive.impl;

import com.mongodb.*;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.sinosoft.ops.cimp.entity.archive.ArchiveMaterialFile;
import com.sinosoft.ops.cimp.repository.archive.ArchiveMaterialFileRepository;
import com.sinosoft.ops.cimp.repository.archive.MongoDbDao;
import com.sinosoft.ops.cimp.repository.archive.ex.CannotFindMongoDbResourceById;
import org.apache.commons.io.IOUtils;
import org.bson.BsonString;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.*;

@Repository("mongoDbDao")
public class MongoDbDaoImpl implements MongoDbDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ArchiveMaterialFileRepository archiveMaterialFileRepository;
    @Value("${mongoClientIp}")
    private String host;
    @Value("${mongoClientPort}")
    private Integer Port;
    @Value("${mongoDbName}")
    private String mongoDbName;

    @Override
    public String uploadFromFile(File file) throws IOException {
        return uploadFromFile(file, null);
    }

    @Override
    public String uploadFromFile(File file, Map<String, Object> extendDoc) throws IOException {
        return uploadFileFromStream(file.getName(), new FileInputStream(file), extendDoc);
    }

    @Override
    public String uploadFileFromStream(String fileName, InputStream is) throws IOException {
        return uploadFileFromStream(fileName, is, null);
    }

    @Override
    public String uploadFileFromStream(String fileName, InputStream is, Map<String, Object> extendDoc)
            throws IOException {

        String mongoDbId = genMongoDbId();
        uploadFileFromStream(mongoDbId, fileName, is, extendDoc);

        return mongoDbId;
    }

    @Override
    public void uploadFileFromStream(String mongoDbId, String fileName, InputStream is,
                                     Map<String, Object> extDoc) throws IOException {
        try {
            String contentType = Files.probeContentType(Paths.get(fileName));
            String fileExtension = com.google.common.io.Files.getFileExtension(fileName);
            GridFSBucket gfsb = GridFSBuckets.create(mongoTemplate.getDb());
            Document doc = new Document("extension", fileExtension).append("content_type", contentType);
            System.out.println("docdoc===" + doc);
            if (extDoc != null) {
                doc.putAll(extDoc);
            }
            GridFSUploadOptions uploadOptions = new GridFSUploadOptions().metadata(doc);
            gfsb.uploadFromStream(new BsonString(mongoDbId), fileName, is, uploadOptions);
            ArchiveMaterialFile archiveMaterialFile = new ArchiveMaterialFile();
            archiveMaterialFile.setArchiveMaterialId("1");
            archiveMaterialFile.setPageCount(12);
            archiveMaterialFile.setPageNumber(1);
            archiveMaterialFile.setFileType("11");
            archiveMaterialFile.setFileStorageRef(mongoDbId);
            archiveMaterialFile.setId(UUID.randomUUID().toString());
            archiveMaterialFile.setFileSize(is.available());
            Timestamp ts = new Timestamp(System.currentTimeMillis());
            Date time=new Date();
            String ntime = time.toString();
            try {
                ts = Timestamp.valueOf(ntime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            archiveMaterialFile.setCreatedStamp(ts);
            archiveMaterialFile.setCreatedTxStamp(ts);
            archiveMaterialFile.setFileFormat("jpg");
            archiveMaterialFile.setLastUpdatedStamp(ts);
            archiveMaterialFile.setLastUpdatedTxStamp(ts);
            archiveMaterialFileRepository.save(archiveMaterialFile);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }


    @Override
    public GridFSDBFile downloadFileToStream(String id, OutputStream os)
            throws IOException, CannotFindMongoDbResourceById {
        GridFSDBFile gridFSOne = null;


        try {
            //链接服务器
            Mongo mongo = new Mongo(host, Port);
            //连接数据库
            DB db = mongo.getDB(mongoDbName);
            GridFS gridFS = new GridFS(db);

            BasicDBObject basicDBObject = new BasicDBObject();
            basicDBObject.put("_id", id);
            DBObject query = basicDBObject;
            gridFSOne = gridFS.findOne(query);

            GridFSBucket gfsb = GridFSBuckets.create(mongoTemplate.getDb());
            gfsb.downloadToStream(new BsonString(id), os);
        } finally {
            IOUtils.closeQuietly(os);
        }
        /* GridFSFile gfsf=null;
       try {
            // 获取文件头信息
           BasicDBObject queryWhereId=new BasicDBObject();
           queryWhereId.put("_id",id);
           System.out.println(gfsf);
//            gfsf = gridFsTemplate.findOne(queryWhereId);
//            if (gfsf == null)
//                throw new CannotFindMongoDbResourceById(queryWhereId);

            // 获取多块二进制数据,写入到输出流
            GridFSBucket gfsb = GridFSBuckets.create(mongoTemplate.getDb());
            gfsb.downloadToStream(new BsonString(id), os);
        } finally {
            IOUtils.closeQuietly(os);
        }*/


        return gridFSOne;
    }

    @Override
    public File dowloadToFile(String id, Path path)
            throws IOException, CannotFindMongoDbResourceById {
        return dowloadToFile(id, path.toString());
    }

    @Override
    public File dowloadToFile(String id, String path)
            throws IOException, CannotFindMongoDbResourceById {
        File file = new File(path);
        dowloadToFile(id, file);
        return file;
    }

    @Override
    public void dowloadToFile(String id, File file)
            throws IOException, CannotFindMongoDbResourceById {
        downloadFileToStream(id, new FileOutputStream(file));
    }

    @Override
    public void deleteFileById(String id) {
        GridFSBucket gfsb = GridFSBuckets.create(mongoTemplate.getDb());
        gfsb.delete(new BsonString(id));
    }

    @Override
    public GridFSFile findFileById(String id) {
        Query queryWhereId = new Query(Criteria.where("_id").is(id));
        return mongoTemplate.findOne(queryWhereId, GridFSFile.class);
//        GridFSFile gfsf = gridFsTemplate.findOne(queryWhereId);

//        return gfsf;
    }

    @Override
    public List<GridFSFile> findeFileListById(Collection<String> ids) {
        List<GridFSFile> list = new ArrayList<GridFSFile>();
        Query query = new Query(Criteria.where("_id").in(ids));//select * from ** where _id in (ids);
        GridFSFindIterable iter = mongoTemplate.findOne(query, GridFSFindIterable.class);

//        mongoTemplate.findAll(query).var
//        GridFSFindIterable iter = gridFsTemplate.find(query);
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
        GridFSBucket gfsb = GridFSBuckets.create(mongoTemplate.getDb());
        gfsb.find(filters).sort(sort).limit(limit).skip(skip).into(gfsfs);

        return gfsfs;

    }

    @Override
    public void updateFromFile(String id, File file) throws IOException {
        updateFromFile(id, file, null);
    }

    @Override
    public void updateFromFile(String id, File file, Map<String, Object> extendDoc)
            throws IOException {
        updateFileFromStream(id, file.getName(), new FileInputStream(file), extendDoc);
    }

    @Override
    public void updateFileFromStream(String id, String fileName, InputStream is) throws IOException {
        updateFileFromStream(id, fileName, is, null);
    }

    @Override
    public void updateFileFromStream(String id, String fileName, InputStream is,
                                     Map<String, Object> extendDoc) throws IOException {

        // 查出以前扩展信息
        GridFSFile gfsf = findFileById(id);
        Document doc = gfsf.getMetadata();
        doc.putAll(extendDoc);

        // 删除的
        deleteFileById(id);

        uploadFileFromStream(fileName, is, doc);

    }

    @Override
    public String genMongoDbId() {
        return String.format("%s", UUID.randomUUID());//格式化成字符串
    }
}
