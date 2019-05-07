package com.sinosoft.ops.cimp.vo.from.sys.check;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "系统查错结果模型")
public class SysCheckResultItemModel {
    /**
     * 查错条件id
     */
    @ApiModelProperty(value = "查错条件id")
    private String conditionId;
    /**
     * 查错条件名称
     */
    @ApiModelProperty(value = "查错条件名称")
    private String conditionName;

    /**
     * 错误数量
     */
    @ApiModelProperty(value = "错误数量")
    private int wrongNum;

    /**
     * 完成进度
     */
    @ApiModelProperty(value = "完成进度")
    private int completeSchedule;

    public String getConditionId() {
        return conditionId;
    }

    public void setConditionId(String conditionId) {
        this.conditionId = conditionId;
    }

    public String getConditionName() {
        return conditionName;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }

    public int getWrongNum() {
        return wrongNum;
    }

    public void setWrongNum(int wrongNum) {
        this.wrongNum = wrongNum;
    }

    public int getCompleteSchedule() {
        return completeSchedule;
    }

    public void setCompleteSchedule(int completeSchedule) {
        this.completeSchedule = completeSchedule;
    }
}
