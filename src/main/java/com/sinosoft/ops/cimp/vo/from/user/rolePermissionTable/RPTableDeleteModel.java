package com.sinosoft.ops.cimp.vo.from.user.rolePermissionTable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(description = "角色关联Table 删除类")
public class RPTableDeleteModel {

    @ApiModelProperty(value = "要删除的id集合")
    private List<String> ids;

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
