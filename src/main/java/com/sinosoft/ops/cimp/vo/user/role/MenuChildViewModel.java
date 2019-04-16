package com.sinosoft.ops.cimp.vo.user.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Jay on 2018/12/9.
 *
 * @author Jay
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinoSoft All Rights Received
 */
@ApiModel(value = "MenuChildViewModel", description = "子级菜单")
public class MenuChildViewModel {

    @ApiModelProperty(value = "标识")
    private String id;

    @ApiModelProperty(value = "菜单名")
    private String name;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "地址")
    private String url;

    @ApiModelProperty(value = "icon")
    private String icon;

    @ApiModelProperty(value = "介绍")
    private String introduction;

    @ApiModelProperty(value = "颜色")
    private String color;


    @ApiModelProperty(value = "排序")
    private Integer sortNumber;

    @ApiModelProperty(value = "分组ID")
    private String parentId;

    @ApiModelProperty(value = "未处理消息集合")
    private int noDeal;

    @ApiModelProperty(value = "分组ID")
    private String groupId;

    @ApiModelProperty(value = "分组名称")
    private String groupName;

    @ApiModelProperty(value = "子页面数量")
    private Integer pageCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getNoDeal() {
        return noDeal;
    }

    public void setNoDeal(int noDeal) {
        this.noDeal = noDeal;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }
}
