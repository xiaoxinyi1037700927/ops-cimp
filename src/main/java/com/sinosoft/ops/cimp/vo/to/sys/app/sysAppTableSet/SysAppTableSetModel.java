package com.sinosoft.ops.cimp.vo.to.sys.app.sysAppTableSet;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 系统应用表集合模型
 */
@ApiModel(description = "系统应用表集合模型")
public class SysAppTableSetModel {

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 表分组ID
     */
    @ApiModelProperty(value = "表分组ID")
    private String sysAppTableGroupId;
    /**
     * 系统表ID
     */
    @ApiModelProperty(value = "系统表ID")
    private String sysTableId;
    /**
     * 表名称
     */
    @ApiModelProperty(value = "表名称")
    private String name;
    /**
     * 表名称是否修改过
     */
    @ApiModelProperty(value = "表名称是否修改过")
    private boolean nameChanged;
    /**
     * 英文表名
     */
    @ApiModelProperty(value = "英文表名")
    private String nameEn;
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

    public String getSysAppTableGroupId() {
        return sysAppTableGroupId;
    }

    public void setSysAppTableGroupId(String sysAppTableGroupId) {
        this.sysAppTableGroupId = sysAppTableGroupId;
    }

    public String getSysTableId() {
        return sysTableId;
    }

    public void setSysTableId(String sysTableId) {
        this.sysTableId = sysTableId;
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

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public boolean isNameChanged() {
        return nameChanged;
    }

    public void setNameChanged(boolean nameChanged) {
        this.nameChanged = nameChanged;
    }

}
