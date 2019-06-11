package com.sinosoft.ops.cimp.entity.sheet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * @ClassName: SheetCondition
 * @description: 条件信息
 */
@Entity
@Table(name = "SHEET_CONDITION")
public class SheetCondition implements java.io.Serializable {

    /*** 条件项信息 */
    private static final long serialVersionUID = -120216642216106828L;
    // Fields
    /*** 标识 */
    private UUID id;
    /*** 标识*/
    private UUID categoryId;
    /*** 模板ID*/
    private UUID designId;
    /*** 条件名*/
    private String conditionName;
    /*** 条件关系*/
    private String conditionRelation;

    /*** 内容*/
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

    private String sql;

    // Property accessors
    @Id
    @Column(name = "ID", unique = true, nullable = false)
    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Column(name = "CATEGORY_ID", nullable = false)
    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    @Column(name = "DESIGN_ID", nullable = false)
    public UUID getDesignId() {
        return designId;
    }

    public void setDesignId(UUID designId) {
        this.designId = designId;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
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

    @Column(name = "Condition_Name")
    public String getConditionName() {
        return this.conditionName;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }

    @Column(name = "Condition_Relation")
    public String getConditionRelation() {
        return this.conditionRelation;
    }

    public void setConditionRelation(String conditionRelation) {
        this.conditionRelation = conditionRelation;
    }

    @Column(name = "SQL")
    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public SheetCondition(UUID id, UUID categoryId,
                          String conditionName, String conditionRelation, Integer ordinal, Byte status,
                          Timestamp createdTime, UUID createdBy, Timestamp lastModifiedTime, UUID lastModifiedBy, String sql) {
        this.id = id;
        this.categoryId = categoryId;
        this.conditionName = conditionName;
        this.conditionRelation = conditionRelation;
        this.ordinal = ordinal;
        this.status = status;
        this.createdTime = createdTime;
        this.createdBy = createdBy;
        this.lastModifiedTime = lastModifiedTime;
        this.lastModifiedBy = lastModifiedBy;
        this.sql = sql;
    }

    public SheetCondition(UUID id, UUID categoryId,
                          String conditionName, String conditionRelation) {
        this.id = id;
        this.categoryId = categoryId;
        this.conditionName = conditionName;
        this.conditionRelation = conditionRelation;
    }

    public SheetCondition() {
    }

    @Override
    public String toString() {
        return "SheetCondition [id=" + id + ", categoryId=" + categoryId
                + ", conditionName=" + conditionName + ", conditionRelation="
                + conditionRelation + ", description=" + description + ", ordinal=" + ordinal + ", status=" + status
                + ", createdTime=" + createdTime + ", createdBy=" + createdBy + ", lastModifiedTime=" + lastModifiedTime
                + ", lastModifiedBy=" + lastModifiedBy + "]";
    }
}
