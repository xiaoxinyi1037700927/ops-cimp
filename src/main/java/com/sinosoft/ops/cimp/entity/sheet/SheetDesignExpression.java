package com.sinosoft.ops.cimp.entity.sheet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;

/** 
 * @classname:  SheetDesignExpression
 * @description: 表格设计表达式
 * @author:        Nil
 * @date:            2017年10月22日 下午6:12:02
 * @version        1.0.0
 */
@Entity
@Table(name = "SHEET_DESIGN_EXPRESSION")
public class SheetDesignExpression implements java.io.Serializable {
    private static final long serialVersionUID = -4407221624008014113L;
    
    // Fields
    /*** 标识 */
    private UUID id;
    /*** 表格设计标识 */
    private UUID designId;
    /*** 名称 */
    private String name;
    /*** 表达式类型  */
    private Byte type;
    /*** 内容 */
    private String content;
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
    public SheetDesignExpression() {
    }

    /** minimal constructor */
    public SheetDesignExpression(UUID id, UUID designId, Byte type, Byte status, Timestamp createdTime,
            Timestamp lastModifiedTime) {
        this.id = id;
        this.designId = designId;
        this.type = type;
        this.status = status;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
    }

    /** full constructor */
    public SheetDesignExpression(UUID id, UUID designId, String name, Byte type, String content,
            String description, Integer ordinal, Byte status, Timestamp createdTime, UUID createdBy,
            Timestamp lastModifiedTime, UUID lastModifiedBy) {
        this.id = id;
        this.designId = designId;
        this.name = name;
        this.type = type;
        this.content = content;
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

    @Column(name = "DESIGN_ID", nullable = false)
    public UUID getDesignId() {
        return this.designId;
    }
    public void setDesignId(UUID designId) {
        this.designId = designId;
    }

    @Column(name = "NAME")
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "TYPE", nullable = false, precision = 2, scale = 0)
    public Byte getType() {
        return this.type;
    }
    public void setType(Byte type) {
        this.type = type;
    }

    @Column(name = "CONTENT")
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "ORDINAL", precision = 8)
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