package com.sinosoft.ops.cimp.vo.from.cadre.combinedQuery;

import com.sinosoft.ops.cimp.dto.RePagination;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description = "干部列表查询模型")
public class CadreSearchModel extends RePagination {
    /**
     * 单位id
     */
    @ApiModelProperty(value = "单位id")
    private String deptId;
    /**
     * 是否包含下级
     */
    @ApiModelProperty(value = "是否包含下级")
    private String includeSubNode;
    /**
     * 组合查询id
     */
    @ApiModelProperty(value = "组合查询id")
    private String combinedQueryId;

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getIncludeSubNode() {
        return includeSubNode;
    }

    public void setIncludeSubNode(String includeSubNode) {
        this.includeSubNode = includeSubNode;
    }

    public String getCombinedQueryId() {
        return combinedQueryId;
    }

    public void setCombinedQueryId(String combinedQueryId) {
        this.combinedQueryId = combinedQueryId;
    }
}
