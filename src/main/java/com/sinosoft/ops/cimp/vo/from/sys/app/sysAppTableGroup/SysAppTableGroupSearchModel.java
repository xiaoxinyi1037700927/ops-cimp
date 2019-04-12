package com.sinosoft.ops.cimp.vo.from.sys.app.sysAppTableGroup;

import com.sinosoft.ops.cimp.dto.RePagination;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

@ApiModel(description = "系统应用表分组查询模型")
public class SysAppTableGroupSearchModel extends RePagination {
    /**
     * 系统应用ID
     */
    @ApiModelProperty(value = "系统应用ID")
    private String sysAppId;
    /**
     * 表分组名称
     */
    @ApiModelProperty(value = "表分组名称")
    private String name;

    public String getSysAppId() {
        return sysAppId;
    }

    public void setSysAppId(String sysAppId) {
        this.sysAppId = sysAppId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
