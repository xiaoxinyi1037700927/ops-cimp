package com.sinosoft.ops.cimp.entity.sys.tag;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "SYS_TAG")
public class SysTag implements Serializable {
    private static final long serialVersionUID = 5704453407248851082L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "ID", length = 36)
    private String id;

    @Column(name = "TAG_CATEGORY_ID", length = 36)
    private String tagCategoryId;

    @Column(name = "TAG", length = 50)
    private String tag;

    @Column(name = "TAG_SQL", length = 4000)
    private String tagSql;

    @Column(name = "TAG_COUNT_SQL", length = 4000)
    private String tagCountSql;

    @Column(name = "TAG_TARGET_NAME", length = 200)
    private String tagTargetName;

    @Column(name = "TAG_SORT", length = 13)
    private Integer tagSort;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTagCategoryId() {
        return tagCategoryId;
    }

    public void setTagCategoryId(String tagCategoryId) {
        this.tagCategoryId = tagCategoryId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTagSql() {
        return tagSql;
    }

    public void setTagSql(String tagSql) {
        this.tagSql = tagSql;
    }

    public String getTagCountSql() {
        return tagCountSql;
    }

    public void setTagCountSql(String tagCountSql) {
        this.tagCountSql = tagCountSql;
    }

    public String getTagTargetName() {
        return tagTargetName;
    }

    public void setTagTargetName(String tagTargetName) {
        this.tagTargetName = tagTargetName;
    }

    public Integer getTagSort() {
        return tagSort;
    }

    public void setTagSort(Integer tagSort) {
        this.tagSort = tagSort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysTag sysTag = (SysTag) o;
        return Objects.equals(id, sysTag.id) &&
                Objects.equals(tagCategoryId, sysTag.tagCategoryId) &&
                Objects.equals(tag, sysTag.tag) &&
                Objects.equals(tagSql, sysTag.tagSql) &&
                Objects.equals(tagCountSql, sysTag.tagCountSql) &&
                Objects.equals(tagTargetName, sysTag.tagTargetName) &&
                Objects.equals(tagSort, sysTag.tagSort);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, tagCategoryId, tag, tagSql, tagCountSql, tagTargetName, tagSort);
    }

    @Override
    public String toString() {
        return "SysTag{" +
                "id='" + id + '\'' +
                ", tagCategoryId='" + tagCategoryId + '\'' +
                ", tag='" + tag + '\'' +
                ", tagSql='" + tagSql + '\'' +
                ", tagCountSql='" + tagCountSql + '\'' +
                ", tagTargetName='" + tagTargetName + '\'' +
                ", tagSort=" + tagSort +
                '}';
    }
}
