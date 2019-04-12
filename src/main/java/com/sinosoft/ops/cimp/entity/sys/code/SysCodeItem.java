package com.sinosoft.ops.cimp.entity.sys.code;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/***
 * '代码项'
 */
@Entity
@Table(name = "SYS_CODE_ITEM")
public class SysCodeItem implements Serializable {
    private static final long serialVersionUID = 2761056458148457602L;

    //'标识'
    @Id
    @GeneratedValue
    private Integer id;

    //'代码集标识'
    private Integer codeSetId;

    //'代码'
    private String code;

    //'名称'
    private String name;

    //'简称'
    private String briefName;

    //'上级代码'
    private String parentCode;

    //'名称简拼'
    private String spell;

    //'是否可选择'
    private Integer selectAble;

    //'是否叶子'
    private Integer leaf;

    //'描述'
    private String description;

    //'是否已失效'
    private Integer invalid;

    //'次序'
    private Integer ordinal;

    //'状态'
    private Integer status;

    //'创建时间'
    private Date createdTime;

    //'创建人'
    private String createdBy;

    //'最后修改时间'
    private Date lastModifiedTime;

    //'最后修改人'
    private String LastModifiedBy;

    //是否只读
    private Integer readonly;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCodeSetId() {
        return codeSetId;
    }

    public void setCodeSetId(Integer codeSetId) {
        this.codeSetId = codeSetId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBriefName() {
        return briefName;
    }

    public void setBriefName(String briefName) {
        this.briefName = briefName;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getSpell() {
        return spell;
    }

    public void setSpell(String spell) {
        this.spell = spell;
    }

    public Integer getSelectAble() {
        return selectAble;
    }

    public void setSelectAble(Integer selectAble) {
        this.selectAble = selectAble;
    }

    public Integer getLeaf() {
        return leaf;
    }

    public void setLeaf(Integer leaf) {
        this.leaf = leaf;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getInvalid() {
        return invalid;
    }

    public void setInvalid(Integer invalid) {
        this.invalid = invalid;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Date lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getLastModifiedBy() {
        return LastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        LastModifiedBy = lastModifiedBy;
    }

    public Integer getReadonly() {
        return readonly;
    }

    public void setReadonly(Integer readonly) {
        this.readonly = readonly;
    }
}

