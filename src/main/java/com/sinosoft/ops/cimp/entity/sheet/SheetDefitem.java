package com.sinosoft.ops.cimp.entity.sheet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "SHEET_DEFITEM")
public class SheetDefitem implements java.io.Serializable{

  private Integer id;
  private Integer infoSetId;
  private Integer infoItemId;
  private String description;
  private Timestamp createdTime;
  private UUID createdBy;
  private Timestamp lastModifiedTime;
  private UUID lastModifiedBy;
  private Integer ordinal;
  private Byte status;

  public SheetDefitem()
  {

  }

  @Id
  @Column(name = "ID", unique = true, nullable = false)
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @Column(name = "INFO_SET_ID", precision = 8, scale = 0)
  public Integer getInfoSetId() {
    return infoSetId;
  }

  public void setInfoSetId(Integer infoSetId) {
    this.infoSetId = infoSetId;
  }

  @Column(name = "INFO_ITEM_ID", precision = 8, scale = 0)
  public Integer getInfoItemId() {
    return infoItemId;
  }

  public void setInfoItemId(Integer infoItemId) {
    this.infoItemId = infoItemId;
  }

  @Column(name = "DESCRIPTION")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Column(name = "CREATED_BY")
  public UUID getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(UUID createdBy) {
    this.createdBy = createdBy;
  }

  @Column(name = "CREATED_TIME", nullable = false, length = 11)
  public Timestamp getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Timestamp createdTime) {
    this.createdTime = createdTime;
  }

  @Column(name = "LAST_MODIFIED_BY")
  public UUID getLastModifiedBy() {
    return lastModifiedBy;
  }

  public void setLastModifiedBy(UUID lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
  }

  @Column(name = "LAST_MODIFIED_TIME", nullable = false, length = 11)
  public Timestamp getLastModifiedTime() {
    return lastModifiedTime;
  }

  public void setLastModifiedTime(Timestamp lastModifiedTime) {
    this.lastModifiedTime = lastModifiedTime;
  }

  @Column(name = "STATUS", nullable = false, precision = 2, scale = 0)
  public Byte getStatus() {
    return status;
  }

  public void setStatus(Byte status) {
    this.status = status;
  }

  @Column(name = "ORDINAL", precision = 8, scale = 0)
  public Integer getOrdinal() {
    return ordinal;
  }

  public void setOrdinal(Integer ordinal) {
    this.ordinal = ordinal;
  }

}
