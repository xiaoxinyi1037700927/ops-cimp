package com.sinosoft.ops.cimp.entity.archive;


import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ARCHIVE_MATERIAL_FILE")
public class ArchiveMaterialFile implements java.io.Serializable {


  private static final long serialVersionUID = 37110476254023831L;
  private String id;
  private String archiveMaterialId;
  private int pageCount;
  private int pageNumber;
  private String fileType;
  private String fileFormat;
  private long fileSize;
  private String fileStorageRef;
  private Timestamp createdStamp;
  private Timestamp createdTxStamp;
  private Timestamp lastUpdatedStamp;
  private Timestamp lastUpdatedTxStamp;

  public ArchiveMaterialFile() {}


  public ArchiveMaterialFile(String id, String archiveMaterialId, int pageCount, int pageNumber,
      String fileType, long fileSize, String fileStorageRef) {
    this.id = id;
    this.archiveMaterialId = archiveMaterialId;
    this.pageCount = pageCount;
    this.pageNumber = pageNumber;
    this.fileType = fileType;
    this.fileSize = fileSize;
    this.fileStorageRef = fileStorageRef;
  }

  public ArchiveMaterialFile(String id, String archiveMaterialId, int pageCount, int pageNumber,
      String fileType, String fileFormat, long fileSize, String fileStorageRef,
      Timestamp createdStamp, Timestamp createdTxStamp, Timestamp lastUpdatedStamp,
      Timestamp lastUpdatedTxStamp) {
    this.id = id;
    this.archiveMaterialId = archiveMaterialId;
    this.pageCount = pageCount;
    this.pageNumber = pageNumber;
    this.fileType = fileType;
    this.fileFormat = fileFormat;
    this.fileSize = fileSize;
    this.fileStorageRef = fileStorageRef;
    this.createdStamp = createdStamp;
    this.createdTxStamp = createdTxStamp;
    this.lastUpdatedStamp = lastUpdatedStamp;
    this.lastUpdatedTxStamp = lastUpdatedTxStamp;
  }

  @Id


  @Column(name = "ID", unique = true, nullable = false, length = 50)
  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }


  @Column(name = "ARCHIVE_MATERIAL_ID", nullable = false, length = 50)
  public String getArchiveMaterialId() {
    return this.archiveMaterialId;
  }

  public void setArchiveMaterialId(String archiveMaterialId) {
    this.archiveMaterialId = archiveMaterialId;
  }


  @Column(name = "PAGE_COUNT", nullable = false, precision = 5, scale = 0)
  public int getPageCount() {
    return this.pageCount;
  }

  public void setPageCount(int pageCount) {
    this.pageCount = pageCount;
  }


  @Column(name = "PAGE_NUMBER", nullable = false, precision = 5, scale = 0)
  public int getPageNumber() {
    return this.pageNumber;
  }

  public void setPageNumber(int pageNumber) {
    this.pageNumber = pageNumber;
  }


  @Column(name = "FILE_TYPE", nullable = false, length = 50)
  public String getFileType() {
    return this.fileType;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }


  @Column(name = "FILE_FORMAT", length = 10)
  public String getFileFormat() {
    return this.fileFormat;
  }

  public void setFileFormat(String fileFormat) {
    this.fileFormat = fileFormat;
  }


  @Column(name = "FILE_SIZE", nullable = false, precision = 18, scale = 0)
  public long getFileSize() {
    return this.fileSize;
  }

  public void setFileSize(long fileSize) {
    this.fileSize = fileSize;
  }


  @Column(name = "FILE_STORAGE_REF", nullable = false, length = 200)
  public String getFileStorageRef() {
    return this.fileStorageRef;
  }

  public void setFileStorageRef(String fileStorageRef) {
    this.fileStorageRef = fileStorageRef;
  }


  @Column(name = "CREATED_STAMP")
  public Timestamp getCreatedStamp() {
    return this.createdStamp;
  }

  public void setCreatedStamp(Timestamp createdStamp) {
    this.createdStamp = createdStamp;
  }


  @Column(name = "CREATED_TX_STAMP")
  public Timestamp getCreatedTxStamp() {
    return this.createdTxStamp;
  }

  public void setCreatedTxStamp(Timestamp createdTxStamp) {
    this.createdTxStamp = createdTxStamp;
  }


  @Column(name = "LAST_UPDATED_STAMP")
  public Timestamp getLastUpdatedStamp() {
    return this.lastUpdatedStamp;
  }

  public void setLastUpdatedStamp(Timestamp lastUpdatedStamp) {
    this.lastUpdatedStamp = lastUpdatedStamp;
  }


  @Column(name = "LAST_UPDATED_TX_STAMP")
  public Timestamp getLastUpdatedTxStamp() {
    return this.lastUpdatedTxStamp;
  }

  public void setLastUpdatedTxStamp(Timestamp lastUpdatedTxStamp) {
    this.lastUpdatedTxStamp = lastUpdatedTxStamp;
  }



}


