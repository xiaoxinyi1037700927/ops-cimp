package com.sinosoft.ops.cimp.vo.from.sys.oplog;

import java.io.Serializable;

public class SysOperationLogSearchVO implements Serializable {
    private static final long serialVersionUID = -172038276082166836L;

    //登陆名
    private String loginName;
    //用户名
    private String userName;
    //操作开始时间
    private String opStartTime;
    //操作结束时间
    private String opEndTime;
    //起始页
    private String pageIndex;
    //页面大小
    private String pageSize;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOpStartTime() {
        return opStartTime;
    }

    public void setOpStartTime(String opStartTime) {
        this.opStartTime = opStartTime;
    }

    public String getOpEndTime() {
        return opEndTime;
    }

    public void setOpEndTime(String opEndTime) {
        this.opEndTime = opEndTime;
    }

    public String getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(String pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }
}
