package com.sinosoft.ops.cimp.vo.to.user;


import com.sinosoft.ops.cimp.vo.user.role.MenuChildViewModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by Jay on 2019/1/2.
 *
 * @author Jay
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinoSoft All Rights Received
 */
@ApiModel(value = "SaveMenuGroupSortViewModel", description = "保存排序")
public class SaveMenuGroupSortViewModel {

    @ApiModelProperty(value = "父级Id")
    private String parentId;

    @ApiModelProperty(value = "子集")
    private List<MenuChildViewModel> menuChildViewModelList;


    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<MenuChildViewModel> getMenuChildViewModelList() {
        return menuChildViewModelList;
    }

    public void setMenuChildViewModelList(List<MenuChildViewModel> menuChildViewModelList) {
        this.menuChildViewModelList = menuChildViewModelList;
    }
}
