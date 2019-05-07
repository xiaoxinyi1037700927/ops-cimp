package com.sinosoft.ops.cimp.vo.from.sys.check;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;


@ApiModel(description = "系统查错条件添加模型")
public class SysCheckConditionAddModel {
    /**
     * 查错条件名称
     */
    @ApiModelProperty(value = "查错条件名称")
    private String name;
    /**
     * 查错条件类型
     */
    @ApiModelProperty(value = "查错条件类型")
    private String typeId;
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
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createId;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getWherePart() {
        return wherePart;
    }

    public void setWherePart(String wherePart) {
        this.wherePart = wherePart;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSysTableNameEn() {
        return sysTableNameEn;
    }

    public void setSysTableNameEn(String sysTableNameEn) {
        this.sysTableNameEn = sysTableNameEn;
    }
}
