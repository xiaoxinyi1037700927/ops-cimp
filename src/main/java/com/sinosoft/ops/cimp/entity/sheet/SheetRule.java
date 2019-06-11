package com.sinosoft.ops.cimp.entity.sheet;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * @classname:  SheetRule
 * @description: 表格规则
 * 表格规则是构建表格条件的基础，每个规则包含一到多个规则项组，
 * 每个规则组包含一到多个规则项。规则组的逻辑操作符（AND/OR/NOT）
 * 决定了该组规则项之间的逻辑关系。
 * @author:        Nil
 * @date:            2017年8月13日 下午2:08:11
 * @version        1.0.0
 */
@Entity
@Table(name = "SHEET_RULE")
public class SheetRule implements java.io.Serializable {
    private static final long serialVersionUID = 9169057163912260582L;
    // Fields
    /*** 标识 */
    private UUID id;
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
    /*** 条件项分组集 */
    protected Collection<SheetRuleItemGroup> itemGroups = new ArrayList<SheetRuleItemGroup>();

    // Constructors

    /** default constructor */
    public SheetRule() {
    }

    /** minimal constructor */
    public SheetRule(UUID id, Byte status, Timestamp createdTime, Timestamp lastModifiedTime) {
        this.id = id;
        this.status = status;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
    }

    /** full constructor */
    public SheetRule(UUID id, String name, String description, Integer ordinal, Byte status, Timestamp createdTime,
            UUID createdBy, Timestamp lastModifiedTime, UUID lastModifiedBy) {
        this.id = id;
        this.name = name;
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

    @Column(name = "NAME", length = 100)

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

    @Transient
    public Collection<SheetRuleItemGroup> getItemGroups() {
        return itemGroups;
    }
    public void setItemGroups(Collection<SheetRuleItemGroup> itemGroups) {
        this.itemGroups = itemGroups;
    }
}