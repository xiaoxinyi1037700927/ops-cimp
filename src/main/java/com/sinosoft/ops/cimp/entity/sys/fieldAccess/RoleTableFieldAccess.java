package com.sinosoft.ops.cimp.entity.sys.fieldAccess;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 角色对表字段的访问权限
 */
@Entity
@Table(name = "ROLE_TABLE_FIELD_ACCESS")
public class RoleTableFieldAccess implements Serializable {
    private static final long serialVersionUID = -1812456354180177297L;
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "ID", length = 36)
    private String id;
    /**
     * 角色对表访问权限ID
     */
    @Column(name = "ROLE_TABLE_ACCESS_ID", length = 36)
    private String roleTableAccessId;
    /**
     * 系统表字段ID
     */
    @Column(name = "SYS_TABLE_FIELD_ID", length = 36)
    private String sysTableFieldId;
    /**
     * 是否对表中所有字段有读权限
     */
    @Column(name = "CAN_READ", length = 1)
    private Boolean canRead;
    /**
     * 是否对表中所有字段有读权限
     */
    @Column(name = "CAN_WRITE", length = 1)
    private Boolean canWrite;
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

    public String getRoleTableAccessId() {
        return roleTableAccessId;
    }

    public void setRoleTableAccessId(String roleTableAccessId) {
        this.roleTableAccessId = roleTableAccessId;
    }

    public String getSysTableFieldId() {
        return sysTableFieldId;
    }

    public void setSysTableFieldId(String sysTableFieldId) {
        this.sysTableFieldId = sysTableFieldId;
    }

    public Boolean getCanRead() {
        return canRead;
    }

    public void setCanRead(Boolean canRead) {
        this.canRead = canRead;
    }

    public Boolean getCanWrite() {
        return canWrite;
    }

    public void setCanWrite(Boolean canWrite) {
        this.canWrite = canWrite;
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
