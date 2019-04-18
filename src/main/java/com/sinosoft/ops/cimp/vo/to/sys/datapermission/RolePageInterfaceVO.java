package com.sinosoft.ops.cimp.vo.to.sys.datapermission;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 角色页面接口
 */
@ApiModel(description = "角色页面接口")
public class RolePageInterfaceVO {
    /**
     * 角色ID
     * */
    @ApiModelProperty(value = "角色ID")
    private String roleId;

    /**
     * 关联接口
     */
    @ApiModelProperty(value = "关联接口ID")
    private String pageInterfaceId;

    /**
     * 接口sql
     * */
    @ApiModelProperty(value = "接口sql")
    private String sql;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getPageInterfaceId() {
        return pageInterfaceId;
    }

    public void setPageInterfaceId(String pageInterfaceId) {
        this.pageInterfaceId = pageInterfaceId;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
