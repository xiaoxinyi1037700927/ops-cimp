package com.sinosoft.ops.cimp.vo.to.sys.sysapp.access;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;

/**
 * app中角色对表字段的访问权限模型
 */
@ApiModel(description = "app中角色对表字段的访问权限模型")
public class SysAppFieldAccessModel {
    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 表字段中文名称
     */
    @ApiModelProperty(value = "表字段中文名称")
    private String nameCn;
    /**
     * 系统表字段ID
     */
    @ApiModelProperty(value = "系统表字段ID")
    private String sysTableFieldId;
    /**
     * 系统应用表字段ID
     */
    @ApiModelProperty(value = "系统应用表字段ID")
    private String sysAppTableFieldSetId;
    /**
     * 表字段英文名称
     */
    @ApiModelProperty(value = "表字段英文名称")
    private String nameEn;
    /**
     * 是否对字段有读权限
     */
    @ApiModelProperty(value = "是否对字段有读权限")
    private boolean canRead;
    /**
     * 是否对字段有写权限
     */
    @ApiModelProperty(value = "是否对字段有写权限")
    private boolean canWrite;

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

    public boolean getCanRead() {
        return canRead;
    }

    public void setCanRead(boolean canRead) {
        this.canRead = canRead;
    }

    public boolean getCanWrite() {
        return canWrite;
    }

    public void setCanWrite(boolean canWrite) {
        this.canWrite = canWrite;
    }

    public String getSysTableFieldId() {
        return sysTableFieldId;
    }

    public void setSysTableFieldId(String sysTableFieldId) {
        this.sysTableFieldId = sysTableFieldId;
    }

    public String getSysAppTableFieldSetId() {
        return sysAppTableFieldSetId;
    }

    public void setSysAppTableFieldSetId(String sysAppTableFieldSetId) {
        this.sysAppTableFieldSetId = sysAppTableFieldSetId;
    }
}
