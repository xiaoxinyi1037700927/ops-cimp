package com.sinosoft.ops.cimp.entity.sheet;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * @classname:  SheetRuleItemGroup
 * @description: 表格规则项组
 * 表格规则项组包含一到多个表格规则项，各规则项之间的逻辑关系由
 * 规则项组的“逻辑操作符”决定。
 * @author:        Nil
 * @date:            2017年8月13日 下午2:07:26
 * @version        1.0.0
 */
@Entity
@Table(name = "SHEET_RULE_ITEM_GROUP")
public class SheetRuleItemGroup implements java.io.Serializable {
    private static final long serialVersionUID = -7912740927947516587L;
    // Fields
    /*** 标识 */
    private UUID id;
    /*** 规则标识 */
    private UUID ruleId;
    /*** 名称 */
    private String name;
    /*** 逻辑运算符 */
    private String logicalOperator;
    /*** 上级标识 */
    private UUID parentId;
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
    /*** 条件项集 */
    protected Collection<SheetRuleItem> items = new ArrayList<SheetRuleItem>();
    
    // Constructors

    /** default constructor */
    public SheetRuleItemGroup() {
    }

    /** minimal constructor */
    public SheetRuleItemGroup(UUID id, UUID ruleId, Byte status, Timestamp createdTime,
            Timestamp lastModifiedTime) {
        this.id = id;
        this.ruleId = ruleId;
        this.status = status;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
    }

    /** full constructor */
    public SheetRuleItemGroup(UUID id, UUID ruleId, String name, String logicalOperator, UUID parentId,
            String description, Integer ordinal, Byte status, Timestamp createdTime, UUID createdBy,
            Timestamp lastModifiedTime, UUID lastModifiedBy) {
        this.id = id;
        this.ruleId = ruleId;
        this.name = name;
        this.logicalOperator = logicalOperator;
        this.parentId = parentId;
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

    @Column(name = "RULE_ID", nullable = false)

    public UUID getRuleId() {
        return this.ruleId;
    }

    public void setRuleId(UUID ruleId) {
        this.ruleId = ruleId;
    }

    @Column(name = "NAME", length = 100)

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "LOGICAL_OPERATOR", length = 20)

    public String getLogicalOperator() {
        return this.logicalOperator;
    }

    public void setLogicalOperator(String logicalOperator) {
        this.logicalOperator = logicalOperator;
    }

    @Column(name = "PARENT_ID")

    public UUID getParentId() {
        return this.parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
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
    public Collection<SheetRuleItem> getItems() {
        return items;
    }
    public void setItems(Collection<SheetRuleItem> items) {
        this.items = items;
    }
}