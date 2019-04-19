package com.sinosoft.ops.cimp.vo.from.user.rolePermissionPageSql;

import com.sinosoft.ops.cimp.dto.RePagination;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(description = "角色权限页面sql搜索类")
public class RPPageSqlSearchModel extends RePagination {

    @ApiModelProperty(value = "roleId的集合")
    private List<String> roleIds;

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
    }
}
