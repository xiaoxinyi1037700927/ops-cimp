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
    @ApiModelProperty(value = "表字段分组名称")
    private String appTableFieldGroupName;

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

    public String getAppTableFieldGroupName() {
        return appTableFieldGroupName;
    }

    public void setAppTableFieldGroupName(String appTableFieldGroupName) {
        this.appTableFieldGroupName = appTableFieldGroupName;
    }
}
