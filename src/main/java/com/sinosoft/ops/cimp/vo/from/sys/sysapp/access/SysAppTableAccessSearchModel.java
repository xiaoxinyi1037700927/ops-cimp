package com.sinosoft.ops.cimp.vo.from.sys.sysapp.access;

import com.sinosoft.ops.cimp.dto.RePagination;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;


@ApiModel(description = "角色对表访问权限查询模型")
public class SysAppTableAccessSearchModel extends RePagination {
    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID", required = true)
    @NotNull(message = "角色ID不能为空")
    private String roleId;
    /**
     * 系统应用分组ID
     */
    @ApiModelProperty(value = "系统应用分组ID")
    private String sysAppTableGroupId;
    /**
     * 表名
     */
    @ApiModelProperty(value = "表名", required = true)
    private String name;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSysAppTableGroupId() {
        return sysAppTableGroupId;
    }

    public void setSysAppTableGroupId(String sysAppTableGroupId) {
        this.sysAppTableGroupId = sysAppTableGroupId;
    }
}
