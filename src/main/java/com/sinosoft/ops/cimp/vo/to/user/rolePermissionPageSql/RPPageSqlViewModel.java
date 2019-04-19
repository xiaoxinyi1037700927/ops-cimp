package com.sinosoft.ops.cimp.vo.to.user.rolePermissionPageSql;

import com.sinosoft.ops.cimp.vo.from.user.rolePermissionPageSql.RPPageSqlAddModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;

@ApiModel(description = "角色权限页面sql 查询返回类")
public class RPPageSqlViewModel extends RPPageSqlAddModel {

    @ApiModelProperty(value = "主键id", required = true)
    @NotEmpty(message = "id不能为空")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
