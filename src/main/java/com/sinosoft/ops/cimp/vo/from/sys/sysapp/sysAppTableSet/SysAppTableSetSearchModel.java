package com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysAppTableSet;

import com.sinosoft.ops.cimp.dto.RePagination;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "系统应用表分组查询模型")
public class SysAppTableSetSearchModel extends RePagination {
    /**
     * 表分组ID
     */
    @ApiModelProperty(value = "表分组ID")
    private String sysAppTableGroupId;
    /**
     * 表名称
     */
    @ApiModelProperty(value = "表名称")
    private String name;

    public String getSysAppTableGroupId() {
        return sysAppTableGroupId;
    }

    public void setSysAppTableGroupId(String sysAppTableGroupId) {
        this.sysAppTableGroupId = sysAppTableGroupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
