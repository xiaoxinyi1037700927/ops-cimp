package com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysAppTableFieldSet;

import com.sinosoft.ops.cimp.dto.RePagination;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "系统应用表字段分组查询模型")
public class SysAppTableFieldSetSearchModel extends RePagination {
    /**
     * 字段分组ID
     */
    @ApiModelProperty(value = "字段分组ID")
    private String sysAppTableFieldGroupId;
    /**
     * 表集合ID
     */
    @ApiModelProperty(value = "表集合ID")
    private String sysAppTableSetId;
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
     * 角色对表的访问权限id
     */
    @ApiModelProperty(value = "角色对表的访问权限id")
    private String sysAppRoleTableAccessId;

    public String getSysAppTableFieldGroupId() {
        return sysAppTableFieldGroupId;
    }

    public void setSysAppTableFieldGroupId(String sysAppTableFieldGroupId) {
        this.sysAppTableFieldGroupId = sysAppTableFieldGroupId;
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

    public String getSysAppRoleTableAccessId() {
        return sysAppRoleTableAccessId;
    }

    public void setSysAppRoleTableAccessId(String sysAppRoleTableAccessId) {
        this.sysAppRoleTableAccessId = sysAppRoleTableAccessId;
    }

    public String getSysAppTableSetId() {
        return sysAppTableSetId;
    }

    public void setSysAppTableSetId(String sysAppTableSetId) {
        this.sysAppTableSetId = sysAppTableSetId;
    }
}
