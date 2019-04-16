package com.sinosoft.ops.cimp.dao.domain.sys.app;

import java.io.Serializable;

public class SysAppTableInfo implements Serializable {
    private static final long serialVersionUID = -2880439367204773326L;

    //项目唯一标识
    private String appId;
    //项目编号
    private String prjCode;
    //项目名称
    private String appName;
    //项目对系统表（信息集）的分组的记录id
    private String appTableGroupId;
    //项目对系统表（信息集）的分组名称
    private String appTableGroupName;
    //项目对系统表（信息集）的分组排序
    private Integer appTableGroupSort;
    //信息集分组下的信息集唯一标识
    private String appTableSetId;
    //信息集对应的系统表的主键值
    private String sysTableId;
    //项目信息集中文名
    private String appTableNameCn;
    //项目信息集英文名
    private String appTableNameEn;
    //项目信息集排序
    private Integer appTableNameSort;
    //系统表信息集中文名
    private String sysTableNameCn;
    //系统表信息集英文名
    private String sysTableNameEn;
    //项目字段分组唯一编号
    private String appTableFieldGroupId;
    //项目字段唯一编号
    private String appTableFieldId;
    //项目字段分组名称
    private String appTableFieldGroupName;
    //项目字段分组排序
    private Integer appTableFieldGroupSort;
    //系统字段唯一编号
    private String sysTableFieldId;
    //项目字段名称
    private String appTableFieldName;
    //项目字段名称
    private Integer appTableFieldSort;
    //项目字段名称
    private String sysTableFieldName;
    //项目字段html代码
    private String appTableFieldHtml;
    //项目字段javascript脚本代码
    private String appTableFieldScript;
    //系统字段html代码
    private String sysTableFieldHtml;
    //系统字段javascript脚本代码
    private String sysTableFieldScript;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPrjCode() {
        return prjCode;
    }

    public void setPrjCode(String prjCode) {
        this.prjCode = prjCode;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppTableGroupId() {
        return appTableGroupId;
    }

    public void setAppTableGroupId(String appTableGroupId) {
        this.appTableGroupId = appTableGroupId;
    }

    public String getAppTableGroupName() {
        return appTableGroupName;
    }

    public void setAppTableGroupName(String appTableGroupName) {
        this.appTableGroupName = appTableGroupName;
    }

    public Integer getAppTableGroupSort() {
        return appTableGroupSort;
    }

    public void setAppTableGroupSort(Integer appTableGroupSort) {
        this.appTableGroupSort = appTableGroupSort;
    }

    public String getAppTableSetId() {
        return appTableSetId;
    }

    public void setAppTableSetId(String appTableSetId) {
        this.appTableSetId = appTableSetId;
    }

    public String getSysTableId() {
        return sysTableId;
    }

    public void setSysTableId(String sysTableId) {
        this.sysTableId = sysTableId;
    }

    public String getAppTableNameCn() {
        return appTableNameCn;
    }

    public void setAppTableNameCn(String appTableNameCn) {
        this.appTableNameCn = appTableNameCn;
    }

    public String getAppTableNameEn() {
        return appTableNameEn;
    }

    public void setAppTableNameEn(String appTableNameEn) {
        this.appTableNameEn = appTableNameEn;
    }

    public String getSysTableNameCn() {
        return sysTableNameCn;
    }

    public void setSysTableNameCn(String sysTableNameCn) {
        this.sysTableNameCn = sysTableNameCn;
    }

    public String getSysTableNameEn() {
        return sysTableNameEn;
    }

    public void setSysTableNameEn(String sysTableNameEn) {
        this.sysTableNameEn = sysTableNameEn;
    }

    public Integer getAppTableNameSort() {
        return appTableNameSort;
    }

    public void setAppTableNameSort(Integer appTableNameSort) {
        this.appTableNameSort = appTableNameSort;
    }

    public String getAppTableFieldGroupId() {
        return appTableFieldGroupId;
    }

    public void setAppTableFieldGroupId(String appTableFieldGroupId) {
        this.appTableFieldGroupId = appTableFieldGroupId;
    }

    public String getAppTableFieldId() {
        return appTableFieldId;
    }

    public void setAppTableFieldId(String appTableFieldId) {
        this.appTableFieldId = appTableFieldId;
    }

    public String getAppTableFieldGroupName() {
        return appTableFieldGroupName;
    }

    public void setAppTableFieldGroupName(String appTableFieldGroupName) {
        this.appTableFieldGroupName = appTableFieldGroupName;
    }

    public Integer getAppTableFieldGroupSort() {
        return appTableFieldGroupSort;
    }

    public void setAppTableFieldGroupSort(Integer appTableFieldGroupSort) {
        this.appTableFieldGroupSort = appTableFieldGroupSort;
    }

    public String getSysTableFieldId() {
        return sysTableFieldId;
    }

    public void setSysTableFieldId(String sysTableFieldId) {
        this.sysTableFieldId = sysTableFieldId;
    }

    public String getAppTableFieldName() {
        return appTableFieldName;
    }

    public void setAppTableFieldName(String appTableFieldName) {
        this.appTableFieldName = appTableFieldName;
    }

    public Integer getAppTableFieldSort() {
        return appTableFieldSort;
    }

    public void setAppTableFieldSort(Integer appTableFieldSort) {
        this.appTableFieldSort = appTableFieldSort;
    }

    public String getSysTableFieldName() {
        return sysTableFieldName;
    }

    public void setSysTableFieldName(String sysTableFieldName) {
        this.sysTableFieldName = sysTableFieldName;
    }

    public String getAppTableFieldHtml() {
        return appTableFieldHtml;
    }

    public void setAppTableFieldHtml(String appTableFieldHtml) {
        this.appTableFieldHtml = appTableFieldHtml;
    }

    public String getAppTableFieldScript() {
        return appTableFieldScript;
    }

    public void setAppTableFieldScript(String appTableFieldScript) {
        this.appTableFieldScript = appTableFieldScript;
    }

    public String getSysTableFieldHtml() {
        return sysTableFieldHtml;
    }

    public void setSysTableFieldHtml(String sysTableFieldHtml) {
        this.sysTableFieldHtml = sysTableFieldHtml;
    }

    public String getSysTableFieldScript() {
        return sysTableFieldScript;
    }

    public void setSysTableFieldScript(String sysTableFieldScript) {
        this.sysTableFieldScript = sysTableFieldScript;
    }
}
