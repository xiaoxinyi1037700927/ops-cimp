package com.sinosoft.ops.cimp.vo.from.cadre;

import com.sinosoft.ops.cimp.dto.RePagination;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(description = "干部列表搜索模型")
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
    /**
     * 组合查询表达式
     */
    @ApiModelProperty(value = "组合查询表达式")
    private String exprStr;
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
    /**
     * 查询条件
     */
    @ApiModelProperty(value = "姓名")
    private String name;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private List<SortModel> sorts;
    /**
     * 是否是初始化
     */
    @ApiModelProperty(value = "1：初始化，0：非初始化")
    private String isInit;
    /**
     * 人员管理状态
     */
    @ApiModelProperty(value = "1：现职人员(默认) 2:离退人员 3:调出人员 4:已去世 9:其他人员")
    private String cadreStatus = "1";

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

    public String getExprStr() {
        return exprStr;
    }

    public void setExprStr(String exprStr) {
        this.exprStr = exprStr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<SortModel> getSorts() {
        return sorts;
    }

    public void setSorts(List<SortModel> sorts) {
        this.sorts = sorts;
    }

    public String getIsInit() {
        return isInit;
    }

    public void setIsInit(String isInit) {
        this.isInit = isInit;
    }

    public String getCadreStatus() {
        return cadreStatus;
    }

    public void setCadreStatus(String cadreStatus) {
        this.cadreStatus = cadreStatus;
    }
}

