package com.sinosoft.ops.cimp.entity.sys.user;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 业务部门表
 */
@Entity
@Table(name = "BUSINESS_UNIT")
public class BusinessUnit {
    /**
     * 主键ID
     */
    private String id;
    /**
     * 业务部门名称
     */
    private String name;

    /**
     * 有效期
     */
    private Date validityDate;

    /**
     * 创建单位ID
     */
    private String createOrgId;
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

    @Column(length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(length = 50)
    public String getCreateOrgId() {
        return createOrgId;
    }

    public void setCreateOrgId(String createOrgId) {
        this.createOrgId = createOrgId;
    }

    @Column
    public Date getValidityDate() {
        return validityDate;
    }

    public void setValidityDate(Date validityDate) {
        this.validityDate = validityDate;
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
        return "BusinessUnit{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", validityDate=" + validityDate +
                ", createOrgId='" + createOrgId + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
