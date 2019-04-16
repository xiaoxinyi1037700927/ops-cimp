package com.sinosoft.ops.cimp.entity.sys.oraganization;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 业务部门关联单位表
 */
@Entity
@Table(name = "BUSINESS_UNIT_ORG")
public class BusinessUnitOrg {
    /**
     * 主键ID
     */
    private String id;
    /**
     * 业务部门Id
     */
    private String businessUnitId;
    /**
     * 业务部门名称
     */
    private String businessUnitName;
    /**
     * 单位Id
     */
    private String organizationId;
    /**
     * 有效期
     */
    private Date validityDate;

    /**
     * 创建人ID
     */
    private String createId;
    /**
     * 创建时间
     */
    private Date createTime;


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
    public String getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(String businessUnitId) {
        this.businessUnitId = businessUnitId;
    }

    @Column(length = 100)
    public String getBusinessUnitName() {
        return businessUnitName;
    }

    public void setBusinessUnitName(String businessUnitName) {
        this.businessUnitName = businessUnitName;
    }

    @Column(length = 50)
    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    @Column
    public Date getValidityDate() {
        return validityDate;
    }

    public void setValidityDate(Date validityDate) {
        this.validityDate = validityDate;
    }

    @Column(length = 50)
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

    @Override
    public String toString() {
        return "BusinessUnitOrg{" +
                "id='" + id + '\'' +
                ", businessUnitId='" + businessUnitId + '\'' +
                ", businessUnitName='" + businessUnitName + '\'' +
                ", organizationId='" + organizationId + '\'' +
                ", validityDate=" + validityDate +
                ", createId='" + createId + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
