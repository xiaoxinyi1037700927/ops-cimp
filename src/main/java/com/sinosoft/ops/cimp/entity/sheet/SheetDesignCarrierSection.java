package com.sinosoft.ops.cimp.entity.sheet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;

/** 
 * @classname:  SheetDesignCarrierSection
 * @description: 表格设计载体数据区
 * @author:        Nil
 * @date:            2017年10月22日 下午5:56:49
 * @version        1.0.0
 */
@Entity
@Table(name = "SHEET_DESIGN_CARRIER_SECTION")
public class SheetDesignCarrierSection implements java.io.Serializable {
    private static final long serialVersionUID = -1703014875774503401L;
    // Fields
    /*** 标识 */
    private UUID id;
    /*** 数据区标识 */
    private UUID sectionId;
    /*** 载体标识 */
    private UUID carrierId;
    /*** 起始行号 */
    private Integer startRowNo;
    /*** 起始列号 */
    private Integer startColumnNo;
    /*** 结束行号 */
    private Integer endRowNo;
    /*** 结束列号 */
    private Integer endColumnNo;
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
    public SheetDesignCarrierSection() {
    }

    /** minimal constructor */
    public SheetDesignCarrierSection(UUID id, UUID sectionId, UUID carrierId, Byte status, Timestamp createdTime,
            Timestamp lastModifiedTime) {
        this.id = id;
        this.sectionId = sectionId;
        this.carrierId = carrierId;
        this.status = status;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
    }

    /** full constructor */
    public SheetDesignCarrierSection(UUID id, UUID sectionId, UUID carrierId, Integer startRowNo,
            Integer startColumnNo, Integer endRowNo, Integer endColumnNo, Integer ordinal, Byte status,
            Timestamp createdTime, UUID createdBy, Timestamp lastModifiedTime, UUID lastModifiedBy) {
        this.id = id;
        this.sectionId = sectionId;
        this.carrierId = carrierId;
        this.startRowNo = startRowNo;
        this.startColumnNo = startColumnNo;
        this.endRowNo = endRowNo;
        this.endColumnNo = endColumnNo;
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

    @Column(name = "SECTION_ID", nullable = false)
    public UUID getSectionId() {
        return this.sectionId;
    }
    public void setSectionId(UUID sectionId) {
        this.sectionId = sectionId;
    }

    @Column(name = "CARRIER_ID", nullable = false)
    public UUID getCarrierId() {
        return this.carrierId;
    }
    public void setCarrierId(UUID carrierId) {
        this.carrierId = carrierId;
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