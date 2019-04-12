package com.sinosoft.ops.cimp.vo.from.sys.app.sysAppTableFieldSet;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "系统表字段查询模型")
public class SysTableFieldSearchModel {
    /**
     * 字段分组ID
     */
    @ApiModelProperty(value = "字段分组ID")
    private String sysAppTableFieldGroupId;

    public String getSysAppTableFieldGroupId() {
        return sysAppTableFieldGroupId;
    }

    public void setSysAppTableFieldGroupId(String sysAppTableFieldGroupId) {
        this.sysAppTableFieldGroupId = sysAppTableFieldGroupId;
    }
}
