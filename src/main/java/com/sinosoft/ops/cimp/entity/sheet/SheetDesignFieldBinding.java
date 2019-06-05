package com.sinosoft.ops.cimp.entity.sheet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "SHEET_DESIGN_FIELD_BINDING")
public class SheetDesignFieldBinding implements Serializable{

  /*** TODO请描述一下这个变量 */
  private static final long serialVersionUID = -7943875007861798898L;
  
  private UUID id;
  private UUID designId;
  private UUID fieldId;
  private Integer startRowNo;
  private Integer endRowNo;
  private Integer startColumnNo;
  private Integer endColumnNo;
  private String sectionNo;

  private String description;
  private java.sql.Timestamp createdTime;
  private UUID createdBy;
  private java.sql.Timestamp lastModifiedTime;
  private UUID lastModifiedBy;
  private Integer ordinal;
  private Byte status;

  public SheetDesignFieldBinding()
  {

  }

  @Id
  @Column(name = "ID", unique = true, nullable = false)
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  @Column(name = "DESIGN_ID")
  public UUID getDesignId() {
    return designId;
  }

  public void setDesignId(UUID designId) {
    this.designId = designId;
  }

  @Column(name = "FIELD_ID")
  public UUID getFieldId() {
    return fieldId;
  }

  public void setFieldId(UUID fieldId) {
    this.fieldId = fieldId;
  }

  @Column(name = "START_ROW_NO")
  public Integer getStartRowNo() {
    return startRowNo;
  }

  public void setStartRowNo(Integer startRowNo) {
    this.startRowNo = startRowNo;
  }

  @Column(name = "END_ROW_NO")
  public Integer getEndRowNo() {
    return endRowNo;
  }

  public void setEndRowNo(Integer endRowNo) {
    this.endRowNo = endRowNo;
  }

  @Column(name = "START_COLUMN_NO")
  public Integer getStartColumnNo() {
    return startColumnNo;
  }

  public void setStartColumnNo(Integer startColumnNo) {
    this.startColumnNo = startColumnNo;
  }

  @Column(name = "END_COLUMN_NO")
  public Integer getEndColumnNo() {
    return endColumnNo;
  }

  public void setEndColumnNo(Integer endColumnNo) {
    this.endColumnNo = endColumnNo;
  }

  @Column(name = "SECTION_NO")
  public String getSectionNo() {
    return sectionNo;
  }

  public void setSectionNo(String sectionNo) {
    this.sectionNo = sectionNo;
  }


  @Column(name = "CREATED_BY")
  public UUID getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(UUID createdBy) {
    this.createdBy = createdBy;
  }

  @Column(name = "CREATED_TIME", nullable = false, length = 11)
  public java.sql.Timestamp getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(java.sql.Timestamp createdTime) {
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
  public java.sql.Timestamp getLastModifiedTime() {
    return lastModifiedTime;
  }

  public void setLastModifiedTime(java.sql.Timestamp lastModifiedTime) {
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
