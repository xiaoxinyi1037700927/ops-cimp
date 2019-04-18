package com.sinosoft.ops.cimp.vo.from.sys.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;

@ApiModel(value = "RoleModel", description = "角色参数")
public class RoleModel {

    @ApiModelProperty(value = "角色id")
    private String id;

    @ApiModelProperty(value = "角色名称", required = true)
    @NotEmpty(message = "角色名称不能为空")
    private String name;

    @ApiModelProperty(value = "角色值", required = true)
    @NotEmpty(message = "角色值不能为空")
    private String code;

    @ApiModelProperty(value = "父角色ID")
    private String parentId;

    @ApiModelProperty(value = "角色描述")
    private String description;


    @ApiModelProperty(value = "首页类型" )
    private String pageType;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

}
