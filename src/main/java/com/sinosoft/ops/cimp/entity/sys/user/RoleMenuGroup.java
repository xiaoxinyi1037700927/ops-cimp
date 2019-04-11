package com.sinosoft.ops.cimp.entity.sys.user;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 角色表
 */
@Entity
@Table(name="ROLE_MENU_GROUP")
public class RoleMenuGroup {

    /**
     * 主键标识ID
     */
    private String id;

    /**
     * 角色ID
     * */
    private String roleId;

    /**
     * 名称
     * */
    private String name;

    /**
     * icon
     * */
    private String icon;

    /**
     * 排序
     * */
    private Integer sortNumber;

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

    @Column(length = 500)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(length = 500)
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Column
    public Integer getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(Integer sortNumber) {
        this.sortNumber = sortNumber;
    }
}
