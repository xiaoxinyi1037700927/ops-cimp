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
@Table(name = "PAGE_INTERFACE")
public class PageInterface {

    /**
     * 主键标识ID
     */
    private String id;

    /**
     * 页面ID
     * */
    private String permissionPageId;

    /**
     * 名称
     * */
    private String name;

    /**
     * 显示名称
     * */
    private String showName;

    /**
     * 描述
     * */
    private String description;

    /**
     * 默认接口sql
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
    public String getPermissionPageId() {
        return permissionPageId;
    }

    public void setPermissionPageId(String permissionPageId) {
        this.permissionPageId = permissionPageId;
    }

    @Column(length = 500)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(length = 500)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(length = 4000)
    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    @Column(length = 500)
    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }
}
