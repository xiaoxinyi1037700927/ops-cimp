package com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysAppTableFieldSet;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel(description = "系统应用表字段集合修改模型")
public class SysAppTableFieldSetModifyModel {
    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private String id;
    /**
     * 字段名称
     */
    @ApiModelProperty(value = "字段名称")
    private String name;
    /**
     * 代码模板
     */
    @ApiModelProperty(value = "代码模板")
    private String html;
    /**
     * 脚本
     */
    @ApiModelProperty(value = "脚本")
    private String script;
    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String modifyId;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date modifyTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
