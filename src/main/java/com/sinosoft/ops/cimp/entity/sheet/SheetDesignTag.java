package com.sinosoft.ops.cimp.entity.sheet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * @classname:  SheetDesignTag
 * @description: 表格设计标签
 * 1、表标签：startRow=0 endRow=0 startCol=0 endCol=0
 * 2、行标签： startRow=非零 endRow=非零 startCol=0 endCol=0
 * 3、列标签： startRow=0 endRow=0 startCol=非零 endCol=非零
 * 4、区域（单元格）标签： startRow=非零 endRow=非零 startCol=非零 endCol=非零
 * （报表定义中行、列的起始值是1）
 * @author:        Nil
 * @date:            2017年8月13日 下午2:58:20
 * @version        1.0.0
 */
@Entity
@Table(name = "SHEET_DESIGN_TAG")
public class SheetDesignTag implements java.io.Serializable{
    private static final long serialVersionUID = -4926466588567000725L;
    // Fields
    /*** 标识 */
    private UUID id;
    /*** 表格设计标识 */
    private UUID designId;
    /*** 行号 */
    private Integer rowNo;
    /*** 列号 */
    private Integer columnNo;
    /*** 名称 */
    private String name;
    /*** 描述 */
    private String description;
    /*** 次序 */
    private Integer ordinal;
    /*** 状态 */
    private Byte status;
    /*** 创建时间 */
    private Timestamp createdTime;
    /*** 创建人 */
    private UUID createdBy;
    /*** 最后修改时间 */
    private Timestamp lastModifiedTime;
    /*** 最后修改人 */
    private UUID lastModifiedBy;


    // Constructors

    /** default constructor */
    public SheetDesignTag() {
    }

    /** minimal constructor */
    public SheetDesignTag(UUID id, UUID designId, Byte status, Timestamp createdTime, Timestamp lastModifiedTime) {
        this.id = id;
        this.designId = designId;
        this.status = status;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
    }

    /** full constructor */
    public SheetDesignTag(UUID id, UUID designId, String name, Integer columnNo, String description,
            Integer ordinal, Byte status, Timestamp createdTime, UUID createdBy, Timestamp lastModifiedTime,
            UUID lastModifiedBy, Integer rowNo) {
        this.id = id;
        this.designId = designId;
        this.name = name;
        this.columnNo = columnNo;
        this.description = description;
        this.ordinal = ordinal;
        this.status = status;
        this.createdTime = createdTime;
        this.createdBy = createdBy;
        this.lastModifiedTime = lastModifiedTime;
        this.lastModifiedBy = lastModifiedBy;
        this.rowNo = rowNo;
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

    @Column(name = "DESIGN_ID", nullable = false)

    public UUID getDesignId() {
        return this.designId;
    }

    public void setDesignId(UUID designId) {
        this.designId = designId;
    }

    @Column(name = "NAME", length = 100)

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "COLUMN_NO", precision = 8, scale = 0)

    public Integer getColumnNo() {
        return this.columnNo;
    }

    public void setColumnNo(Integer columnNo) {
        this.columnNo = columnNo;
    }

    @Column(name = "DESCRIPTION")

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Column(name = "ROW_NO", precision = 8, scale = 0)

    public Integer getRowNo() {
        return this.rowNo;
    }

    public void setRowNo(Integer rowNo) {
        this.rowNo = rowNo;
    }

}