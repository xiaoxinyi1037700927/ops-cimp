package com.sinosoft.ops.cimp.entity.sys.tag;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "CADRE_TAG")
public class CadreTag implements Serializable {
    private static final long serialVersionUID = 4036507269784967352L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "SUB_ID", length = 36)
    private String subId;

    //干部id
    @Column(name = "EMP_ID", length = 36)
    private String empId;

    @Column(name = "TAG_CATEGORY_ID", length = 36)
    private String tagCategoryId;

    @Column(name = "TAG_ID", length = 36)
    private String tagId;

    @Column(name = "TAG_TYPE", length = 50)
    private String tagType;

    @Column(name = "CREATE_TIME")
    private Date createTime;

    @Column(name = "STATUS", length = 2, columnDefinition = "NUMBER(2) default 0")
    private Integer status;

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getTagCategoryId() {
        return tagCategoryId;
    }

    public void setTagCategoryId(String tagCategoryId) {
        this.tagCategoryId = tagCategoryId;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CadreTag cadreTag = (CadreTag) o;
        return Objects.equals(subId, cadreTag.subId) &&
                Objects.equals(empId, cadreTag.empId) &&
                Objects.equals(tagCategoryId, cadreTag.tagCategoryId) &&
                Objects.equals(tagId, cadreTag.tagId) &&
                Objects.equals(tagType, cadreTag.tagType) &&
                Objects.equals(createTime, cadreTag.createTime) &&
                Objects.equals(status, cadreTag.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subId, empId, tagCategoryId, tagId, tagType, createTime, status);
    }

    @Override
    public String toString() {
        return "CadreTag{" +
                "subId='" + subId + '\'' +
                ", empId='" + empId + '\'' +
                ", tagCategoryId='" + tagCategoryId + '\'' +
                ", tagId='" + tagId + '\'' +
                ", tagType='" + tagType + '\'' +
                ", createTime=" + createTime +
                ", status=" + status +
                '}';
    }
}
