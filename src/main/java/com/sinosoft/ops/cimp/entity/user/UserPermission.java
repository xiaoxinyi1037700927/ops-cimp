package com.sinosoft.ops.cimp.entity.user;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户权限表
 */
@Entity
@Table(name = "USER_PERMISSION")
public class UserPermission {
    /**
     * 主键标识ID
     */
    private String id;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 权限ID
     */
    private String permissionId;

    /**
     * 创建人
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

    @Column(length = 32)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(length = 32)
    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String rightId) {
        this.permissionId = rightId;
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
        return "UserRight{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", permissionId='" + permissionId + '\'' +
                '}';
    }
}
