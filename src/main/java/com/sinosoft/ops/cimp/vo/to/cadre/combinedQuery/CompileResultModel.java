package com.sinosoft.ops.cimp.vo.to.cadre.combinedQuery;

import com.sinosoft.ops.cimp.util.combinedQuery.beans.Expr;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;


@ApiModel(description = "表达式编译结果模型")
public class CompileResultModel {
    /**
     * 组合查询id
     */
    @ApiModelProperty(value = "组合查询id")
    private String combinedQueryId;
    /**
     * 是否编译通过
     */
    @ApiModelProperty(value = "是否编译通过")
    private boolean compilePass;
    /**
     * 错误信息
     */
    @ApiModelProperty(value = "错误信息")
    private String wrongMessage;
    /**
     * 表达式
     */
    @ApiModelProperty(value = "表达式")
    private List<Expr> exprs;
    /**
     * 表达式字符串
     */
    @ApiModelProperty(value = "表达式字符串")
    private String exprstr;

    public String getCombinedQueryId() {
        return combinedQueryId;
    }

    public void setCombinedQueryId(String combinedQueryId) {
        this.combinedQueryId = combinedQueryId;
    }

    public boolean isCompilePass() {
        return compilePass;
    }

    public void setCompilePass(boolean compilePass) {
        this.compilePass = compilePass;
    }

    public String getWrongMessage() {
        return wrongMessage;
    }

    public void setWrongMessage(String wrongMessage) {
        this.wrongMessage = wrongMessage;
    }

    public List<Expr> getExprs() {
        return exprs;
    }

    public void setExprs(List<Expr> exprs) {
        this.exprs = exprs;
    }

    public String getExprstr() {
        return exprstr;
    }

    public void setExprstr(String exprstr) {
        this.exprstr = exprstr;
    }
}
