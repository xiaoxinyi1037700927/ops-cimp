package com.sinosoft.ops.cimp.entity.sheet;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * 数据源分类表
 */
@Entity
@Table(name = "SHEET_DATA_SOURCE_CATEGORY")
public class SheetDataSourceCategory implements java.io.Serializable {
    private static final long serialVersionUID = 6141133395022321208L;
    /**
     * 主键标识ID
     */
    private Integer id;
    /**
     * 名称
     */
    private String name;
    /**
     * 描述
     */
    private String description;
    /**
     * 父标识
     */
    private Integer parentId;
    /**
     * 显示次序
     */
    private Integer ordinal;
    /**
     * 状态
     */
    private Byte status;
    /**
     * 创建时间
     */
    private Timestamp createdTime;
    /**
     * 创建人
     */
    private UUID createdBy;
    /**
     * 最后修改时间
     */
    private Timestamp lastModifiedTime;
    /**
     * 最后修改人
     */
    private UUID lastModifiedBy;
    /**
     * 是否子节点
     */
    private boolean isLeaf;
    /**
     * 子节点数据源
     */
    private Collection<SheetDataSource> children = new ArrayList<SheetDataSource>();

    public SheetDataSourceCategory(){}

    /** minimal constructor */
    public SheetDataSourceCategory(Integer id, String name, Byte status, Timestamp createdTime,
                                   Timestamp lastModifiedTime) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
    }

    /** full constructor */
    public SheetDataSourceCategory(Integer id, String name, String description, Integer parentId, String appId,
                                   Integer ordinal, Byte status, Timestamp createdTime, UUID createdBy, Timestamp lastModifiedTime,
                                   UUID lastModifiedBy) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.parentId = parentId;
        this.ordinal = ordinal;
        this.status = status;
        this.createdTime = createdTime;
        this.createdBy = createdBy;
        this.lastModifiedTime = lastModifiedTime;
        this.lastModifiedBy = lastModifiedBy;
    }

    @Id
    @Column(name = "ID", unique = true, nullable = false, precision = 8, scale = 0)
    public Integer getId() {
        return this.id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "NAME", nullable = false)
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "PARENT_ID", precision = 8, scale = 0)
    public Integer getParentId() {
        return this.parentId;
    }
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    @Column(name = "ORDINAL", precision = 8, scale = 0)

    public Integer getOrdinal() {
        return this.ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    @Column(name = "STATUS", nullable = false, precision = 2, scale = 0)

    public Byte getStatus() {
        return this.status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Column(name = "CREATED_TIME", nullable = false, length = 11)

    public Timestamp getCreatedTime() {
        return this.createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    @Column(name = "CREATED_BY")

    public UUID getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name = "LAST_MODIFIED_TIME", nullable = false, length = 11)

    public Timestamp getLastModifiedTime() {
        return this.lastModifiedTime;
    }

    public void setLastModifiedTime(Timestamp lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    @Column(name = "LAST_MODIFIED_BY")

    public UUID getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public void setLastModifiedBy(UUID lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Transient
    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    @Transient
    public Collection<SheetDataSource> getChildren() {
        return children;
    }

    public void setChildren(Collection<SheetDataSource> children) {
        this.children = children;
    }
}
