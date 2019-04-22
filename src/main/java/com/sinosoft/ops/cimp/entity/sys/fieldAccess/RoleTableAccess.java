package com.sinosoft.ops.cimp.entity.sys.fieldAccess;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 角色对表的访问权限
 */
@Entity
@Table(name = "ROLE_TABLE_ACCESS")
public class RoleTableAccess implements Serializable {

    private static final long serialVersionUID = -6270278742885643656L;
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "ID", length = 36)
    private String id;
    /**
     * 角色ID
     */
    @Column(name = "ROLE_ID", length = 36)
    private String roleId;
    /**
     * 系统表ID
     */
    @Column(name = "SYS_TABLE_ID", length = 36)
    private String sysTableId;
    /**
     * 是否对表中所有字段有读权限
     */
    @Column(name = "CAN_READ_ALL", length = 1)
    private Boolean canReadAll;
    /**
     * 是否对表中所有字段有读权限
     */
    @Column(name = "CAN_WRITE_ALL", length = 1)
    private Boolean canWriteAll;
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

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getSysTableId() {
        return sysTableId;
    }

    public void setSysTableId(String sysTableId) {
        this.sysTableId = sysTableId;
    }

    public Boolean getCanReadAll() {
        return canReadAll;
    }

    public void setCanReadAll(Boolean canReadAll) {
        this.canReadAll = canReadAll;
    }

    public Boolean getCanWriteAll() {
        return canWriteAll;
    }

    public void setCanWriteAll(Boolean canWriteAll) {
        this.canWriteAll = canWriteAll;
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
