package com.sinosoft.ops.cimp.vo.to.sys.permission;

import io.swagger.annotations.ApiModel;

/**
 * 页面
 */
@ApiModel(description = "页面")
public class PermissionPageVO {

    /**
     * 标识ID
     * */
    private String id;

    /**
     * 页面名称
     * */
    private String name;

    /**
     * 描述
     * */
    private String  description;

    /**
     *页面所在菜单ID
     * */
    private String permissionId;

    /**
     * 父级ID，若无父级为-1
     * */
    private String parentId;

    /**
     * URL
     * */
    private String url;

    /**
     * 排序
     * */
    private Integer sortNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(Integer sortNumber) {
        this.sortNumber = sortNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
