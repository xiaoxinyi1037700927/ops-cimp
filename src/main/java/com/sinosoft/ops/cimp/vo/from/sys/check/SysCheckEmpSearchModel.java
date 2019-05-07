package com.sinosoft.ops.cimp.vo.from.sys.check;

import com.sinosoft.ops.cimp.dto.RePagination;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "系统查错干部列表查询模型")
public class SysCheckEmpSearchModel extends RePagination {

    /**
     * 查错条件id
     */
    @ApiModelProperty(value = "查错条件id")
    private String conditionId;
    /**
     * 单位id
     */
    @ApiModelProperty(value = "单位id")
    private String orgId;
    /**
     * 搜索关键字
     */
    @ApiModelProperty(value = "搜索关键字")
    private String keywords;

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

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
