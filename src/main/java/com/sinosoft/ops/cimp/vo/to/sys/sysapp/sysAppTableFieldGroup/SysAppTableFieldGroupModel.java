package com.sinosoft.ops.cimp.vo.to.sys.sysapp.sysAppTableFieldGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 系统应用表字段分组模型
 */
@ApiModel(description = "系统应用表字段分组模型")
public class SysAppTableFieldGroupModel {

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 系统应用表集合ID
     */
    @ApiModelProperty(value = "系统应用表集合ID")
    private String sysAppTableSetId;
    /**
     * 字段分组名称
     */
    @ApiModelProperty(value = "字段分组名称")
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

    public String getSysAppTableSetId() {
        return sysAppTableSetId;
    }

    public void setSysAppTableSetId(String sysAppTableSetId) {
        this.sysAppTableSetId = sysAppTableSetId;
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
