package com.sinosoft.ops.cimp.vo.to.homePageCount;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 角色统计项信息
 */
@ApiModel(description = "角色统计项信息")
public class RoleHomePageCountVO {

    /**
     * 主键标识ID
     */
    @ApiModelProperty(value = "主键标识ID")
    private String id;
    /**
     * 角色标识
     */
    @ApiModelProperty(value = "角色标识")
    private String roleId;

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称")
    private String roleName;
    /**
     * 统计名称
     */
    @ApiModelProperty(value = "统计名称")
    private String countName;
    /**
     * 统计语句
     */
    @ApiModelProperty(value = "统计语句")
    private String countSql;


    /**************************************/
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getCountName() {
        return countName;
    }

    public void setCountName(String countName) {
        this.countName = countName;
    }

    public String getCountSql() {
        return countSql;
    }

    public void setCountSql(String countSql) {
        this.countSql = countSql;
    }
}
