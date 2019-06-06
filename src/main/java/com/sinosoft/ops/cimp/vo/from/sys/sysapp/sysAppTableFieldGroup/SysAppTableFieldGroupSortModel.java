package com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysAppTableFieldGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "系统应用表字段分组排序修改模型")
public class SysAppTableFieldGroupSortModel {

    @ApiModelProperty(value = "源数据id")
    private String sourceId;

    @ApiModelProperty(value = "目标数据id")
    private String targetId;

    @ApiModelProperty(value = "0：移动到目标之前，1：移动到目标之后")
    private String type;

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
