package com.sinosoft.ops.cimp.entity.archive;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "ARCHIVE_MATERIAL_CATEGORY",
    uniqueConstraints = @UniqueConstraint(columnNames = "CODE"))
public class ArchiveMaterialCategory implements java.io.Serializable {


  private static final long serialVersionUID = -8134738058443888873L;
  private String id;
  private String assignedSn;
  private String name;
  private String code;
  private String parentCode;
  private byte sn;

  public ArchiveMaterialCategory() {}

  public ArchiveMaterialCategory(String id, String assignedSn, String name, String code,
      String parentCode, byte sn) {
    this.id = id;
    this.assignedSn = assignedSn;
    this.name = name;
    this.code = code;
    this.parentCode = parentCode;
    this.sn = sn;
  }

  @Id


  @Column(name = "ID", unique = true, nullable = false, length = 50)
  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }


  @Column(name = "ASSIGNED_SN", nullable = false, length = 10)
  public String getAssignedSn() {
    return this.assignedSn;
  }

  public void setAssignedSn(String assignedSn) {
    this.assignedSn = assignedSn;
  }


  @Column(name = "NAME", nullable = false, length = 100)
  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }


  @Column(name = "CODE", unique = true, nullable = false, length = 10)
  public String getCode() {
    return this.code;
  }

  public void setCode(String code) {
    this.code = code;
  }


  @Column(name = "PARENT_CODE", nullable = false, length = 10)
  public String getParentCode() {
    return this.parentCode;
  }

  public void setParentCode(String parentCode) {
    this.parentCode = parentCode;
  }


  @Column(name = "SN", nullable = false, precision = 2, scale = 0)
  public byte getSn() {
    return this.sn;
  }

  public void setSn(byte sn) {
    this.sn = sn;
  }



}


