package com.sinosoft.ops.cimp.vo.from.user.organization;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 删除业务部门
 */
@ApiModel(description = "删除业务部门")
public class BusinessUnitDeleteViewModel {
    /**
     * 业务部门Ids
     */
    @ApiModelProperty(value = "业务部门Ids", required = true)
    private List<String> ids;

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
