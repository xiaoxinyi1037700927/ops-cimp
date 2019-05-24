package com.sinosoft.ops.cimp.entity.archive;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * BUS_ARCHAPPLY entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BUS_ARCHAPPLY")
public class BusArchApply implements java.io.Serializable {

    // Fields
    private static final long serialVersionUID = -8465178198105129201L;
    private String id;
    private String userid;
    private String loginname;
    private String reason;
    private String revokeReason;
    private String description;
    private Integer ordinal;
    private Timestamp endTime;	
    private Timestamp createdTime;
    private String createdBy;
    private Integer verifyType;
    private Timestamp verifyTime;
    private String verifyBy;

    @Transient
    private String personName;

    // Constructors

    /** default constructor */
    public BusArchApply() {
    }

    /** minimal constructor */
    public BusArchApply(String id, String userid, String loginname, String reason,Timestamp endtime, Timestamp createdTime, String createdBy) {
        this.id = id;
        this.userid = userid;
        this.loginname = loginname;
        this.reason = reason;
        this.endTime = endtime;
        this.createdTime =createdTime;
        this.createdBy = createdBy;
    }

    // Property accessors
    @Id
    @Column(name = "ID", unique = true, nullable = false)
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "LOGIN_NAME")
    public String getName() {
        return this.loginname;
    }

    public void setName(String loginname) {
        this.loginname = loginname;
    }
    
     @Column(name = "USER_ID")
    public String getUserid() {
        return this.userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }   
    
    @Column(name = "REASON")
    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
    
    @Column(name = "REVOKE_REASON")
    public String getRevokeReason() {
        return this.revokeReason;
    }

    public void setRevokeReason(String revokereason) {
        this.revokeReason = revokereason;
    }

    @Column(name = "ENDTIME")
    public Timestamp getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Timestamp endtime) {
        this.endTime = endtime;
    }
    
    @Column(name = "DESCRIPTION")

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "ORDINAL", precision = 8, scale = 0)

    public Integer getOrdinal() {
        return this.ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    @Column(name = "VERIFY_TYPE", precision = 8, scale = 0)
    public Integer getVerifyType() {
        return this.verifyType;
    }

    public void setVerifyType(Integer verifyType) {
        this.verifyType = verifyType;
    }

    @Column(name = "CREATED_TIME")

    public Timestamp getCreatedTime() {
        return this.createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    @Column(name = "CREATED_BY")

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name = "VERIFY_TIME")
    public Timestamp getVerifyTime() {
        return this.verifyTime;
    }

    public void setVerifyTime(Timestamp verifyTime) {
        this.verifyTime = verifyTime;
    }

    @Column(name = "VERIFY_BY")
    public String getVerifyBy() {
        return this.verifyBy;
    }

    public void setVerifyBy(String verifyBy) {
        this.verifyBy = verifyBy;
    }
    
    
    @Transient
    public String getPersonName() {
        return this.personName;
    }

    @Transient
    public void setPersonName(String personName) {
        this.personName = personName;
    }

}