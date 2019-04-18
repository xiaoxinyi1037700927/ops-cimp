package com.sinosoft.ops.cimp.entity.sys.permission;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 菜单分组权限映射表
 */
@Entity
@Table(name = "PERMISSION_MENU_GROUP_REL")
public class PermissionMenuGroupRel {
    /**
     * 标识ID
     * */
    private String id;
    /**
     * 菜单分组Id
     * */
    private String menuGroupId;
    /**
     * 权限ID
     * */
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

    @Column(length = 50)
    public String getMenuGroupId() {
        return menuGroupId;
    }


    public void setMenuGroupId(String menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    @Column(length = 50)
    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    @Column(length = 50)
    public Integer getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(Integer sortNumber) {
        this.sortNumber = sortNumber;
    }
}
