package com.sinosoft.ops.cimp.vo.user.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by Jay on 2018/12/9.
 *
 * @author Jay
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinoSoft All Rights Received
 */
@ApiModel(value = "MenuParentViewModel", description = "父级菜单")
public class MenuParentViewModel {

    @ApiModelProperty(value = "标识")
    private String id;

    @ApiModelProperty(value = "菜单名")
    private String name;

    @ApiModelProperty(value = "icon")
    private String icon;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "排序")
    private Integer sortNumber;


    @ApiModelProperty(value = "子项菜单集合")
    private List<MenuChildViewModel> children;

    @ApiModelProperty(value = "未处理消息集合")
    private int noDeal;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<MenuChildViewModel> getChildren() {
        return children;
    }

    public void setChildren(List<MenuChildViewModel> children) {
        this.children = children;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(Integer sortNumber) {
        this.sortNumber = sortNumber;
    }


    public int getNoDeal() {
        return noDeal;
    }

    public void setNoDeal(int noDeal) {
        this.noDeal = noDeal;
    }
}
