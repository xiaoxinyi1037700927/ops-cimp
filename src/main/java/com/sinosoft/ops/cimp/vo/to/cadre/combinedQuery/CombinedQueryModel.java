package com.sinosoft.ops.cimp.vo.to.cadre.combinedQuery;

import com.sinosoft.ops.cimp.util.combinedQuery.beans.Expr;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
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
    /**
     * 使用次数
     */
    @ApiModelProperty(value = "使用次数")
    private int usedTimes;
    /**
     * 最后使用时间
     */
    @ApiModelProperty(value = "最后使用时间")
    private String lastUsedTime;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createUser;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 最后维护人
     */
    @ApiModelProperty(value = "最后维护人")
    private String lastModifyUser;
    /**
     * 最后维护时间
     */
    @ApiModelProperty(value = "最后维护时间")
    private Date lastModifyTime;

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

    public int getUsedTimes() {
        return usedTimes;
    }

    public void setUsedTimes(int usedTimes) {
        this.usedTimes = usedTimes;
    }

    public String getLastUsedTime() {
        return lastUsedTime;
    }

    public void setLastUsedTime(String lastUsedTime) {
        this.lastUsedTime = lastUsedTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getLastModifyUser() {
        return lastModifyUser;
    }

    public void setLastModifyUser(String lastModifyUser) {
        this.lastModifyUser = lastModifyUser;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }
}
