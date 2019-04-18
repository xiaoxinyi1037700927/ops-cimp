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
}
