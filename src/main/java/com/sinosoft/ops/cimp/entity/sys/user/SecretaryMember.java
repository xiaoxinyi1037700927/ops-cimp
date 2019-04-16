package com.sinosoft.ops.cimp.entity.sys.user;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 书记会成员表配置
 */
@Entity
@Table(name = "SECRETARY_MEMBER")
public class SecretaryMember {

    /**
     * 主键标识
     */
    private String id;
    /**
     * 干部ID
     */
    private String cadreInfoId;
    /**
     * 机构ID
     */
    private String organizationId;

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
    public String getCadreInfoId() {
        return cadreInfoId;
    }

    public void setCadreInfoId(String cadreInfoId) {
        this.cadreInfoId = cadreInfoId;
    }

    @Column(length = 50)
    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
}
