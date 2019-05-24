package com.sinosoft.ops.cimp.vo.to.cadre.combinedQuery;

import com.sinosoft.ops.cimp.util.combinedQuery.beans.Expr;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description = "表达式模型")
public class ExprModel {
    /**
     * 组合查询id
     */
    @ApiModelProperty(value = "组合查询id")
    private String combinedQueryId;
    /**
     * 表达式字符串
     */
    @ApiModelProperty(value = "表达式字符串")
    private String exprstr;
    /**
     * 表达式
     */
    @ApiModelProperty(value = "表达式")
    private Expr expr;
    /**
     * 编译是否通过
     */
    @ApiModelProperty(value = "编译是否通过")
    private boolean complilePass;

    public String getCombinedQueryId() {
        return combinedQueryId;
    }

    public void setCombinedQueryId(String combinedQueryId) {
        this.combinedQueryId = combinedQueryId;
    }

    public String getExprstr() {
        return exprstr;
    }

    public void setExprstr(String exprstr) {
        this.exprstr = exprstr;
    }

    public Expr getExpr() {
        return expr;
    }

    public void setExpr(Expr expr) {
        this.expr = expr;
    }

    public boolean isComplilePass() {
        return complilePass;
    }

    public void setComplilePass(boolean complilePass) {
        this.complilePass = complilePass;
    }
}
