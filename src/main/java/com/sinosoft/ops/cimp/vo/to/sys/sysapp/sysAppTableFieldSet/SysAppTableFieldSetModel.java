package com.sinosoft.ops.cimp.vo.to.sys.sysapp.sysAppTableFieldSet;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 系统应用表字段集合模型
 */
@ApiModel(description = "系统应用表字段集合模型")
public class SysAppTableFieldSetModel {
    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 字段分组ID
     */
    @ApiModelProperty(value = "字段分组ID")
    private String sysAppTableFieldGroupId;
    /**
     * 系统表字段ID
     */
    @ApiModelProperty(value = "系统表字段ID")
    private String sysTableFieldId;
    /**
     * 字段名称
     */
    @ApiModelProperty(value = "字段名称")
    private String name;
    /**
     * 字段名称是否修改
     */
    @ApiModelProperty(value = "字段名称是否修改")
    private boolean nameChanged;
    /**
     * 英文字段名
     */
    @ApiModelProperty(value = "英文字段名")
    private String nameEn;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;
    /**
     * 代码模板
     */
    @ApiModelProperty(value = "代码模板")
    private String html;
    /**
     * 代码模板是否修改
     */
    @ApiModelProperty(value = "代码模板是否修改")
    private boolean htmlChanged;
    /**
     * 脚本
     */
    @ApiModelProperty(value = "脚本")
    private String script;
    /**
     * 脚本是否修改
     */
    @ApiModelProperty(value = "脚本是否修改")
    private boolean scriptChanged;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSysAppTableFieldGroupId() {
        return sysAppTableFieldGroupId;
    }

    public void setSysAppTableFieldGroupId(String sysAppTableFieldGroupId) {
        this.sysAppTableFieldGroupId = sysAppTableFieldGroupId;
    }

    public String getSysTableFieldId() {
        return sysTableFieldId;
    }

    public void setSysTableFieldId(String sysTableFieldId) {
        this.sysTableFieldId = sysTableFieldId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public boolean isNameChanged() {
        return nameChanged;
    }

    public void setNameChanged(boolean nameChanged) {
        this.nameChanged = nameChanged;
    }

    public boolean isHtmlChanged() {
        return htmlChanged;
    }

    public void setHtmlChanged(boolean htmlChanged) {
        this.htmlChanged = htmlChanged;
    }

    public boolean isScriptChanged() {
        return scriptChanged;
    }

    public void setScriptChanged(boolean scriptChanged) {
        this.scriptChanged = scriptChanged;
    }
}
