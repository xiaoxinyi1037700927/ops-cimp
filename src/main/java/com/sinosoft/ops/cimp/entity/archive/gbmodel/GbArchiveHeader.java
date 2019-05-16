package com.sinosoft.ops.cimp.entity.archive.gbmodel;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"name", "sex", "nation", "birthDate", "id"})

public class GbArchiveHeader {

  /**
   * 姓名
   */
  private String name;

  /**
   * 性别
   */
  private String sex;

  /**
   * 民族
   */
  private String nation;

  /**
   * 出生日期
   */
  private String birthDate;

  /**
   * 身份证号
   */
  private String id;

  public String getName() {
    return name;
  }

  @XmlElement(name = "姓名")
  public void setName(String name) {
    this.name = name;
  }

  public String getSex() {
    return sex;
  }

  @XmlElement(name = "性别")
  public void setSex(String sex) {
    this.sex = sex;
  }

  public String getNation() {
    return nation;
  }

  @XmlElement(name = "民族")
  public void setNation(String nation) {
    this.nation = nation;
  }

  public String getBirthDate() {
    return birthDate;
  }

  @XmlElement(name = "出生日期")
  public void setBirthDate(String birthDate) {
    this.birthDate = birthDate;
  }

  public String getId() {
    return id;
  }

  @XmlElement(name = "身份证号")
  public void setId(String id) {
    this.id = id;
  }

}
