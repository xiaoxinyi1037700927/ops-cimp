package com.sinosoft.ops.cimp.entity.sys.user.organization;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 单位职务表
 */
@Entity
@Table(name  = "ORGANIZATION_POSITION")
public class OrganizationPosition {

    /**
     * 标准主键ID
     */
    private String id;
    /**
     * 单位ID
     */
    private String organizationId;
    /**
     * 职务名称
     */
    private String name;
    /**
     * 职务代码
     */
    private String code;
    /**
     * 职务ID
     */
    private String positionId;
    /**
     * 职务属性代码 正职,副职
     */
    private String type;
    /**
     * 职务属性ID
     */
    private String typeId;
    /**
     * 职数应有数
     */
    private Integer positionQuota;
    /**
     * 职级ID
     */
    private String rankId;
    /**
     * 职级代码
     */
    private String rankCode;
    /**
     * 任职方式
     */
    private String beInOfficeType;
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

    @Column(length = 50)
    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    @Column(length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(length = 100)
    public String getCode() {
        return code;
    }

    @Column(length = 100)
    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    @Column(length = 100)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(length = 100)
    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(length = 8)
    public Integer getPositionQuota() {
        return positionQuota;
    }

    public void setPositionQuota(Integer positionQuota) {
        this.positionQuota = positionQuota;
    }

    @Column(length = 32)
    public String getRankId() {
        return rankId;
    }

    public void setRankId(String rankId) {
        this.rankId = rankId;
    }

    @Column(length = 100)
    public String getRankCode() {
        return rankCode;
    }

    public void setRankCode(String rankCode) {
        this.rankCode = rankCode;
    }

    @Column(length = 50)
    public String getBeInOfficeType() {
        return beInOfficeType;
    }

    public void setBeInOfficeType(String beInOfficeType) {
        this.beInOfficeType = beInOfficeType;
    }

    @Column(length = 100)
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

    @Column(length = 100)
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

    @Override
    public String toString() {
        return "OrganizationPosition{" +
                "id='" + id + '\'' +
                ", organizationId='" + organizationId + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", positionQuota=" + positionQuota +
                ", rankId='" + rankId + '\'' +
                ", rankCode='" + rankCode + '\''+
                '}';
    }
}
