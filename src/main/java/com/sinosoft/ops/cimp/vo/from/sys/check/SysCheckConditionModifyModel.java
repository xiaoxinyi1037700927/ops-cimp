package com.sinosoft.ops.cimp.vo.from.sys.check;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel(description = "系统应用修改模型")
public class SysCheckConditionModifyModel {
    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private String id;
    /**
     * 查错条件名称
     */
    @ApiModelProperty(value = "查错条件名称")
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
    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String modifyId;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date modifyTime;

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

    public String getModifyId() {
        return modifyId;
    }

    public void setModifyId(String modifyId) {
        this.modifyId = modifyId;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getSysTableNameEn() {
        return sysTableNameEn;
    }

    public void setSysTableNameEn(String sysTableNameEn) {
        this.sysTableNameEn = sysTableNameEn;
    }
}
