package com.sinosoft.ops.cimp.vo.to.sys.datapermission;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "角色数据权限模型")
public class RoleDataPerModel {
    /**
     * 角色数据权限id
     */
    @ApiModelProperty(value = "角色数据权限id")
    private String id;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID")
    private String roleId;
    /**
     * 接口url
     */
    @ApiModelProperty(value = "接口url")
    private String url;
    /**
     * sql类型
     */
    @ApiModelProperty(value = "sql类型")
    private String type;
    /**
     * sql类型名称
     */
    @ApiModelProperty(value = "sql类型名称")
    private String typeName;
    /**
     * sql
     */
    @ApiModelProperty(value = "sql")
    private String sql;

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

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
