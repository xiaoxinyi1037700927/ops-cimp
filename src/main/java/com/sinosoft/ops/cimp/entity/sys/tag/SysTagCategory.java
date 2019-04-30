package com.sinosoft.ops.cimp.entity.sys.tag;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "SYS_TAG_CATEGORY")
public class SysTagCategory implements Serializable {
    private static final long serialVersionUID = -1249114063352615591L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "ID", length = 36)
    private String id;

    @Column(name = "TAG_CATEGORY_NAME", length = 100)
    private String tagCategoryName;

    @Column(name = "TAG_CATEGORY_SORT", length = 13)
    private Integer tagCategorySort;

    @Column(name = "TAG_MODEL", length = 50)
    private String tagModel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTagCategoryName() {
        return tagCategoryName;
    }

    public void setTagCategoryName(String tagCategoryName) {
        this.tagCategoryName = tagCategoryName;
    }

    public Integer getTagCategorySort() {
        return tagCategorySort;
    }

    public void setTagCategorySort(Integer tagCategorySort) {
        this.tagCategorySort = tagCategorySort;
    }

    public String getTagModel() {
        return tagModel;
    }

    public void setTagModel(String tagModel) {
        this.tagModel = tagModel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysTagCategory that = (SysTagCategory) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(tagCategoryName, that.tagCategoryName) &&
                Objects.equals(tagCategorySort, that.tagCategorySort) &&
                Objects.equals(tagModel, that.tagModel);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, tagCategoryName, tagCategorySort, tagModel);
    }

    @Override
    public String toString() {
        return "SysTagCategory{" +
                "id='" + id + '\'' +
                ", tagCategoryName='" + tagCategoryName + '\'' +
                ", tagCategorySort=" + tagCategorySort +
                ", tagModel='" + tagModel + '\'' +
                '}';
    }
}
