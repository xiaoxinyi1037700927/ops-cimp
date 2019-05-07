package com.sinosoft.ops.cimp.vo.from.user.rolePermissionTable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(description = "角色关联Table 新增类的集合")
public class RPTableAddListModel {

    @ApiModelProperty(value = "角色关联Table 新增类", required = true)
    private List<RPTableAddModel> rpTableAddModels;

    public List<RPTableAddModel> getRpTableAddModels() {
        return rpTableAddModels;
    }

    public void setRpTableAddModels(List<RPTableAddModel> rpTableAddModels) {
        this.rpTableAddModels = rpTableAddModels;
    }
}
