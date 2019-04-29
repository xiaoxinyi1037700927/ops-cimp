package com.sinosoft.ops.cimp.vo.from.sys.check;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "系统查错查询模型")
public class SysCheckQueryDataModel {

    /**
     * 查错条件id
     */
    private String sysCheckConditionId;

    /**
     * 单位id
     */
    private String orgId;


    public String getSysCheckConditionId() {
        return sysCheckConditionId;
    }

    public void setSysCheckConditionId(String sysCheckConditionId) {
        this.sysCheckConditionId = sysCheckConditionId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
