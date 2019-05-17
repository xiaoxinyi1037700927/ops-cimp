package com.sinosoft.ops.cimp.entity.sys.oplog;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "SYS_OPERATION_LOG")
public class SysOperationLog implements Serializable {
    private static final long serialVersionUID = -5408607121444984405L;

    //记录id
    private String id;
    //操作用户id
    private String userId;
    //操作用户的账号
    private String loginName;
    //操作用户的名称
    private String userName;
    //操作内容
    private String logDetail;
    //执行内容
    private String execDetail;
    //执行方法
    private Integer execMethod;
    //操作时间
    private Date opDateTime;
    //操作用户的ip地址
    private String opFromIp;
    //操作用户的Mac地址
    private String opFromMac;

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
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(length = 50)
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @Column(length = 50)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(length = 4000)
    public String getLogDetail() {
        return logDetail;
    }

    public void setLogDetail(String logDetail) {
        this.logDetail = logDetail;
    }

    @Column(length = 4000)
    public String getExecDetail() {
        return execDetail;
    }

    public void setExecDetail(String execDetail) {
        this.execDetail = execDetail;
    }

    @Column(length = 100)
    public Integer getExecMethod() {
        return execMethod;
    }

    public void setExecMethod(Integer execMethod) {
        this.execMethod = execMethod;
    }

    public Date getOpDateTime() {
        return opDateTime;
    }

    public void setOpDateTime(Date opDateTime) {
        this.opDateTime = opDateTime;
    }

    @Column(length = 50)
    public String getOpFromIp() {
        return opFromIp;
    }

    public void setOpFromIp(String opFromIp) {
        this.opFromIp = opFromIp;
    }

    @Column(length = 200)
    public String getOpFromMac() {
        return opFromMac;
    }

    public void setOpFromMac(String opFromMac) {
        this.opFromMac = opFromMac;
    }
}
