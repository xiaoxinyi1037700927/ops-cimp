package com.sinosoft.ops.cimp.entity.sheet;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * @classname:  SheetDesignSql
 * @description: 表格设计SQL
 * @author:        Nil
 * @date:            2017年8月13日 下午3:43:06
 * @version        1.0.0
 */
@Entity
@Table(name = "SHEET_DESIGN_SQL", uniqueConstraints = @UniqueConstraint(columnNames = "DESIGN_ID"))
public class SheetDesignSql implements java.io.Serializable {
    private static final long serialVersionUID = -2788737970939290967L;
    // Fields
    /*** 标识 */
    private UUID id;
    /*** 表格设计标识 */
    private UUID designId;
    /*** 名称 */    
    private String name;
    /*** SQL */
    private String sql;
    /*** 是否人工录入*/
    private Boolean isManual;
    /*** 起始行号 */
    private Integer startRowNo;
    /*** 起始列号 */
    private Integer startColumnNo;
    /*** 结束行号 */
    private Integer endRowNo;
    /*** 结束列号 */
    private Integer endColumnNo;
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
    public SheetDesignSql() {
    }

    /** minimal constructor */
    public SheetDesignSql(UUID id, UUID designId, Boolean isManual, Byte status, Timestamp createdTime,
            Timestamp lastModifiedTime) {
        this.id = id;
        this.designId = designId;
        this.isManual = isManual;
        this.status = status;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
    }

    /** full constructor */
    public SheetDesignSql(UUID id, UUID designId, String name, String sql, Boolean isManual, Integer startRowNo,
            Integer startColumnNo, Integer endRowNo, Integer endColumnNo, String description, Integer ordinal,
            Byte status, Timestamp createdTime, UUID createdBy, Timestamp lastModifiedTime, UUID lastModifiedBy) {
        this.id = id;
        this.designId = designId;
        this.name = name;
        this.sql = sql;
        this.isManual = isManual;
        this.startRowNo = startRowNo;
        this.startColumnNo = startColumnNo;
        this.endRowNo = endRowNo;
        this.endColumnNo = endColumnNo;
        this.description = description;
        this.ordinal = ordinal;
        this.status = status;
        this.createdTime = createdTime;
        this.createdBy = createdBy;
        this.lastModifiedTime = lastModifiedTime;
        this.lastModifiedBy = lastModifiedBy;
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

    @Column(name = "DESIGN_ID", unique = true, nullable = false)
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

    @Column(name = "SQL")
    public String getSql() {
        return this.sql;
    }
    public void setSql(String sql) {
        this.sql = sql;
    }

    @Column(name = "IS_MANUAL", nullable = false, precision = 1, scale = 0)
    public Boolean getIsManual() {
        return this.isManual;
    }
    public void setIsManual(Boolean isManual) {
        this.isManual = isManual;
    }

    @Column(name = "START_ROW_NO", precision = 8, scale = 0)
    public Integer getStartRowNo() {
        return this.startRowNo;
    }
    public void setStartRowNo(Integer startRowNo) {
        this.startRowNo = startRowNo;
    }

    @Column(name = "START_COLUMN_NO", precision = 8, scale = 0)
    public Integer getStartColumnNo() {
        return this.startColumnNo;
    }
    public void setStartColumnNo(Integer startColumnNo) {
        this.startColumnNo = startColumnNo;
    }

    @Column(name = "END_ROW_NO", precision = 8, scale = 0)
    public Integer getEndRowNo() {
        return this.endRowNo;
    }
    public void setEndRowNo(Integer endRowNo) {
        this.endRowNo = endRowNo;
    }

    @Column(name = "END_COLUMN_NO", precision = 8, scale = 0)
    public Integer getEndColumnNo() {
        return this.endColumnNo;
    }
    public void setEndColumnNo(Integer endColumnNo) {
        this.endColumnNo = endColumnNo;
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
}