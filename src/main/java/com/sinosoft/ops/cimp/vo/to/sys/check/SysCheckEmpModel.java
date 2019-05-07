package com.sinosoft.ops.cimp.vo.to.sys.check;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 系统查错干部模型
 */
@ApiModel(description = "系统查错条件模型")
public class SysCheckEmpModel {
    /**
     * 干部id
     */
    @ApiModelProperty(value = "干部id")
    private String empId;
    /**
     * 干部名称
     */
    @ApiModelProperty(value = "干部名称")
    private String name;
    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    private String gender;
    /**
     * 出生日期
     */
    @ApiModelProperty(value = "出生日期")
    private String birthday;
    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String organization;
    /**
     * 职务
     */
    @ApiModelProperty(value = "职务")
    private String position;
    /**
     * 职级
     */
    @ApiModelProperty(value = "职级")
    private String positionLevel;
    /**
     * 对应系统表英文名
     */
    @ApiModelProperty(value = "对应系统表英文名")
    private String sysTableNameEn;

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPositionLevel() {
        return positionLevel;
    }

    public void setPositionLevel(String positionLevel) {
        this.positionLevel = positionLevel;
    }

    public String getSysTableNameEn() {
        return sysTableNameEn;
    }

    public void setSysTableNameEn(String sysTableNameEn) {
        this.sysTableNameEn = sysTableNameEn;
    }
}
