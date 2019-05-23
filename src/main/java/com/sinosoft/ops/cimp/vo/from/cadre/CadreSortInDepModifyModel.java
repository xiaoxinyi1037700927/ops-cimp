package com.sinosoft.ops.cimp.vo.from.cadre;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(description = "修改干部在单位内排序模型")
public class CadreSortInDepModifyModel implements Serializable {
    private static final long serialVersionUID = -4352440617082895845L;
    /**
     * 单位id
     */
    @ApiModelProperty(value = "单位id")
    private String orgId;

    @ApiModelProperty(value = "需要移动的人员id")
    private String fromEmpId;

    @ApiModelProperty(value = "被移动的人员id")
    private String toEmpId;

    @ApiModelProperty(value = "移动类型(0.移动到之前1.移动到之后)")
    private String moveType;
    /**
     * 排序号
     */
    @ApiModelProperty(value = "排序号")
    private String sortNumber;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getFromEmpId() {
        return fromEmpId;
    }

    public void setFromEmpId(String fromEmpId) {
        this.fromEmpId = fromEmpId;
    }

    public String getToEmpId() {
        return toEmpId;
    }

    public void setToEmpId(String toEmpId) {
        this.toEmpId = toEmpId;
    }

    public String getMoveType() {
        return moveType;
    }

    public void setMoveType(String moveType) {
        this.moveType = moveType;
    }

    public String getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(String sortNumber) {
        this.sortNumber = sortNumber;
    }
}
