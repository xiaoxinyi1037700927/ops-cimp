package com.sinosoft.ops.cimp.entity.sheet;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "SHEET_CONDITION_ITEM", uniqueConstraints = @UniqueConstraint(columnNames = "ID"))
public class SheetConditionItem implements java.io.Serializable {

    // Fields
    private static final long serialVersionUID = -2495831922163024560L;
    private String id;
    private String conditionid;
    private Integer conditionnum;
    private String jsondata;
    private String sql;
    private String description;
    private Integer ordinal;
    private Byte status;
    private Timestamp createdTime;
    private String createdBy;
    private Timestamp lastModifiedTime;
    private String lastModifiedBy;

    // Constructors

    /**
     * default constructor
     */
    public SheetConditionItem() {
    }

    /**
     * minimal constructor
     */
    public SheetConditionItem(String id, String conditionid, String jsondata, Integer conditionnum) {
        this.id = id;
        this.conditionid = conditionid;
        this.conditionnum = conditionnum;
        this.jsondata = jsondata;
    }

    @Id
    @Column(name = "ID", unique = true, nullable = false)
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "CONDITION_ID")
    public String getConditionId() {
        return this.conditionid;
    }

    public void setConditionId(String conditionid) {
        this.conditionid = conditionid;
    }

    @Column(name = "CONDITION_NUM")
    public Integer getConditionNum() {
        return this.conditionnum;
    }

    public void setConditionNum(Integer conditionnum) {
        this.conditionnum = conditionnum;
    }


    @Column(name = "Json_Data")
    public String getJsonData() {
        return this.jsondata;
    }

    public void setJsonData(String jsondata) {
        this.jsondata = jsondata;
    }

    @Column(name = "SQL")
    public String getSql() {
        return this.sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
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

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
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

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

}