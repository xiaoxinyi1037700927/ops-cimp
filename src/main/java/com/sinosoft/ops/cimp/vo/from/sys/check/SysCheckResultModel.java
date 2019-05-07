package com.sinosoft.ops.cimp.vo.from.sys.check;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(description = "系统查错结果模型")
public class SysCheckResultModel {
    /**
     * 治理项总数
     */
    @ApiModelProperty(value = "治理项总数")
    private int conditionNum;

    /**
     * 存在问题数
     */
    @ApiModelProperty(value = "存在问题数")
    private int wrongNum;

    /**
     * 治理项
     */
    @ApiModelProperty(value = "治理项")
    private List<SysCheckResultItemModel> items;

    public int getConditionNum() {
        return conditionNum;
    }

    public void setConditionNum(int conditionNum) {
        this.conditionNum = conditionNum;
    }

    public int getWrongNum() {
        return wrongNum;
    }

    public void setWrongNum(int wrongNum) {
        this.wrongNum = wrongNum;
    }

    public List<SysCheckResultItemModel> getItems() {
        return items;
    }

    public void setItems(List<SysCheckResultItemModel> items) {
        this.items = items;
    }
}
