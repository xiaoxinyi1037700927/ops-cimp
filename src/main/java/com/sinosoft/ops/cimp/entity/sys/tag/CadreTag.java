package com.sinosoft.ops.cimp.entity.sys.tag;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "CADRE_TAG")
public class CadreTag implements Serializable {
    private static final long serialVersionUID = 4036507269784967352L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "ID", length = 36)
    private String id;

    //干部id
    @Column(name = "EMP_ID", length = 36)
    private String empId;

    @Column(name = "TAG_CATEGORY_ID", length = 36)
    private String tagCategoryId;

    @Column(name = "TAG_ID", length = 36)
    private String tagId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CadreTag cadreTag = (CadreTag) o;
        return Objects.equals(id, cadreTag.id) &&
                Objects.equals(empId, cadreTag.empId) &&
                Objects.equals(tagCategoryId, cadreTag.tagCategoryId) &&
                Objects.equals(tagId, cadreTag.tagId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, empId, tagCategoryId, tagId);
    }

    @Override
    public String toString() {
        return "CadreTag{" +
                "id='" + id + '\'' +
                ", empId='" + empId + '\'' +
                ", tagCategoryId='" + tagCategoryId + '\'' +
                ", tagId='" + tagId + '\'' +
                '}';
    }
}
