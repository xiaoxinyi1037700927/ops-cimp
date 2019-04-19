package com.sinosoft.ops.cimp.vo.from.user.rolePermissionPageSql;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(description = "角色权限页面sql 删除类")
public class RPPageSqlDeleteModel {

    @ApiModelProperty(value = "主键id的集合", required = true)
    private List<String> ids;

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
