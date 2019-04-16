package com.sinosoft.ops.cimp.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;

@ApiModel(value = "PermissionModel", description = "权限参数")
public class PermissionModel {

    @ApiModelProperty(value = "权限id")
    private String id;

    @ApiModelProperty(value = "权限名称",required = true)
    @NotEmpty(message = "权限名称不能为空")
    private String name;

    @ApiModelProperty(value = "权限描述")
    private String description;

    @ApiModelProperty(value = "权限值", required = true)
    @NotEmpty(message = "权限值不能为空")
    private String url;

    @ApiModelProperty(value = "父权限ID")
    private String parentId;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
