package com.sinosoft.ops.cimp.vo.from.cadre;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(description = "修改干部所属单位模型")
public class CadreOrgModifyModel {
    /**
     * 干部id列表
     */
    @ApiModelProperty(value = "干部id列表")
    private List<String> empIds;
    /**
     * 单位id
     */
    @ApiModelProperty(value = "单位id")
    private String orgId;

    public List<String> getEmpIds() {
        return empIds;
    }

    public void setEmpIds(List<String> empIds) {
        this.empIds = empIds;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
