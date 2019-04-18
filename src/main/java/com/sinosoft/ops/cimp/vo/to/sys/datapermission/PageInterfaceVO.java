package com.sinosoft.ops.cimp.vo.to.sys.datapermission;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 页面接口
 */
@ApiModel(description = "页面接口")
public class PageInterfaceVO {

    /**
     * 主键标识ID
     */
    @ApiModelProperty(value = "id")
    private String id;

    /**
     * 页面ID
     * */
    @ApiModelProperty(value = "页面ID")
    private String permissionPageId;

    /**
     * 名称
     * */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 显示名称
     * */
    @ApiModelProperty(value = "显示名称")
    private String showName;

    /**
     * 描述
     * */
    @ApiModelProperty(value = "描述")
    private String description;

    /**
     * 默认接口sql
     * */
    @ApiModelProperty(value = "默认sql")
    private String sql;

    /**
     * 角色sql
     * */
    @ApiModelProperty(value = "角色sql")
    private String roleSql;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPermissionPageId() {
        return permissionPageId;
    }

    public void setPermissionPageId(String permissionPageId) {
        this.permissionPageId = permissionPageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getRoleSql() {
        return roleSql;
    }

    public void setRoleSql(String roleSql) {
        this.roleSql = roleSql;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }
}
