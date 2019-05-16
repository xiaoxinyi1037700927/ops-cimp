package com.sinosoft.ops.cimp.entity.archive.gbmodel;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "数字档案")
public class GbArchive {

  private GbArchiveHeader archieveHeader;

  private List<GbArchiveItem> archieveItems = new ArrayList<GbArchiveItem>();

  public GbArchive() {}

  public GbArchive(GbArchiveHeader archieveHeader) {
    this.setArchieveHeader(archieveHeader);
  }

  public GbArchiveHeader getArchieveHeader() {
    return archieveHeader;
  }

  @XmlElement(name = "人员基本信息")
  public void setArchieveHeader(GbArchiveHeader archieveHeader) {
    this.archieveHeader = archieveHeader;
  }

  public List<GbArchiveItem> getArchieveItems() {
    return archieveItems;
  }

  @XmlElementWrapper(name = "目录信息")
  @XmlElement(name = "档案目录条目")
  public void setArchieveItems(List<GbArchiveItem> archieveItems) {
    this.archieveItems = archieveItems;
  }
}
