package com.sinosoft.ops.cimp.vo.to.sys.permission;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "AddMenuToGroupViewModel", description = "通过ID给分组增加菜单")
public class AddMenuToGroupViewModel {


    @ApiModelProperty(value = "父级Id")
    private String parentId;

    @ApiModelProperty(value = "菜单集合")
    private List<String> menuIds;


    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<String> getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(List<String> menuIds) {
        this.menuIds = menuIds;
    }
}
