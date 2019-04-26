package com.sinosoft.ops.cimp.dto.sys.table;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class SysTableFieldInfoDTO implements Serializable {
    private static final long serialVersionUID = -3412987432391670949L;

    @ApiModelProperty(value = "表字段（属性）信息唯一主键")
    private String id;
    @ApiModelProperty(value = "表字段（属性）信息英文名")
    private String fieldNameEn;
    @ApiModelProperty(value = "表字段（属性）信息中文名")
    private String fieldNameCn;
    @ApiModelProperty(value = "默认的html片段")
    private String defaultHtml;
    @ApiModelProperty(value = "默认的js片段")
    private String defaultScript;
    @ApiModelProperty(value = "排序")
    private Integer sort;
    @ApiModelProperty(value = "代码项名称")
    private String codeSetName;
    @ApiModelProperty(value = "代码项类型")
    private String codeSetType;
    @ApiModelProperty(value = "表字段分组名称")
    private String appTableFieldGroupName;
    @ApiModelProperty(value = "是否只读")
    private boolean readOnly;
    @ApiModelProperty(value = "在列表中的排序")
    private Integer sortInList;
    @ApiModelProperty(value = "在列表中是否显示")
    private boolean isShowInList;
    @ApiModelProperty(value = "是否必填")
    private boolean isNecessary;
    @ApiModelProperty(value = "字段类型")
    private String dbFieldDataType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFieldNameEn() {
        return fieldNameEn;
    }

    public void setFieldNameEn(String fieldNameEn) {
        this.fieldNameEn = fieldNameEn;
    }

    public String getFieldNameCn() {
        return fieldNameCn;
    }

    public void setFieldNameCn(String fieldNameCn) {
        this.fieldNameCn = fieldNameCn;
    }

    public String getDefaultHtml() {
        return defaultHtml;
    }

    public void setDefaultHtml(String defaultHtml) {
        this.defaultHtml = defaultHtml;
    }

    public String getDefaultScript() {
        return defaultScript;
    }

    public void setDefaultScript(String defaultScript) {
        this.defaultScript = defaultScript;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getCodeSetName() {
        return codeSetName;
    }

    public void setCodeSetName(String codeSetName) {
        this.codeSetName = codeSetName;
    }

    public String getCodeSetType() {
        return codeSetType;
    }

    public void setCodeSetType(String codeSetType) {
        this.codeSetType = codeSetType;
    }

    public String getAppTableFieldGroupName() {
        return appTableFieldGroupName;
    }

    public void setAppTableFieldGroupName(String appTableFieldGroupName) {
        this.appTableFieldGroupName = appTableFieldGroupName;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public Integer getSortInList() {
        return sortInList;
    }

    public void setSortInList(Integer sortInList) {
        this.sortInList = sortInList;
    }

    public boolean isShowInList() {
        return isShowInList;
    }

    public void setShowInList(boolean showInList) {
        isShowInList = showInList;
    }

    public boolean isNecessary() {
        return isNecessary;
    }

    public void setNecessary(boolean necessary) {
        isNecessary = necessary;
    }

    public String getDbFieldDataType() {
        return dbFieldDataType;
    }

    public void setDbFieldDataType(String dbFieldDataType) {
        this.dbFieldDataType = dbFieldDataType;
    }
}
