package com.sinosoft.ops.cimp.entity.sys.code;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/***
 * '代码集'
 */
@Entity
@Table(name = "SYS_CODE_SET")
public class SysCodeSet implements Serializable {

    private static final long serialVersionUID = 8883196329399769497L;

    //'标识'
    @Id
    @GeneratedValue
    private Integer id;

    //'名称'
    private String name;

    //'中文名称'
    private String nameCn;

    //'代码最大长度'
    private Integer maxCodeLength;

    //'类型（国家标准、行业标准、企业标准等）'
    private Integer type;

    //'是否层次结构代码(树形)'
    private Integer hierarchical;

    //'描述'
    private String description;

    //'次序'
    private Integer ordinal;

    //'应用标识'
    private String appId;

    //'状态'
    private Integer status;

    //'创建时间'
    private Date createdTime;

    //'创建人'
    private String createBy;

    //'最后修改时间'
    private Date lastModifiedTime;

    //'最后修改人'
    private String lastModifiedBy;

    //
    private Integer version;

    //
    private Integer leaf;

    //
    private Integer parentId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public Integer getMaxCodeLength() {
        return maxCodeLength;
    }

    public void setMaxCodeLength(Integer maxCodeLength) {
        this.maxCodeLength = maxCodeLength;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getHierarchical() {
        return hierarchical;
    }

    public void setHierarchical(Integer hierarchical) {
        this.hierarchical = hierarchical;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
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

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Date lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getLeaf() {
        return leaf;
    }

    public void setLeaf(Integer leaf) {
        this.leaf = leaf;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}
