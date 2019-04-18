package com.sinosoft.ops.cimp.entity.user;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户表
 */
@Entity
@Table(name="USER_INFO")
public class User {

    /**
     * 主键标识 ID
     */
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 登录名
     */
    private String loginName;
    /**
     * 登录密码
     */
    private String loginPassword;
    /**
     * 所属机构标识ID
     */
    private String organizationId;
    /**
     * 机构代码
     */
    private String organizationCode;
    /**
     * 考察组账号关联干部ID
     */
    private String reserveCadreId;
    /**
     * 电话
     */
    private String telePhone;
    /**
     * 手机号码
     */
    private String mobilePhone;
    /**
     * 角色类型
     */
    private String roleType;
    /**
     * 最后登录IP
     */
    private String lastLoginIP;
    /**
     * 最后登录时间
     */
    private Date lastLoginTime;
    /**
     * 是否被锁定(1 否, 0 是)
     */
    private Boolean locked;
    /**
     * 锁定时间
     */
    private Date lockTime;
    /**
     * 登录失败重试次数
     */
    private Integer InvalidLoginAttempts;
    /**
     * 最后登录失败时间
     */
    private Date LastInvalidLoginTime;
    /**
     * 描述
     */
    private String description;
    /**
     * 用户状态
     */
    private Integer status;

    /**
     * 数据权限单位
     */
    private String dataOrganizationId;

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
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(length = 50)
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @Column(length = 100)
    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    @Column(length = 32)
    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    @Column(length = 120)
    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }


    @Column(length = 50)
    public String getReserveCadreId() {
        return reserveCadreId;
    }

    public void setReserveCadreId(String reserveCadreId) {
        this.reserveCadreId = reserveCadreId;
    }

    @Column(length = 30)
    public String getTelePhone() {
        return telePhone;
    }

    public void setTelePhone(String telePhone) {
        this.telePhone = telePhone;
    }

    @Column(length = 30)
    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    @Column(length = 50)
    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    @Column(length = 50)
    public String getLastLoginIP() {
        return lastLoginIP;
    }

    public void setLastLoginIP(String lastLoginIP) {
        this.lastLoginIP = lastLoginIP;
    }

    @Column
    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    @Column(length = 1)
    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    @Column
    public Date getLockTime() {
        return lockTime;
    }

    public void setLockTime(Date lockTime) {
        this.lockTime = lockTime;
    }

    @Column(length = 8)
    public Integer getInvalidLoginAttempts() {
        return InvalidLoginAttempts;
    }

    public void setInvalidLoginAttempts(Integer invalidLoginAttempts) {
        InvalidLoginAttempts = invalidLoginAttempts;
    }

    @Column
    public Date getLastInvalidLoginTime() {
        return LastInvalidLoginTime;
    }

    public void setLastInvalidLoginTime(Date lastInvalidLoginTime) {
        LastInvalidLoginTime = lastInvalidLoginTime;
    }

    @Column(length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(length = 2)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(length = 32)
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

    @Column(length = 32)
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
    public String getDataOrganizationId() {
        return dataOrganizationId;
    }

    public void setDataOrganizationId(String dataOrganizationId) {
        this.dataOrganizationId = dataOrganizationId;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", loginName='" + loginName + '\'' +
                ", loginPassword='" + loginPassword + '\'' +
                ", organizationId='" + organizationId + '\'' +
                ", organizationCode='" + organizationCode + '\'' +
                ", reserveCadreId='" + reserveCadreId + '\'' +
                ", telePhone='" + telePhone + '\'' +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", roleType='" + roleType + '\'' +
                ", lastLoginIP='" + lastLoginIP + '\'' +
                ", lastLoginTime=" + lastLoginTime +
                ", locked=" + locked +
                ", lockTime=" + lockTime +
                ", InvalidLoginAttempts=" + InvalidLoginAttempts +
                ", LastInvalidLoginTime=" + LastInvalidLoginTime +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", dataOrganizationId='" + dataOrganizationId + '\'' +
                ", createId='" + createId + '\'' +
                ", createTime=" + createTime +
                ", modifyId='" + modifyId + '\'' +
                ", modifyTime=" + modifyTime +
                '}';
    }
}
