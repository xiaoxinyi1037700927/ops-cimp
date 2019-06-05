package com.sinosoft.ops.cimp.entity.sheet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * @classname:  SheetDesignCell
 * @description: 表格设计单元格
 * @author:        Nil
 * @date:            2017年10月22日 下午6:05:15
 * @version        1.0.0
 */
@Entity
@Table(name = "SHEET_DESIGN_CELL")
public class SheetDesignCell implements java.io.Serializable,Trackable {
    private static final long serialVersionUID = -6975531703676344886L;
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
    /**是否需要校核*/
    private Boolean needCheck;


    // Constructors

    /** default constructor */
    public SheetDesignCell() {
    }

    /** minimal constructor */
    public SheetDesignCell(UUID id, Integer rowNo, Integer columnNo, Byte status, Timestamp createdTime,
            Timestamp lastModifiedTime, UUID designId) {
        this.id = id;
        this.rowNo = rowNo;
        this.columnNo = columnNo;
        this.status = status;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
        this.designId = designId;
    }

    /** full constructor */
    public SheetDesignCell(UUID id, Integer rowNo, Integer columnNo, String name, String description, Integer ordinal,
            Byte status, Timestamp createdTime, UUID createdBy, Timestamp lastModifiedTime, UUID lastModifiedBy,
            UUID designId) {
        this.id = id;
        this.rowNo = rowNo;
        this.columnNo = columnNo;
        this.name = name;
        this.description = description;
        this.ordinal = ordinal;
        this.status = status;
        this.createdTime = createdTime;
        this.createdBy = createdBy;
        this.lastModifiedTime = lastModifiedTime;
        this.lastModifiedBy = lastModifiedBy;
        this.designId = designId;
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

    @Column(name = "ROW_NO", nullable = false, precision = 8, scale = 0)
    public Integer getRowNo() {
        return this.rowNo;
    }
    public void setRowNo(Integer rowNo) {
        this.rowNo = rowNo;
    }

    @Column(name = "COLUMN_NO", nullable = false, precision = 8, scale = 0)
    public Integer getColumnNo() {
        return this.columnNo;
    }
    public void setColumnNo(Integer columnNo) {
        this.columnNo = columnNo;
    }

    @Column(name = "NAME")
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

    @Column(name = "NEED_CHECK")
	public Boolean getNeedCheck() {
		return needCheck;
	}

	public void setNeedCheck(Boolean needCheck) {
		this.needCheck = needCheck;
	}
    
    
}