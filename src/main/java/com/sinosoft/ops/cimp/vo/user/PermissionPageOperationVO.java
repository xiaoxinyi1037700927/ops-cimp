package com.sinosoft.ops.cimp.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 操作
 */
@ApiModel(description = "操作")
public class PermissionPageOperationVO {

    /**
     * 标识ID
     * */
    @ApiModelProperty(value = "标识ID")
    private String id;

    /**
     * 页面ID
     * */
    @ApiModelProperty(value = "页面ID")
    private String permissionPageId;

    /**
     * 操作名称
     * */
    @ApiModelProperty(value = "操作名称")
    private String name;

    /**
     * 描述
     * */
    @ApiModelProperty(value = "描述")
    private String  description;

    /**
     * 操作ID
     * */
    @ApiModelProperty(value = "操作ID")
    private String operationId;

    /**
     * 0禁用
     * */
    @ApiModelProperty(value = "0禁用")
    private String status;

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

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
