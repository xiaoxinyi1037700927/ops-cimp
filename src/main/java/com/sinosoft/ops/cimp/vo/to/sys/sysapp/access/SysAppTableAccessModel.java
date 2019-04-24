package com.sinosoft.ops.cimp.vo.to.sys.sysapp.access;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;

/**
 * app中角色对表的访问权限模型
 */
@ApiModel(description = "app中角色对表的访问权限模型")
public class SysAppTableAccessModel {
    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 系统表id
     */
    @ApiModelProperty(value = "系统表id")
    private String sysTableId;
    /**
     * 系统应用表ID
     */
    @ApiModelProperty(value = "系统应用表ID")
    private String sysAppTableSetId;
    /**
     * 表中文名称
     */
    @ApiModelProperty(value = "表中文名称")
    private String nameCn;
    /**
     * 表英文名称
     */
    @ApiModelProperty(value = "表英文名称")
    private String nameEn;
    /**
     * 是否对表中所有字段有读权限
     */
    @ApiModelProperty(value = "是否对表中所有字段有读权限")
    private boolean canReadAll;
    /**
     * 是否对表中所有字段有写权限
     */
    @ApiModelProperty(value = "是否对表中所有字段有写权限")
    private boolean canWriteAll;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public boolean isCanReadAll() {
        return canReadAll;
    }

    public void setCanReadAll(boolean canReadAll) {
        this.canReadAll = canReadAll;
    }

    public boolean isCanWriteAll() {
        return canWriteAll;
    }

    public void setCanWriteAll(boolean canWriteAll) {
        this.canWriteAll = canWriteAll;
    }

    public String getSysTableId() {
        return sysTableId;
    }

    public void setSysTableId(String sysTableId) {
        this.sysTableId = sysTableId;
    }

    public String getSysAppTableSetId() {
        return sysAppTableSetId;
    }

    public void setSysAppTableSetId(String sysAppTableSetId) {
        this.sysAppTableSetId = sysAppTableSetId;
    }
}
