package com.sinosoft.ops.cimp.entity.sys.datapermission;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 角色数据权限配置
 */
@Entity
@Table(name = "ROLE_DATA_PERMISSION")
public class RoleDataPermission {
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "ID", length = 36)
    private String id;
    /**
     * 名称
     */
    @Column(name = "NAME", length = 200)
    private String name;
    /**
     * 角色ID
     */
    @Column(name = "ROLE_ID", length = 36)
    private String roleId;
    /**
     * 接口url
     */
    @Column(name = "URL", length = 500)
    private String url;
    /**
     * sql类型
     */
    @Column(name = "TYPE", length = 10)
    private String type;
    /**
     * sql
     */
    @Column(name = "SQL", length = 1000)
    private String sql;
    /**
     * 创建人
     */
    @Column(name = "CREATE_ID", length = 36)
    private String createId;
    /**
     * 创建时间
     */
    @Column(name = "CREATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    /**
     * 修改人
     */
    @Column(name = "MODIFY_ID", length = 36)
    private String modifyId;
    /**
     * 修改时间
     */
    @Column(name = "MODIFY_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getModifyId() {
        return modifyId;
    }

    public void setModifyId(String modifyId) {
        this.modifyId = modifyId;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
