package com.sinosoft.ops.cimp.vo.from.sys.app.sysAppTableFieldGroup;

import com.sinosoft.ops.cimp.dto.RePagination;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "系统应用表字段分组查询模型")
public class SysAppTableFieldGroupSearchModel extends RePagination {
    /**
     * 系统应用表集合ID
     */
    @ApiModelProperty(value = "系统应用表集合ID")
    private String sysAppTableSetId;
    /**
     * 字段分组名称
     */
    @ApiModelProperty(value = "字段分组名称")
    private String name;

    public String getSysAppTableSetId() {
        return sysAppTableSetId;
    }

    public void setSysAppTableSetId(String sysAppTableSetId) {
        this.sysAppTableSetId = sysAppTableSetId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
