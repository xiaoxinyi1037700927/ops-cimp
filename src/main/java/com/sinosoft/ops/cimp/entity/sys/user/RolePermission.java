package com.sinosoft.ops.cimp.entity.sys.user;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 角色权限关系表
 */
@Entity
@Table(name = "ROLE_PERMISSION")
public class RolePermission {
    /**
     * 主键标识ID
     */
    private String id;
    /**
     * 角色ID
     */
    private String roleId;
    /**
     * 权限所在分组
     * */
    private String roleMenuGroupId;
    /**
     * 权限ID
     */
    private String permissionId;
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

    @Column(length = 32)
    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Column(length = 32)
    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    @Column(length = 50)
    public String getRoleMenuGroupId() {
        return roleMenuGroupId;
    }

    public void setRoleMenuGroupId(String roleMenuGroupId) {
        this.roleMenuGroupId = roleMenuGroupId;
    }

    @Column(length = 50)
    public Integer getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(Integer sortNumber) {
        this.sortNumber = sortNumber;
    }

    @Override
    public String toString() {
        return "RoleRight{" +
                "id='" + id + '\'' +
                ", roleId='" + roleId + '\'' +
                ", permissionId='" + permissionId + '\'' +
                '}';
    }
}
