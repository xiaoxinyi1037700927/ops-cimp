package com.sinosoft.ops.cimp.entity.sys.user;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 角色与页面功能权限关联
 */
@Entity
@Table(name = "ROLE_PERMISSION_PAGE")
public class RolePermissionPage {

    /**
     * 标识ID
     * */
    private String id;

    /**
     * 角色ID
     * */
    private String roleId;

    /**
     *页面Id
     * */
    private String permissionPageId;

    /**
     * 操作ID
     * */
    private String permissionPageOperationId;

    /**
     * 0禁用
     * */
    private String status;

    /**
     * 排序
     * */
    private Integer sortNumber;

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
    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Column(length = 50)
    public String getPermissionPageId() {
        return permissionPageId;
    }

    public void setPermissionPageId(String permissionPageId) {
        this.permissionPageId = permissionPageId;
    }

    @Column(length = 50)
    public String getPermissionPageOperationId() {
        return permissionPageOperationId;
    }

    public void setPermissionPageOperationId(String permissionPageOperationId) {
        this.permissionPageOperationId = permissionPageOperationId;
    }

    @Column(length = 50)
    public Integer getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(Integer sortNumber) {
        this.sortNumber = sortNumber;
    }

    @Column(length = 50)
    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    @Column(length = 50)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(length = 50)
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
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
