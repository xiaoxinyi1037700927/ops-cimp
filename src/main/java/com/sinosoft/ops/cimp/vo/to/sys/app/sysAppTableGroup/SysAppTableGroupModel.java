package com.sinosoft.ops.cimp.vo.to.sys.app.sysAppTableGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 系统应用表分组模型
 */
@ApiModel(description = "系统应用表分组模型")
public class SysAppTableGroupModel {

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 系统应用ID
     */
    @ApiModelProperty(value = "系统应用ID")
    private String sysAppId;
    /**
     * 表分组名称
     */
    @ApiModelProperty(value = "表分组名称")
    private String name;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSysAppId() {
        return sysAppId;
    }

    public void setSysAppId(String sysAppId) {
        this.sysAppId = sysAppId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
