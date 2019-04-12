package com.sinosoft.ops.cimp.vo.from.sys.app.sysAppTableSet;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(description = "系统表查询模型")
public class SysTableSearchModel {
    /**
     * 表分组ID
     */
    @ApiModelProperty(value = "表分组ID")
    private String sysAppTableGroupId;
    /**
     * 表类别ID
     */
    @ApiModelProperty(value = "表类别ID")
    private String sysTableTypeId;

    public String getSysAppTableGroupId() {
        return sysAppTableGroupId;
    }

    public void setSysAppTableGroupId(String sysAppTableGroupId) {
        this.sysAppTableGroupId = sysAppTableGroupId;
    }

    public String getSysTableTypeId() {
        return sysTableTypeId;
    }

    public void setSysTableTypeId(String sysTableTypeId) {
        this.sysTableTypeId = sysTableTypeId;
    }
}
