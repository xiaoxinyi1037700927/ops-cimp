package com.sinosoft.ops.cimp.repository.archive;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.sinosoft.ops.cimp.repository.archive.ex.CannotFindMongoDbResourceById;
import org.bson.conversions.Bson;

import java.io.*;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface MongoDbDao {

  String uploadFromFile(File file) throws IOException;

  String uploadFromFile(File file, Map<String, Object> extendDoc) throws IOException;

  String uploadFileFromStream(String fileName, InputStream is) throws IOException;

  String uploadFileFromStream(String fileName, InputStream is, Map<String, Object> extendDoc)
      throws IOException;

  void uploadFileFromStream(String id, String fileName, InputStream is, Map<String, Object> extDoc)
      throws IOException;

  GridFSFile downloadFileToStream(String id, OutputStream os)
      throws IOException, CannotFindMongoDbResourceById;

  File dowloadToFile(String id, Path path)
      throws FileNotFoundException, IOException, CannotFindMongoDbResourceById;

  File dowloadToFile(String id, String path)
      throws FileNotFoundException, IOException, CannotFindMongoDbResourceById;

  void dowloadToFile(String id, File file)
      throws FileNotFoundException, IOException, CannotFindMongoDbResourceById;

  void deleteFileById(String id);

  GridFSFile findFileById(String id);

  List<GridFSFile> findFileByConditions(Bson filters);

  List<GridFSFile> findFileByConditions(Bson filters, Bson sort);

  List<GridFSFile> findFileByConditions(Bson filters, Bson sort, int limit);

  List<GridFSFile> findFileByConditions(Bson filters, Bson sort, int limit, int skip);


  void updateFromFile(String id, File file) throws IOException;

  void updateFromFile(String id, File file, Map<String, Object> extendDoc) throws IOException;

  void updateFileFromStream(String id, String fileName, InputStream is) throws IOException;

  void updateFileFromStream(String id, String fileName, InputStream is,
                            Map<String, Object> extendDoc) throws IOException;

  String genMongoDbId();

  List<GridFSFile> findeFileListById(Collection<String> ids);
}
