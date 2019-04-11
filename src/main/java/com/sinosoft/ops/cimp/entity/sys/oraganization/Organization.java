package com.sinosoft.ops.cimp.entity.sys.oraganization;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 单位(组织)表
 */
@Entity
@Table(name = "Organization")
public class Organization {
    /**
     * 主键标识ID
     */
    private String id;
    /**
     * 单位名称
     */
    private String name;
    /**
     * 单位代码
     */
    private String code;
    /**
     * 单位简称
     */
    private String briefName;
    /**
     * 单位全称
     */
    private String fullName;
    /**
     * 排序
     */
    private Integer sortNumber;
    /**
     * 描述
     */
    private String description;
    /**
     * 父单位code
     */
    private String parentCode;
    /**
     * 父单位ID
     */
    private String parentId;
    /**
     * 单位性质(党委、政府..)
     */
    private String orgType;
    /**
     * 单位隶属关系(市值单位, 县区单位, 乡镇, 街道办事处等)
     */
    private String subOrdination;

    /**
     * 单位级别
     */
    private String orgLevel;
    /**
     * 创建人
     */
    private String createId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改人
     */
    private String modifyId;
    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 源单位代码
     */
    private String sourceCode;
    /**
     * 源父单位code
     */
    private String sourceParentCode;

    public Organization() {
    }

    public Organization(String id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public Organization(String id, String name, String code,
                        String parentCode, String briefName,
                        String parentId, String orgType, String subOrdination,Integer sortNumber) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.parentCode = parentCode;
        this.briefName = briefName;
        this.parentId = parentId;
        this.orgType = orgType;
        this.subOrdination = subOrdination;
        this.sortNumber = sortNumber;
    }

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(length = 50)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(length = 120)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(length = 200)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(length = 60)
    public String getBriefName() {
        return briefName;
    }

    public void setBriefName(String briefName) {
        this.briefName = briefName;
    }

    @Column(length = 200)
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Column(length = 10)
    public Integer getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(Integer sortNumber) {
        this.sortNumber = sortNumber;
    }

    @Column(length = 500)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(length = 200)
    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    @Column(length = 32)
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Column(length = 100)
    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    @Column(length = 100)
    public String getOrgLevel() {
        return orgLevel;
    }

    public void setOrgLevel(String orgLevel) {
        this.orgLevel = orgLevel;
    }

    @Column(length = 100)
    public String getSubOrdination() {
        return subOrdination;
    }

    public void setSubOrdination(String subOrdination) {
        this.subOrdination = subOrdination;
    }


    @Column(length = 32)
    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    @Column
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(length = 32)
    public String getModifyId() {
        return modifyId;
    }

    public void setModifyId(String modifyId) {
        this.modifyId = modifyId;
    }

    @Column
    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    @Column(length = 50)
    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    @Column(length = 50)
    public String getSourceParentCode() {
        return sourceParentCode;
    }

    public void setSourceParentCode(String sourceParentCode) {
        this.sourceParentCode = sourceParentCode;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", briefName='" + briefName + '\'' +
                ", description='" + description + '\'' +
                ", parentCode='" + parentCode + '\'' +
                ", parentId='" + parentId + '\'' +
                ", orgType='" + orgType + '\'' +
                ", orgLevel='" + orgLevel + '\'' +
                ", createId='" + createId + '\'' +
                ", createTime=" + createTime +
                ", modifyId='" + modifyId + '\'' +
                ", modifyTime=" + modifyTime +
                '}';
    }
}
