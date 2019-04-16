package com.sinosoft.ops.cimp.vo.to.user.permissionPage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by Jay on 2019/2/21.
 *
 * @author Jay
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinoSoft All Rights Received
 */
@ApiModel(description = "批量 启用/禁用")
public class PermissionBatchVO {

    @ApiModelProperty(value = "操作ID")
    private List<String> permissionPageOperationIds;

    @ApiModelProperty(value = "角色ID")
    private String roleId;

    @ApiModelProperty(value = "0禁用 1启用")
    private String  type;


    public List<String> getPermissionPageOperationIds() {
        return permissionPageOperationIds;
    }

    public void setPermissionPageOperationIds(List<String> permissionPageOperationIds) {
        this.permissionPageOperationIds = permissionPageOperationIds;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
