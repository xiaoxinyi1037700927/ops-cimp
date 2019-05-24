package com.sinosoft.ops.cimp.vo.to.cadre.combinedQuery;

import com.sinosoft.ops.cimp.util.combinedQuery.beans.Expr;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;


@ApiModel(description = "组合查询模型")
public class CombinedQueryModel {
    /**
     * 组合查询id
     */
    @ApiModelProperty(value = "组合查询id")
    private String combinedQueryId;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 表达式字符串
     */
    @ApiModelProperty(value = "表达式字符串")
    private String exprstr;
    /**
     * 表达式
     */
    @ApiModelProperty(value = "表达式")
    private List<Expr> expr;

    public String getCombinedQueryId() {
        return combinedQueryId;
    }

    public void setCombinedQueryId(String combinedQueryId) {
        this.combinedQueryId = combinedQueryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExprstr() {
        return exprstr;
    }

    public void setExprstr(String exprstr) {
        this.exprstr = exprstr;
    }

    public List<Expr> getExpr() {
        return expr;
    }

    public void setExpr(List<Expr> expr) {
        this.expr = expr;
    }
}
