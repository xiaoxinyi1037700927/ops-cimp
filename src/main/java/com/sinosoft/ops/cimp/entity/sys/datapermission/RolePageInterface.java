package com.sinosoft.ops.cimp.entity.sys.datapermission;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by Jay on 2019/2/22.
 *
 * @author Jay
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinoSoft All Rights Received
 */
@Entity
@Table(name = "ROLE_PAGE_INTERFACE")
public class RolePageInterface {
    /**
     * 主键标识ID
     */
    private String id;

    /**
     * 角色ID
     * */
    private String roleId;

    /**
     * 关联接口
     */
    private String pageInterfaceId;

    /**
     * 接口sql
     * */
    private String sql;

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
    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Column(length = 50)
    public String getPageInterfaceId() {
        return pageInterfaceId;
    }

    public void setPageInterfaceId(String pageInterfaceId) {
        this.pageInterfaceId = pageInterfaceId;
    }

    @Column(length = 4000)
    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
