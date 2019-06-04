package com.sinosoft.ops.cimp.vo.to.cadre.combinedQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description = "组合查询统计结果模型")
public class CombinedQueryStatisticsModel {
    /**
     * 模板总数
     */
    @ApiModelProperty(value = "模板总数")
    private int total;
    /**
     * 常用模板
     */
    @ApiModelProperty(value = "常用模板")
    private int commonlyUsed;
    /**
     * 超过6个月未使用过的模板
     */
    @ApiModelProperty(value = "超过6个月未使用过的模板")
    private int unusedInSixMonths;

    public void incrCommonlyUsed() {
        commonlyUsed++;
    }

    public void incrUnusedInSixMonthsUsed() {
        unusedInSixMonths++;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCommonlyUsed() {
        return commonlyUsed;
    }

    public void setCommonlyUsed(int commonlyUsed) {
        this.commonlyUsed = commonlyUsed;
    }

    public int getUnusedInSixMonths() {
        return unusedInSixMonths;
    }

    public void setUnusedInSixMonths(int unusedInSixMonths) {
        this.unusedInSixMonths = unusedInSixMonths;
    }
}
