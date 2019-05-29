package com.sinosoft.ops.cimp.vo.from.cadre;

import com.sinosoft.ops.cimp.dto.RePagination;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "干部列表搜索模型")
public class CadreSearchModel extends RePagination {
    public CadreSearchModel() {
    }

    public CadreSearchModel(String deptId, String includeSubNode, String combinedQueryId, String cadreTagIds, String tableConditions) {
        this.deptId = deptId;
        this.includeSubNode = includeSubNode;
        this.combinedQueryId = combinedQueryId;
        this.cadreTagIds = cadreTagIds;
        this.tableConditions = tableConditions;
    }

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
    /**
     * 干部标签id
     */
    @ApiModelProperty(value = "干部标签id")
    private String cadreTagIds;
    /**
     * 查询条件
     */
    @ApiModelProperty(value = "查询条件")
    private String tableConditions;

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

    public String getCadreTagIds() {
        return cadreTagIds;
    }

    public void setCadreTagIds(String cadreTagIds) {
        this.cadreTagIds = cadreTagIds;
    }

    public String getTableConditions() {
        return tableConditions;
    }

    public void setTableConditions(String tableConditions) {
        this.tableConditions = tableConditions;
    }

}

