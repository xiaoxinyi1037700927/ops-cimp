package com.sinosoft.ops.cimp.vo.from.sys.dataPermission;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(description = "角色数据权限删除模型")
public class RoleDataPerDeleteModel {
    /**
     * 角色数据权限id列表
     */
    @ApiModelProperty(value = "角色数据权限id列表")
    private List<String> ids;

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
