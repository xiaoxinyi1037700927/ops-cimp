package com.sinosoft.ops.cimp.vo.to.cadre.combinedQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description = "表达式统计结果模型")
public class ExprStatisticsModel {
    /**
     * 嵌套表达式数量
     */
    @ApiModelProperty(value = "嵌套表达式数量")
    private int nestedExprNum;
    /**
     * 基础表达式数量
     */
    @ApiModelProperty(value = "基础表达式数量")
    private int generalExprNum;
    /**
     * 错误表达式数量
     */
    @ApiModelProperty(value = "错误表达式数量")
    private int wrongExprNum;

    public void incrNestedExprNum() {
        nestedExprNum++;
    }

    public void incrGeneralExprNum() {
        generalExprNum++;
    }

    public void incrWrongExprNum() {
        wrongExprNum++;
    }

    public int getNestedExprNum() {
        return nestedExprNum;
    }

    public void setNestedExprNum(int nestedExprNum) {
        this.nestedExprNum = nestedExprNum;
    }

    public int getGeneralExprNum() {
        return generalExprNum;
    }

    public void setGeneralExprNum(int generalExprNum) {
        this.generalExprNum = generalExprNum;
    }

    public int getWrongExprNum() {
        return wrongExprNum;
    }

    public void setWrongExprNum(int wrongExprNum) {
        this.wrongExprNum = wrongExprNum;
    }
}
