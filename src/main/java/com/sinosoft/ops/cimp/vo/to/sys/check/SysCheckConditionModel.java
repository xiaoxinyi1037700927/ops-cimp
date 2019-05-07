package com.sinosoft.ops.cimp.vo.to.sys.check;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 系统查错条件模型
 */
@ApiModel(description = "系统查错条件模型")
public class SysCheckConditionModel {
    /**
     * id
     */
    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 条件名称
     */
    @ApiModelProperty(value = "条件名称")
    private String name;
    /**
     * 查询条件部分
     */
    @ApiModelProperty(value = "条件名称")
    private String wherePart;
    /**
     * 系统表英文名
     */
    @ApiModelProperty(value = "系统表英文名")
    private String sysTableNameEn;


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

    public String getWherePart() {
        return wherePart;
    }

    public void setWherePart(String wherePart) {
        this.wherePart = wherePart;
    }

    public String getSysTableNameEn() {
        return sysTableNameEn;
    }

    public void setSysTableNameEn(String sysTableNameEn) {
        this.sysTableNameEn = sysTableNameEn;
    }
}
