package com.sinosoft.ops.cimp.entity.sys.sysapp.fieldAccess;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统app中角色对表的访问权限
 */
@Entity
@Table(name = "SYS_APP_ROLE_TABLE_ACCESS")
public class SysAppRoleTableAccess implements Serializable {

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
     * 系统应用ID
     */
    @Column(name = "SYS_APP_ID", length = 36)
    private String sysAppId;
    /**
     * 系统应用表ID
     */
    @Column(name = "SYS_APP_TABLE_SET_ID", length = 36)
    private String SysAppTableSetId;
    /**
     * 是否对表中所有字段有读权限
     */
    @Column(name = "CAN_READ_ALL", length = 1)
    private boolean canReadAll;
    /**
     * 是否对表中所有字段有写权限
     */
    @Column(name = "CAN_WRITE_ALL", length = 1)
    private boolean canWriteAll;
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

    public String getSysAppId() {
        return sysAppId;
    }

    public void setSysAppId(String sysAppId) {
        this.sysAppId = sysAppId;
    }

    public String getSysAppTableSetId() {
        return SysAppTableSetId;
    }

    public void setSysAppTableSetId(String sysAppTableSetId) {
        SysAppTableSetId = sysAppTableSetId;
    }

    public boolean getCanReadAll() {
        return canReadAll;
    }

    public void setCanReadAll(boolean canReadAll) {
        this.canReadAll = canReadAll;
    }

    public boolean getCanWriteAll() {
        return canWriteAll;
    }

    public void setCanWriteAll(boolean canWriteAll) {
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
