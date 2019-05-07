package com.sinosoft.ops.cimp.vo.from.sys.check;

import com.sinosoft.ops.cimp.dto.RePagination;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "系统查错条件查询模型")
public class SysCheckConditionSearchModel extends RePagination {

    /**
     * 查错类型id
     */
    @ApiModelProperty(value = "查错类型id")
    private String typeId;

    /**
     * 条件名称
     */
    @ApiModelProperty(value = "条件名称")
    private String name;
    /**
     * 查询条件部分
     */
    @ApiModelProperty(value = "查询条件部分")
    private String wherePart;
    /**
     * 系统表英文名
     */
    @ApiModelProperty(value = "系统表英文名")
    private String sysTableNameEn;

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
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
