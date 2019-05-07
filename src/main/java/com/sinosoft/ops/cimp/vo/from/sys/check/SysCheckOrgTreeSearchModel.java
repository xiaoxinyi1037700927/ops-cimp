package com.sinosoft.ops.cimp.vo.from.sys.check;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "系统查错单位树查询模型")
public class SysCheckOrgTreeSearchModel {

    /**
     * 条件id
     */
    @ApiModelProperty(value = "条件id")
    private String conditionId;

    /**
     * 单位id
     */
    @ApiModelProperty(value = "单位id")
    private String orgId;

    public String getConditionId() {
        return conditionId;
    }

    public void setConditionId(String conditionId) {
        this.conditionId = conditionId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
