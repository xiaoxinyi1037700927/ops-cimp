package com.sinosoft.ops.cimp.entity.archive.gbmodel;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlType(propOrder = {"catalogId", "seqNum", "archieveName", "createDate", "pageCount", "memo",
    "sourceImageFileNames", "highDefinitionImageFileNames"})
public class GbArchiveItem {

  /**
   * 类号
   */
  private String catalogId;

  /**
   * 序号
   */
  private int seqNum;

  /**
   * 材料名称
   */
  private String archieveName;

  /**
   * 材料形成日期
   */
  private String createDate;

  /**
   * 页数
   */
  private int pageCount;

  /**
   * 备注
   */
  private String memo;

  /**
   * 原始图像数据
   */
  private List<String> sourceImageFileNames = new ArrayList<String>();

  /**
   * 高清图像数据
   */
  private List<String> highDefinitionImageFileNames = new ArrayList<String>();

  private Map<String, String> sourceImageFileAbsPaths = new HashMap<String, String>();
  private Map<String, String> highDefinitionImageFileAbsPaths = new HashMap<String, String>();



  public String getCatalogId() {
    return catalogId;
  }

  @XmlElement(name = "类号")
  public void setCatalogId(String catalogId) {
    this.catalogId = catalogId;
  }

  public int getSeqNum() {
    return seqNum;
  }

  @XmlElement(name = "序号")
  public void setSeqNum(int seqNum) {
    this.seqNum = seqNum;
  }

  public String getArchieveName() {
    return archieveName;
  }

  @XmlElement(name = "材料名称")
  public void setArchieveName(String archieveName) {
    this.archieveName = archieveName;
  }

  public String getCreateDate() {
	  if(createDate =="00000" || createDate.trim() == "")
	  {
		  return "19900101";
	  }		  		  
    return createDate;
  }

  @XmlElement(name = "材料形成时间")
  public void setCreateDate(String createDate) {
    this.createDate = createDate;
  }

  public int getPageCount() {
    return pageCount;
  }

  @XmlElement(name = "页数")
  public void setPageCount(int pageCount) {
    this.pageCount = pageCount;
  }

  public String getMemo() {
    return memo;
  }

  @XmlElement(name = "备注")
  public void setMemo(String memo) {
    this.memo = memo;
  }

  public List<String> getSourceImageFileNames() {
    return sourceImageFileNames;
  }

  @XmlElement(name = "原始图像数据")
  public void setSourceImageFileNames(List<String> sourceImageFileNames) {
    this.sourceImageFileNames = sourceImageFileNames;
  }

  public List<String> getHighDefinitionImageFileNames() {
    return highDefinitionImageFileNames;
  }

  @XmlElement(name = "高清图像数据")
  public void setHighDefinitionImageFileNames(List<String> highDefinitionImageFileNames) {
    this.highDefinitionImageFileNames = highDefinitionImageFileNames;
  }

  public Map<String, String> getSourceImageFileAbsPaths() {
    return sourceImageFileAbsPaths;
  }

  @XmlTransient
  public void setSourceImageFileAbsPaths(Map<String, String> sourceImageFileAbsPaths) {
    this.sourceImageFileAbsPaths = sourceImageFileAbsPaths;
  }

  public Map<String, String> getHighDefinitionImageFileAbsPaths() {
    return highDefinitionImageFileAbsPaths;
  }
  @XmlTransient
  public void setHighDefinitionImageFileAbsPaths(Map<String, String> highDefinitionImageFileAbsPaths) {
    this.highDefinitionImageFileAbsPaths = highDefinitionImageFileAbsPaths;
  }

}
