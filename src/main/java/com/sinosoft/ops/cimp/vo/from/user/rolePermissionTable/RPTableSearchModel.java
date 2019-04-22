package com.sinosoft.ops.cimp.vo.from.user.rolePermissionTable;

import com.sinosoft.ops.cimp.dto.RePagination;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "角色关联Table 查询类")
public class RPTableSearchModel extends RePagination {

    @ApiModelProperty(value = "roleId")
    private String roleId;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
