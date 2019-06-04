package com.sinosoft.ops.cimp.entity.sheet;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * SheetDataSource entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SHEET_DATA_SOURCE")

public class SheetDataSource implements java.io.Serializable {
    private static final long serialVersionUID = -1457110480110902042L;
    /**
     * 主键标识ID
     */
    private UUID id;
    /**
     * 数据源分类ID
     */
    private Integer categoryId;
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
    private Integer refNum;
    /**
     * 名称
     */
    private String name;
    /**
     * 数据查询sql
     */
    private String sql;
    /**
     * 数据范围
     */
    private String dataRange;

    // Constructors

    /** default constructor */
    public SheetDataSource() {
    }
    // Property accessors
    @Id
    @Column(name = "ID", unique = true, nullable = false)
    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Column(name = "Name", nullable = false)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "Category_Id", nullable = false)
    public Integer getCategoryId() {
        return this.categoryId;
    }
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
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

    @Column(name="SQL")
    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    @Column(name="DATA_RANGE")
    public String getDataRange() {
        return dataRange;
    }

    public void setDataRange(String dataRange) {
        this.dataRange = dataRange;
    }


    @Transient
    public Integer getRefNum() {
        return refNum;
    }

    public void setRefNum(Integer refNum) {
        this.refNum = refNum;
    }
}